package com.amazonaws.http.apache.utils;

import com.amazonaws.Request;
import com.amazonaws.SdkClientException;
import com.amazonaws.http.settings.HttpClientSettings;
import com.amazonaws.util.FakeIOException;
import com.amazonaws.util.ReflectionMethodInvoker;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.HttpContext;

public class ApacheUtils {
   private static final Log log = LogFactory.getLog(ApacheUtils.class);
   private static final ReflectionMethodInvoker<Builder, Builder> normalizeUriInvoker = new ReflectionMethodInvoker(
      Builder.class, Builder.class, "setNormalizeUri", Boolean.TYPE
   );
   private final boolean normalizeUriMethodNotFound = false;

   public static boolean isRequestSuccessful(HttpResponse response) {
      int status = response.getStatusLine().getStatusCode();
      return status / 100 == 2;
   }

   public static com.amazonaws.http.HttpResponse createResponse(
      Request<?> request, HttpRequestBase method, HttpResponse apacheHttpResponse, HttpContext context
   ) throws IOException {
      com.amazonaws.http.HttpResponse httpResponse = new com.amazonaws.http.HttpResponse(request, method, context);
      if (apacheHttpResponse.getEntity() != null) {
         httpResponse.setContent(apacheHttpResponse.getEntity().getContent());
      }

      httpResponse.setStatusCode(apacheHttpResponse.getStatusLine().getStatusCode());
      httpResponse.setStatusText(apacheHttpResponse.getStatusLine().getReasonPhrase());

      for(Header header : apacheHttpResponse.getAllHeaders()) {
         httpResponse.addHeader(header.getName(), header.getValue());
      }

      return httpResponse;
   }

   public static HttpEntity newStringEntity(String s) {
      try {
         return new StringEntity(s);
      } catch (UnsupportedEncodingException var2) {
         throw new SdkClientException("Unable to create HTTP entity: " + var2.getMessage(), var2);
      }
   }

   public static HttpEntity newBufferedHttpEntity(HttpEntity entity) throws FakeIOException {
      try {
         return new BufferedHttpEntity(entity);
      } catch (FakeIOException var2) {
         throw var2;
      } catch (IOException var3) {
         throw new SdkClientException("Unable to create HTTP entity: " + var3.getMessage(), var3);
      }
   }

   public static HttpClientContext newClientContext(HttpClientSettings settings, Map<String, ? extends Object> attributes) {
      HttpClientContext clientContext = new HttpClientContext();
      if (attributes != null && !attributes.isEmpty()) {
         for(Entry<String, ?> entry : attributes.entrySet()) {
            clientContext.setAttribute(entry.getKey(), entry.getValue());
         }
      }

      addPreemptiveAuthenticationProxy(clientContext, settings);
      Builder builder = RequestConfig.custom();
      disableNormalizeUri(builder);
      clientContext.setRequestConfig(builder.build());
      clientContext.setAttribute("com.amazonaws.disableSocketProxy", settings.disableSocketProxy());
      return clientContext;
   }

   public static void disableNormalizeUri(Builder requestConfigBuilder) {
      if (normalizeUriInvoker.isInitialized()) {
         try {
            normalizeUriInvoker.invoke(requestConfigBuilder, false);
         } catch (NoSuchMethodException var2) {
            noSuchMethodThrownByNormalizeUriInvoker();
         }
      }
   }

   public static CredentialsProvider newProxyCredentialsProvider(HttpClientSettings settings) {
      CredentialsProvider provider = new BasicCredentialsProvider();
      provider.setCredentials(newAuthScope(settings), newNTCredentials(settings));
      return provider;
   }

   private static Credentials newNTCredentials(HttpClientSettings settings) {
      return new NTCredentials(settings.getProxyUsername(), settings.getProxyPassword(), settings.getProxyWorkstation(), settings.getProxyDomain());
   }

   private static AuthScope newAuthScope(HttpClientSettings settings) {
      return new AuthScope(settings.getProxyHost(), settings.getProxyPort());
   }

   private static void addPreemptiveAuthenticationProxy(HttpClientContext clientContext, HttpClientSettings settings) {
      if (settings.isPreemptiveBasicProxyAuth()) {
         HttpHost targetHost = new HttpHost(settings.getProxyHost(), settings.getProxyPort());
         CredentialsProvider credsProvider = newProxyCredentialsProvider(settings);
         AuthCache authCache = new BasicAuthCache();
         BasicScheme basicAuth = new BasicScheme();
         authCache.put(targetHost, basicAuth);
         clientContext.setCredentialsProvider(credsProvider);
         clientContext.setAuthCache(authCache);
      }
   }

   private static void noSuchMethodThrownByNormalizeUriInvoker() {
      log.warn(
         "NoSuchMethodException was thrown when disabling normalizeUri. This indicates you are using an old version (< 4.5.8) of Apache http client. It is recommended to use http client version >= 4.5.9 to avoid the breaking change introduced in apache client 4.5.7 and the latency in exception handling. See https://github.com/aws/aws-sdk-java/issues/1919 for more information"
      );
   }

   static {
      try {
         normalizeUriInvoker.initialize();
      } catch (NoSuchMethodException var1) {
         noSuchMethodThrownByNormalizeUriInvoker();
      }
   }
}
