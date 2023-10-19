package com.amazonaws;

import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.metrics.RequestMetricCollector;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@NotThreadSafe
public abstract class AmazonWebServiceRequest implements Cloneable, ReadLimitInfo, HandlerContextAware {
   public static final AmazonWebServiceRequest NOOP = new AmazonWebServiceRequest() {
   };
   private ProgressListener progressListener = ProgressListener.NOOP;
   private final RequestClientOptions requestClientOptions = new RequestClientOptions();
   private RequestMetricCollector requestMetricCollector;
   private AWSCredentialsProvider credentialsProvider;
   private Map<String, String> customRequestHeaders;
   private Map<String, List<String>> customQueryParameters;
   private transient Map<HandlerContextKey<?>, Object> handlerContext = new HashMap<>();
   private AmazonWebServiceRequest cloneSource;
   private Integer sdkRequestTimeout = null;
   private Integer sdkClientExecutionTimeout = null;

   @Deprecated
   public void setRequestCredentials(AWSCredentials credentials) {
      this.credentialsProvider = credentials == null ? null : new StaticCredentialsProvider(credentials);
   }

   @Deprecated
   public AWSCredentials getRequestCredentials() {
      return this.credentialsProvider == null ? null : this.credentialsProvider.getCredentials();
   }

   public void setRequestCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
      this.credentialsProvider = credentialsProvider;
   }

   public AWSCredentialsProvider getRequestCredentialsProvider() {
      return this.credentialsProvider;
   }

   public <T extends AmazonWebServiceRequest> T withRequestCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
      this.setRequestCredentialsProvider(credentialsProvider);
      return (T)this;
   }

   public RequestClientOptions getRequestClientOptions() {
      return this.requestClientOptions;
   }

   public RequestMetricCollector getRequestMetricCollector() {
      return this.requestMetricCollector;
   }

   public void setRequestMetricCollector(RequestMetricCollector requestMetricCollector) {
      this.requestMetricCollector = requestMetricCollector;
   }

   public <T extends AmazonWebServiceRequest> T withRequestMetricCollector(RequestMetricCollector metricCollector) {
      this.setRequestMetricCollector(metricCollector);
      return (T)this;
   }

   public void setGeneralProgressListener(ProgressListener progressListener) {
      this.progressListener = progressListener == null ? ProgressListener.NOOP : progressListener;
   }

   public ProgressListener getGeneralProgressListener() {
      return this.progressListener;
   }

   public <T extends AmazonWebServiceRequest> T withGeneralProgressListener(ProgressListener progressListener) {
      this.setGeneralProgressListener(progressListener);
      return (T)this;
   }

   public Map<String, String> getCustomRequestHeaders() {
      return this.customRequestHeaders == null ? null : Collections.unmodifiableMap(this.customRequestHeaders);
   }

   public String putCustomRequestHeader(String name, String value) {
      if (this.customRequestHeaders == null) {
         this.customRequestHeaders = new HashMap<>();
      }

      return this.customRequestHeaders.put(name, value);
   }

   public Map<String, List<String>> getCustomQueryParameters() {
      return this.customQueryParameters == null ? null : Collections.unmodifiableMap(this.customQueryParameters);
   }

   public void putCustomQueryParameter(String name, String value) {
      if (this.customQueryParameters == null) {
         this.customQueryParameters = new HashMap<>();
      }

      List<String> paramList = this.customQueryParameters.get(name);
      if (paramList == null) {
         paramList = new LinkedList<>();
         this.customQueryParameters.put(name, paramList);
      }

      paramList.add(value);
   }

   @Override
   public final int getReadLimit() {
      return this.requestClientOptions.getReadLimit();
   }

   protected final <T extends AmazonWebServiceRequest> T copyBaseTo(T target) {
      if (this.customRequestHeaders != null) {
         for(Entry<String, String> e : this.customRequestHeaders.entrySet()) {
            target.putCustomRequestHeader(e.getKey(), e.getValue());
         }
      }

      if (this.customQueryParameters != null) {
         for(Entry<String, List<String>> e : this.customQueryParameters.entrySet()) {
            if (e.getValue() != null) {
               for(String value : e.getValue()) {
                  target.putCustomQueryParameter(e.getKey(), value);
               }
            }
         }
      }

      target.setRequestCredentialsProvider(this.credentialsProvider);
      target.setGeneralProgressListener(this.progressListener);
      target.setRequestMetricCollector(this.requestMetricCollector);
      this.requestClientOptions.copyTo(target.getRequestClientOptions());
      return target;
   }

   public AmazonWebServiceRequest getCloneSource() {
      return this.cloneSource;
   }

   public AmazonWebServiceRequest getCloneRoot() {
      AmazonWebServiceRequest cloneRoot = this.cloneSource;
      if (cloneRoot != null) {
         while(cloneRoot.getCloneSource() != null) {
            cloneRoot = cloneRoot.getCloneSource();
         }
      }

      return cloneRoot;
   }

   private void setCloneSource(AmazonWebServiceRequest cloneSource) {
      this.cloneSource = cloneSource;
   }

   public Integer getSdkRequestTimeout() {
      return this.sdkRequestTimeout;
   }

   public void setSdkRequestTimeout(int sdkRequestTimeout) {
      this.sdkRequestTimeout = sdkRequestTimeout;
   }

   public <T extends AmazonWebServiceRequest> T withSdkRequestTimeout(int sdkRequestTimeout) {
      this.setSdkRequestTimeout(sdkRequestTimeout);
      return (T)this;
   }

   public Integer getSdkClientExecutionTimeout() {
      return this.sdkClientExecutionTimeout;
   }

   public void setSdkClientExecutionTimeout(int sdkClientExecutionTimeout) {
      this.sdkClientExecutionTimeout = sdkClientExecutionTimeout;
   }

   public <T extends AmazonWebServiceRequest> T withSdkClientExecutionTimeout(int sdkClientExecutionTimeout) {
      this.setSdkClientExecutionTimeout(sdkClientExecutionTimeout);
      return (T)this;
   }

   @Override
   public <X> void addHandlerContext(HandlerContextKey<X> key, X value) {
      this.handlerContext.put(key, value);
   }

   @Override
   public <X> X getHandlerContext(HandlerContextKey<X> key) {
      return (X)this.handlerContext.get(key);
   }

   @SdkInternalApi
   Map<HandlerContextKey<?>, Object> getHandlerContext() {
      return Collections.unmodifiableMap(this.handlerContext);
   }

   public AmazonWebServiceRequest clone() {
      try {
         AmazonWebServiceRequest cloned = (AmazonWebServiceRequest)super.clone();
         cloned.setCloneSource(this);
         cloned.handlerContext = new HashMap<>(cloned.handlerContext);
         return cloned;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
