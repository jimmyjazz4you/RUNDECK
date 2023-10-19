package com.amazonaws.http.apache.request.impl;

import com.amazonaws.ProxyAuthenticationMethod;
import com.amazonaws.Request;
import com.amazonaws.SdkClientException;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.http.RepeatableInputStreamRequestEntity;
import com.amazonaws.http.apache.utils.ApacheUtils;
import com.amazonaws.http.request.HttpRequestFactory;
import com.amazonaws.http.settings.HttpClientSettings;
import com.amazonaws.util.FakeIOException;
import com.amazonaws.util.SdkHttpUtils;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

public class ApacheHttpRequestFactory implements HttpRequestFactory<HttpRequestBase> {
   private static final String DEFAULT_ENCODING = "UTF-8";
   private static final String QUERY_PARAM_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=" + "UTF-8".toLowerCase();
   private static final List<String> ignoreHeaders = Arrays.asList("Content-Length", "Host");

   public HttpRequestBase create(Request<?> request, HttpClientSettings settings) throws FakeIOException {
      String endpointUri = this.getUriEndpoint(request);
      String encodedParams = SdkHttpUtils.encodeParameters(request);
      HttpRequestBase base;
      if (this.shouldMoveQueryParametersToBody(request, encodedParams)) {
         base = this.createPostParamsInBodyRequest(endpointUri, encodedParams);
         this.addHeadersToRequest(base, request);
         this.addContentTypeHeaderIfNeeded(base);
      } else {
         if (encodedParams != null) {
            endpointUri = endpointUri + "?" + encodedParams;
         }

         base = this.createStandardRequest(request, endpointUri);
         this.addHeadersToRequest(base, request);
      }

      this.addRequestConfig(base, request, settings);
      return base;
   }

   private String getUriEndpoint(Request<?> request) {
      URI endpoint = request.getEndpoint();
      return request.getOriginalRequest().getRequestClientOptions().isSkipAppendUriPath()
         ? endpoint.toString()
         : SdkHttpUtils.appendUri(endpoint.toString(), request.getResourcePath(), true);
   }

   private void addRequestConfig(HttpRequestBase base, Request<?> request, HttpClientSettings settings) {
      Builder requestConfigBuilder = RequestConfig.custom()
         .setConnectionRequestTimeout(settings.getConnectionPoolRequestTimeout())
         .setConnectTimeout(settings.getConnectionTimeout())
         .setSocketTimeout(settings.getSocketTimeout())
         .setLocalAddress(settings.getLocalAddress());
      ApacheUtils.disableNormalizeUri(requestConfigBuilder);
      if (HttpMethodName.PUT == request.getHttpMethod() && settings.isUseExpectContinue()) {
         requestConfigBuilder.setExpectContinueEnabled(true);
      }

      this.addProxyConfig(requestConfigBuilder, settings);
      base.setConfig(requestConfigBuilder.build());
   }

   private HttpRequestBase createStandardRequest(Request<?> request, String uri) throws FakeIOException {
      switch(request.getHttpMethod()) {
         case HEAD:
            return new HttpHead(uri);
         case GET:
            return this.wrapEntity(request, new HttpGetWithBody(uri));
         case DELETE:
            return new HttpDelete(uri);
         case OPTIONS:
            return new HttpOptions(uri);
         case PATCH:
            return this.wrapEntity(request, new HttpPatch(uri));
         case POST:
            return this.wrapEntity(request, new HttpPost(uri));
         case PUT:
            return this.wrapEntity(request, new HttpPut(uri));
         default:
            throw new SdkClientException("Unknown HTTP method name: " + request.getHttpMethod());
      }
   }

   private HttpRequestBase wrapEntity(Request<?> request, HttpEntityEnclosingRequestBase entityEnclosingRequest) throws FakeIOException {
      if (HttpMethodName.POST == request.getHttpMethod()) {
         this.createHttpEntityForPostVerb(request, entityEnclosingRequest);
      } else if (request.getContent() != null) {
         this.createHttpEntityForNonPostVerbs(request, entityEnclosingRequest);
      }

      return entityEnclosingRequest;
   }

