package com.amazonaws.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AdvancedConfig;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.monitoring.CsmConfigurationProvider;
import com.amazonaws.monitoring.MonitoringListener;
import com.amazonaws.retry.RetryPolicyAdapter;
import com.amazonaws.retry.v2.RetryPolicy;
import java.net.URI;
import java.util.List;

@SdkProtectedApi
public abstract class AwsSyncClientParams {
   public abstract AWSCredentialsProvider getCredentialsProvider();

   public abstract ClientConfiguration getClientConfiguration();

   public abstract RequestMetricCollector getRequestMetricCollector();

   public abstract List<RequestHandler2> getRequestHandlers();

   public abstract CsmConfigurationProvider getClientSideMonitoringConfigurationProvider();

   public abstract MonitoringListener getMonitoringListener();

   public AdvancedConfig getAdvancedConfig() {
      return AdvancedConfig.EMPTY;
   }

   public SignerProvider getSignerProvider() {
      return null;
   }

   public URI getEndpoint() {
      return null;
   }

   public RetryPolicy getRetryPolicy() {
      ClientConfiguration config = this.getClientConfiguration();
      return new RetryPolicyAdapter(config.getRetryPolicy(), config);
   }
}
