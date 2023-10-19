package com.amazonaws.internal;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.RequestConfig;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SdkInternalApi
public final class AmazonWebServiceRequestAdapter extends RequestConfig {
   private final AmazonWebServiceRequest request;

   public AmazonWebServiceRequestAdapter(AmazonWebServiceRequest request) {
      this.request = request;
   }

   @Override
   public ProgressListener getProgressListener() {
      return this.request.getGeneralProgressListener();
   }

   @Override
   public RequestMetricCollector getRequestMetricsCollector() {
      return this.request.getRequestMetricCollector();
   }

   @Override
   public AWSCredentialsProvider getCredentialsProvider() {
      return this.request.getRequestCredentialsProvider();
   }

   @Override
   public Map<String, String> getCustomRequestHeaders() {
      return this.request.getCustomRequestHeaders() == null ? Collections.emptyMap() : this.request.getCustomRequestHeaders();
   }

   @Override
   public Map<String, List<String>> getCustomQueryParameters() {
      return this.request.getCustomQueryParameters() == null ? Collections.emptyMap() : this.request.getCustomQueryParameters();
   }

   @Override
   public Integer getRequestTimeout() {
      return this.request.getSdkRequestTimeout();
   }

   @Override
   public Integer getClientExecutionTimeout() {
      return this.request.getSdkClientExecutionTimeout();
   }

   @Override
   public RequestClientOptions getRequestClientOptions() {
      return this.request.getRequestClientOptions();
   }

   @Override
   public String getRequestType() {
      return this.request.getClass().getSimpleName();
   }

   @Override
   public Object getOriginalRequest() {
      return this.request;
   }
}