   private void createHttpEntityForPostVerb(Request<?> request, HttpEntityEnclosingRequestBase entityEnclosingRequest) throws FakeIOException {
      HttpEntity entity = new RepeatableInputStreamRequestEntity(request);
      if (request.getHeaders().get("Content-Length") == null && this.isRequiresLength(request)) {
         entity = ApacheUtils.newBufferedHttpEntity(entity);
      }

      entityEnclosingRequest.setEntity(entity);
   }

   private void createHttpEntityForNonPostVerbs(Request<?> request, HttpEntityEnclosingRequestBase entityEnclosingRequest) throws FakeIOException {
      HttpEntity entity = new RepeatableInputStreamRequestEntity(request);
      if (request.getHeaders().get("Content-Length") == null && (this.isRequiresLength(request) || !this.hasStreamingInput(request))) {
         entity = ApacheUtils.newBufferedHttpEntity(entity);
      }

      entityEnclosingRequest.setEntity(entity);
   }

   private boolean isRequiresLength(Request<?> request) {
      return Boolean.TRUE.equals(request.getHandlerContext(HandlerContextKey.REQUIRES_LENGTH));
   }

   private boolean hasStreamingInput(Request<?> request) {
      return Boolean.TRUE.equals(request.getHandlerContext(HandlerContextKey.HAS_STREAMING_INPUT));
   }

   private void addHeadersToRequest(HttpRequestBase httpRequest, Request<?> request) {
      httpRequest.addHeader("Host", this.getHostHeaderValue(request.getEndpoint()));

      for(Entry<String, String> entry : request.getHeaders().entrySet()) {
         if (!ignoreHeaders.contains(entry.getKey())) {
            httpRequest.addHeader(entry.getKey(), entry.getValue());
         }
      }
   }

   private boolean shouldMoveQueryParametersToBody(Request<?> request, String encodedParams) {
      boolean requestIsPost = request.getHttpMethod() == HttpMethodName.POST;
      return requestIsPost && request.getContent() == null && encodedParams != null;
   }

   private HttpRequestBase createPostParamsInBodyRequest(String endpointUri, String encodedParams) {
      HttpEntityEnclosingRequestBase requestBase = new HttpPost(endpointUri);
      requestBase.setEntity(ApacheUtils.newStringEntity(encodedParams));
      return requestBase;
   }

   private void addContentTypeHeaderIfNeeded(HttpRequestBase base) {
      if (base.getHeaders("Content-Type") == null || base.getHeaders("Content-Type").length == 0) {
         base.addHeader("Content-Type", QUERY_PARAM_CONTENT_TYPE);
      }
   }

   private String getHostHeaderValue(URI endpoint) {
      return SdkHttpUtils.isUsingNonDefaultPort(endpoint) ? endpoint.getHost() + ":" + endpoint.getPort() : endpoint.getHost();
   }

   private void addProxyConfig(Builder requestConfigBuilder, HttpClientSettings settings) {
      if (settings.isProxyEnabled() && settings.isAuthenticatedProxy() && settings.getProxyAuthenticationMethods() != null) {
         List<String> apacheAuthenticationSchemes = new ArrayList<>();

         for(ProxyAuthenticationMethod authenticationMethod : settings.getProxyAuthenticationMethods()) {
            apacheAuthenticationSchemes.add(this.toApacheAuthenticationScheme(authenticationMethod));
         }

         requestConfigBuilder.setProxyPreferredAuthSchemes(apacheAuthenticationSchemes);
      }
   }

   private String toApacheAuthenticationScheme(ProxyAuthenticationMethod authenticationMethod) {
      if (authenticationMethod == null) {
         throw new IllegalStateException("The configured proxy authentication methods must not be null.");
      } else {
         switch(authenticationMethod) {
            case NTLM:
               return "NTLM";
            case BASIC:
               return "Basic";
            case DIGEST:
               return "Digest";
            case SPNEGO:
               return "Negotiate";
            case KERBEROS:
               return "Kerberos";
            default:
               throw new IllegalStateException("Unknown authentication scheme: " + authenticationMethod);
         }
      }
   }
}
