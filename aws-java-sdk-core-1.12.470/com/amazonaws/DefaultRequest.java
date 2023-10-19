package com.amazonaws;

import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.event.ProgressInputStream;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.util.AWSRequestMetrics;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@NotThreadSafe
public class DefaultRequest<T> implements Request<T> {
   private String resourcePath;
   private Map<String, List<String>> parameters = new LinkedHashMap<>();
   private Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
   private URI endpoint;
   private String serviceName;
   private final AmazonWebServiceRequest originalRequest;
   private HttpMethodName httpMethod = HttpMethodName.POST;
   private InputStream content;
   private int timeOffset;
   private AWSRequestMetrics metrics;
   private final Map<HandlerContextKey<?>, Object> handlerContext = new HashMap<>();

   public DefaultRequest(AmazonWebServiceRequest originalRequest, String serviceName) {
      this.serviceName = serviceName;
      this.originalRequest = originalRequest == null ? AmazonWebServiceRequest.NOOP : originalRequest;
      this.handlerContext.putAll(this.originalRequest.getHandlerContext());
   }

   public DefaultRequest(String serviceName) {
      this(null, serviceName);
   }

   @Override
   public AmazonWebServiceRequest getOriginalRequest() {
      return this.originalRequest;
   }

   @Override
   public void addHeader(String name, String value) {
      this.headers.put(name, value);
   }

   @Override
   public Map<String, String> getHeaders() {
      return this.headers;
   }

   @Override
   public void setResourcePath(String resourcePath) {
      this.resourcePath = resourcePath;
   }

   @Override
   public String getResourcePath() {
      return this.resourcePath;
   }

   @Override
   public void addParameter(String name, String value) {
      List<String> paramList = this.parameters.get(name);
      if (paramList == null) {
         paramList = new ArrayList<>();
         this.parameters.put(name, paramList);
      }

      paramList.add(value);
   }

   @Override
   public void addParameters(String name, List<String> values) {
      if (values != null) {
         for(String value : values) {
            this.addParameter(name, value);
         }
      }
   }

   @Override
   public Map<String, List<String>> getParameters() {
      return this.parameters;
   }

   @Override
   public Request<T> withParameter(String name, String value) {
      this.addParameter(name, value);
      return this;
   }

   @Override
   public HttpMethodName getHttpMethod() {
      return this.httpMethod;
   }

   @Override
   public void setHttpMethod(HttpMethodName httpMethod) {
      this.httpMethod = httpMethod;
   }

   @Override
   public void setEndpoint(URI endpoint) {
      this.endpoint = endpoint;
   }

   @Override
   public URI getEndpoint() {
      return this.endpoint;
   }

   @Override
   public String getServiceName() {
      return this.serviceName;
   }

   @Override
   public InputStream getContent() {
      return this.content;
   }

   @Override
   public void setContent(InputStream content) {
      this.content = content;
   }

   @Override
   public void setHeaders(Map<String, String> headers) {
      this.headers.clear();
      this.headers.putAll(headers);
   }

   @Override
   public void setParameters(Map<String, List<String>> parameters) {
      this.parameters.clear();
      this.parameters.putAll(parameters);
   }

   @Override
   public int getTimeOffset() {
      return this.timeOffset;
   }

   @Override
   public void setTimeOffset(int timeOffset) {
      this.timeOffset = timeOffset;
   }

   @Override
   public Request<T> withTimeOffset(int timeOffset) {
      this.setTimeOffset(timeOffset);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getHttpMethod()).append(" ");
      builder.append(this.getEndpoint()).append(" ");
      String resourcePath = this.getResourcePath();
      if (resourcePath == null) {
         builder.append("/");
      } else {
         if (!resourcePath.startsWith("/")) {
            builder.append("/");
         }

         builder.append(resourcePath);
      }

      return builder.toString();
   }

   @Override
   public AWSRequestMetrics getAWSRequestMetrics() {
      return this.metrics;
   }

   @Override
   public void setAWSRequestMetrics(AWSRequestMetrics metrics) {
      if (this.metrics == null) {
         this.metrics = metrics;
      } else {
         throw new IllegalStateException("AWSRequestMetrics has already been set on this request");
      }
   }

   @Override
   public <X> void addHandlerContext(HandlerContextKey<X> key, X value) {
      this.handlerContext.put(key, value);
   }

   @Override
   public <X> X getHandlerContext(HandlerContextKey<X> key) {
      return (X)this.handlerContext.get(key);
   }

   @Override
   public InputStream getContentUnwrapped() {
      InputStream is = this.getContent();
      if (is == null) {
         return null;
      } else {
         while(is instanceof ProgressInputStream) {
            ProgressInputStream pris = (ProgressInputStream)is;
            is = pris.getWrappedInputStream();
         }

         return is;
      }
   }

   @Override
   public ReadLimitInfo getReadLimitInfo() {
      return this.originalRequest;
   }

   @Override
   public Object getOriginalRequestObject() {
      return this.originalRequest;
   }
}
