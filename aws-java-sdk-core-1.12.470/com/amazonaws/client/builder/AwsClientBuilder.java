package com.amazonaws.client.builder;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.AwsAsyncClientParams;
import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.monitoring.CsmConfigurationProvider;
import com.amazonaws.monitoring.DefaultCsmConfigurationProviderChain;
import com.amazonaws.monitoring.MonitoringListener;
import com.amazonaws.regions.AwsRegionProvider;
import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

@NotThreadSafe
@SdkProtectedApi
public abstract class AwsClientBuilder<Subclass extends AwsClientBuilder, TypeToBuild> {
   private static final AwsRegionProvider DEFAULT_REGION_PROVIDER = new DefaultAwsRegionProviderChain();
   private final ClientConfigurationFactory clientConfigFactory;
   private final AwsRegionProvider regionProvider;
   private final AdvancedConfig.Builder advancedConfig = AdvancedConfig.builder();
   private AWSCredentialsProvider credentials;
   private ClientConfiguration clientConfig;
   private RequestMetricCollector metricsCollector;
   private Region region;
   private List<RequestHandler2> requestHandlers;
   private AwsClientBuilder.EndpointConfiguration endpointConfiguration;
   private CsmConfigurationProvider csmConfig;
   private MonitoringListener monitoringListener;

   protected AwsClientBuilder(ClientConfigurationFactory clientConfigFactory) {
      this(clientConfigFactory, DEFAULT_REGION_PROVIDER);
   }

   @SdkTestInternalApi
   protected AwsClientBuilder(ClientConfigurationFactory clientConfigFactory, AwsRegionProvider regionProvider) {
      this.clientConfigFactory = clientConfigFactory;
      this.regionProvider = regionProvider;
   }

   public final AWSCredentialsProvider getCredentials() {
      return this.credentials;
   }

   public final void setCredentials(AWSCredentialsProvider credentialsProvider) {
      this.credentials = credentialsProvider;
   }

   public final Subclass withCredentials(AWSCredentialsProvider credentialsProvider) {
      this.setCredentials(credentialsProvider);
      return this.getSubclass();
   }

   private AWSCredentialsProvider resolveCredentials() {
      return (AWSCredentialsProvider)(this.credentials == null ? DefaultAWSCredentialsProviderChain.getInstance() : this.credentials);
   }

   public final ClientConfiguration getClientConfiguration() {
      return this.clientConfig;
   }

   public final void setClientConfiguration(ClientConfiguration config) {
      this.clientConfig = config;
   }

   public final Subclass withClientConfiguration(ClientConfiguration config) {
      this.setClientConfiguration(config);
      return this.getSubclass();
   }

   private ClientConfiguration resolveClientConfiguration() {
      return this.clientConfig == null ? this.clientConfigFactory.getConfig() : new ClientConfiguration(this.clientConfig);
   }

   public final RequestMetricCollector getMetricsCollector() {
      return this.metricsCollector;
   }

   public final void setMetricsCollector(RequestMetricCollector metrics) {
      this.metricsCollector = metrics;
   }

   public final Subclass withMetricsCollector(RequestMetricCollector metrics) {
      this.setMetricsCollector(metrics);
      return this.getSubclass();
   }

   public final String getRegion() {
      return this.region == null ? null : this.region.getName();
   }

   public final void setRegion(String region) {
      this.withRegion(region);
   }

   public final Subclass withRegion(Regions region) {
      return this.withRegion(region.getName());
   }

   public final Subclass withRegion(String region) {
      return this.withRegion(this.getRegionObject(region));
   }

   private Region getRegionObject(String regionStr) {
      Region regionObj = RegionUtils.getRegion(regionStr);
      if (regionObj == null) {
         throw new SdkClientException(String.format("Could not find region information for '%s' in SDK metadata.", regionStr));
      } else {
         return regionObj;
      }
   }

   private Subclass withRegion(Region region) {
      this.region = region;
      return this.getSubclass();
   }

   public final AwsClientBuilder.EndpointConfiguration getEndpoint() {
      return this.endpointConfiguration;
   }

   public final void setEndpointConfiguration(AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
      this.withEndpointConfiguration(endpointConfiguration);
   }

