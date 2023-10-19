package com.amazonaws;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.internal.AmazonWebServiceRequestAdapter;
import com.amazonaws.metrics.RequestMetricCollector;
import java.util.List;
import java.util.Map;

@SdkProtectedApi
public abstract class RequestConfig {
   public static final RequestConfig NO_OP = new AmazonWebServiceRequestAdapter(AmazonWebServiceRequest.NOOP);

   public abstract ProgressListener getProgressListener();

   public abstract RequestMetricCollector getRequestMetricsCollector();

   public abstract AWSCredentialsProvider getCredentialsProvider();

   public abstract Map<String, String> getCustomRequestHeaders();

   public abstract Map<String, List<String>> getCustomQueryParameters();

   public abstract Integer getRequestTimeout();

   public abstract Integer getClientExecutionTimeout();

   public abstract RequestClientOptions getRequestClientOptions();

   public abstract String getRequestType();

   public abstract Object getOriginalRequest();
}