   public final Subclass withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
      this.endpointConfiguration = endpointConfiguration;
      return this.getSubclass();
   }

   public final List<RequestHandler2> getRequestHandlers() {
      return this.requestHandlers == null ? null : Collections.unmodifiableList(this.requestHandlers);
   }

   public final void setRequestHandlers(RequestHandler2... handlers) {
      this.requestHandlers = Arrays.asList(handlers);
   }

   public final Subclass withRequestHandlers(RequestHandler2... handlers) {
      this.setRequestHandlers(handlers);
      return this.getSubclass();
   }

   public final MonitoringListener getMonitoringListener() {
      return this.monitoringListener;
   }

   public final void setMonitoringListener(MonitoringListener monitoringListener) {
      this.monitoringListener = monitoringListener;
   }

   public final Subclass withMonitoringListener(MonitoringListener monitoringListener) {
      this.setMonitoringListener(monitoringListener);
      return this.getSubclass();
   }

   private List<RequestHandler2> resolveRequestHandlers() {
      return this.requestHandlers == null ? new ArrayList<>() : new ArrayList<>(this.requestHandlers);
   }

   public CsmConfigurationProvider getClientSideMonitoringConfigurationProvider() {
      return this.csmConfig;
   }

   public void setClientSideMonitoringConfigurationProvider(CsmConfigurationProvider csmConfig) {
      this.csmConfig = csmConfig;
   }

   public Subclass withClientSideMonitoringConfigurationProvider(CsmConfigurationProvider csmConfig) {
      this.setClientSideMonitoringConfigurationProvider(csmConfig);
      return this.getSubclass();
   }

   private CsmConfigurationProvider resolveClientSideMonitoringConfig() {
      return (CsmConfigurationProvider)(this.csmConfig == null ? DefaultCsmConfigurationProviderChain.getInstance() : this.csmConfig);
   }

   protected final <T> T getAdvancedConfig(AdvancedConfig.Key<T> key) {
      return this.advancedConfig.get(key);
   }

   protected final <T> void putAdvancedConfig(AdvancedConfig.Key<T> key, T value) {
      this.advancedConfig.put(key, value);
   }

   @SdkInternalApi
   final TypeToBuild configureMutableProperties(TypeToBuild clientInterface) {
      AmazonWebServiceClient client = (AmazonWebServiceClient)clientInterface;
      this.setRegion(client);
      client.makeImmutable();
      return clientInterface;
   }

   public abstract TypeToBuild build();

   protected final AwsSyncClientParams getSyncClientParams() {
      return new AwsClientBuilder.SyncBuilderParams();
   }

   protected final AdvancedConfig getAdvancedConfig() {
      return this.advancedConfig.build();
   }

   private void setRegion(AmazonWebServiceClient client) {
      if (this.region != null && this.endpointConfiguration != null) {
         throw new IllegalStateException("Only one of Region or EndpointConfiguration may be set.");
      } else {
         if (this.endpointConfiguration != null) {
            client.setEndpoint(this.endpointConfiguration.getServiceEndpoint());
            client.setSignerRegionOverride(this.endpointConfiguration.getSigningRegion());
         } else if (this.region != null) {
            client.setRegion(this.region);
         } else {
            String region = this.determineRegionFromRegionProvider();
            if (region == null) {
               throw new SdkClientException(
                  "Unable to find a region via the region provider chain. Must provide an explicit region in the builder or setup environment to supply a region."
               );
            }

            client.setRegion(this.getRegionObject(region));
         }
      }
   }

   private String determineRegionFromRegionProvider() {
      try {
         return this.regionProvider.getRegion();
      } catch (SdkClientException var2) {
         return null;
      }
   }

   protected final Subclass getSubclass() {
      return (Subclass)this;
   }

   public static final class EndpointConfiguration {
      private final String serviceEndpoint;
      private final String signingRegion;

      public EndpointConfiguration(String serviceEndpoint, String signingRegion) {
         this.serviceEndpoint = serviceEndpoint;
         this.signingRegion = signingRegion;
      }

      public String getServiceEndpoint() {
         return this.serviceEndpoint;
      }

      public String getSigningRegion() {
         return this.signingRegion;
      }
   }

   protected class SyncBuilderParams extends AwsAsyncClientParams {
      private final ClientConfiguration _clientConfig = AwsClientBuilder.this.resolveClientConfiguration();
      private final AWSCredentialsProvider _credentials = AwsClientBuilder.this.resolveCredentials();
      private final RequestMetricCollector _metricsCollector = AwsClientBuilder.this.metricsCollector;
      private final List<RequestHandler2> _requestHandlers = AwsClientBuilder.this.resolveRequestHandlers();
      private final CsmConfigurationProvider _csmConfig = AwsClientBuilder.this.resolveClientSideMonitoringConfig();
      private final MonitoringListener _monitoringListener = AwsClientBuilder.this.monitoringListener;
      private final AdvancedConfig _advancedConfig = AwsClientBuilder.this.advancedConfig.build();

      @Override
      public AWSCredentialsProvider getCredentialsProvider() {
         return this._credentials;
      }

      @Override
      public ClientConfiguration getClientConfiguration() {
         return this._clientConfig;
      }

      @Override
      public RequestMetricCollector getRequestMetricCollector() {
         return this._metricsCollector;
      }

      @Override
      public List<RequestHandler2> getRequestHandlers() {
         return this._requestHandlers;
      }

      @Override
      public CsmConfigurationProvider getClientSideMonitoringConfigurationProvider() {
         return this._csmConfig;
      }

      @Override
      public MonitoringListener getMonitoringListener() {
         return this._monitoringListener;
      }

      @Override
      public AdvancedConfig getAdvancedConfig() {
         return this._advancedConfig;
      }

      @Override
      public ExecutorService getExecutor() {
         throw new UnsupportedOperationException("ExecutorService is not used for sync client.");
      }
   }
}
