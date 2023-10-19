package com.amazonaws;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EndpointPrefixAwareSigner;
import com.amazonaws.auth.RegionAwareSigner;
import com.amazonaws.auth.RegionFromEndpointResolverAwareSigner;
import com.amazonaws.auth.Signer;
import com.amazonaws.auth.SignerFactory;
import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.internal.DefaultServiceEndpointBuilder;
import com.amazonaws.internal.auth.DefaultSignerProvider;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.internal.auth.SignerProviderContext;
import com.amazonaws.log.CommonsLogFactory;
import com.amazonaws.log.InternalLogFactory;
import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.monitoring.CsmConfiguration;
import com.amazonaws.monitoring.CsmConfigurationProvider;
import com.amazonaws.monitoring.DefaultCsmConfigurationProviderChain;
import com.amazonaws.monitoring.MonitoringListener;
import com.amazonaws.monitoring.internal.AgentMonitoringListener;
import com.amazonaws.monitoring.internal.ClientSideMonitoringRequestHandler;
import com.amazonaws.regions.EndpointToRegion;
import com.amazonaws.regions.MetadataSupportedRegionFromEndpointProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.util.AWSRequestMetrics;
import com.amazonaws.util.Classes;
import com.amazonaws.util.RuntimeHttpUtils;
import com.amazonaws.util.StringUtils;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AmazonWebServiceClient {
   @Deprecated
   public static final boolean LOGGING_AWS_REQUEST_METRIC = true;
   private static final String AMAZON = "Amazon";
   private static final String AWS = "AWS";
   private static final String DEFAULT_CLIENT_ID = "";
   private static final Log log = LogFactory.getLog(AmazonWebServiceClient.class);
   private volatile boolean isImmutable = false;
   protected volatile URI endpoint;
   protected volatile boolean isEndpointOverridden = false;
   private volatile String signerRegionOverride;
   protected ClientConfiguration clientConfiguration;
   protected AmazonHttpClient client;
   protected final List<RequestHandler2> requestHandler2s;
   protected int timeOffset;
   private volatile SignerProvider signerProvider;
   private final CsmConfiguration csmConfiguration;
   private volatile String serviceName;
   private volatile String endpointPrefix;
   private volatile String signingRegion;
   private Collection<MonitoringListener> monitoringListeners;
   private AgentMonitoringListener agentMonitoringListener;

   public AmazonWebServiceClient(ClientConfiguration clientConfiguration) {
      this(clientConfiguration, null);
   }

   public AmazonWebServiceClient(ClientConfiguration clientConfiguration, RequestMetricCollector requestMetricCollector) {
      this(clientConfiguration, requestMetricCollector, false);
   }

   @SdkProtectedApi
   protected AmazonWebServiceClient(
      final ClientConfiguration clientConfiguration, final RequestMetricCollector requestMetricCollector, boolean disableStrictHostNameVerification
   ) {
      this(new AwsSyncClientParams() {
         @Override
         public AWSCredentialsProvider getCredentialsProvider() {
            return null;
         }

         @Override
         public ClientConfiguration getClientConfiguration() {
            return clientConfiguration;
         }

         @Override
         public RequestMetricCollector getRequestMetricCollector() {
            return requestMetricCollector;
         }

         @Override
         public List<RequestHandler2> getRequestHandlers() {
            return new CopyOnWriteArrayList<>();
         }

         @Override
         public CsmConfigurationProvider getClientSideMonitoringConfigurationProvider() {
            return DefaultCsmConfigurationProviderChain.getInstance();
         }

         @Override
         public MonitoringListener getMonitoringListener() {
            return null;
         }
      }, !disableStrictHostNameVerification);
   }

   protected AmazonWebServiceClient(AwsSyncClientParams clientParams) {
      this(clientParams, null);
   }

   private AmazonWebServiceClient(AwsSyncClientParams clientParams, Boolean useStrictHostNameVerification) {
      this.clientConfiguration = clientParams.getClientConfiguration();
      this.requestHandler2s = clientParams.getRequestHandlers();
      this.monitoringListeners = new CopyOnWriteArrayList<>();
      useStrictHostNameVerification = useStrictHostNameVerification != null ? useStrictHostNameVerification : this.useStrictHostNameVerification();
      this.client = new AmazonHttpClient(
         this.clientConfiguration, clientParams.getRequestMetricCollector(), !useStrictHostNameVerification, this.calculateCRC32FromCompressedData()
      );
      this.csmConfiguration = this.getCsmConfiguration(clientParams.getClientSideMonitoringConfigurationProvider());
      if (this.isCsmEnabled()) {
         this.agentMonitoringListener = new AgentMonitoringListener(this.csmConfiguration.getHost(), this.csmConfiguration.getPort());
         this.monitoringListeners.add(this.agentMonitoringListener);
      }

      if (clientParams.getMonitoringListener() != null) {
         this.monitoringListeners.add(clientParams.getMonitoringListener());
      }

      if (this.shouldGenerateClientSideMonitoringEvents()) {
         this.requestHandler2s.add(new ClientSideMonitoringRequestHandler(this.getClientId(), this.monitoringListeners));
      }
   }

   @Deprecated
   protected Signer getSigner() {
      return this.signerProvider.getSigner(SignerProviderContext.builder().build());
   }

   @SdkProtectedApi
   protected boolean isEndpointOverridden() {
      return this.isEndpointOverridden;
   }

   @SdkProtectedApi
   protected SignerProvider getSignerProvider() {
      return this.signerProvider;
   }

   @Deprecated
   public void setEndpoint(String endpoint) throws IllegalArgumentException {
      this.checkMutability();
      URI uri = this.toURI(endpoint);
      Signer signer = this.computeSignerByURI(uri, this.signerRegionOverride, false);
      synchronized(this) {
         this.isEndpointOverridden = true;
         this.endpoint = uri;
         this.signerProvider = this.createSignerProvider(signer);
         this.signingRegion = EndpointToRegion.guessRegionNameForEndpoint(endpoint, this.getEndpointPrefix());
      }
   }

   private URI toURI(String endpoint) throws IllegalArgumentException {
      return RuntimeHttpUtils.toUri(endpoint, this.clientConfiguration);
   }

   @Deprecated
   public void setEndpoint(String endpoint, String serviceName, String regionId) {
      URI uri = this.toURI(endpoint);
      Signer signer = this.computeSignerByServiceRegion(serviceName, regionId, regionId, true);
      synchronized(this) {
         this.setServiceNameIntern(serviceName);
         this.signerProvider = this.createSignerProvider(signer);
         this.isEndpointOverridden = true;
         this.endpoint = uri;
         this.signerRegionOverride = regionId;
         this.signingRegion = regionId;
      }
   }

   public Signer getSignerByURI(URI uri) {
      return this.computeSignerByURI(uri, this.signerRegionOverride, true);
   }

   private Signer computeSignerByURI(URI uri, String signerRegionOverride, boolean isRegionIdAsSignerParam) {
      if (uri == null) {
         throw new IllegalArgumentException("Endpoint is not set. Use setEndpoint to set an endpoint before performing any request.");
      } else if (uri.getHost() == null) {
         throw new IllegalArgumentException("Endpoint does not contain a valid host name: " + uri);
      } else {
         String service = this.getServiceNameIntern();
         String region = EndpointToRegion.guessRegionNameForEndpointWithDefault(uri.getHost(), this.getEndpointPrefix(), "us-east-1");
         return this.computeSignerByServiceRegion(service, region, signerRegionOverride, isRegionIdAsSignerParam);
      }
   }

   private Signer computeSignerByServiceRegion(String serviceName, String regionId, String signerRegionOverride, boolean isRegionIdAsSignerParam) {
      String signerType = this.clientConfiguration.getSignerOverride();
      Signer signer = signerType == null ? SignerFactory.getSigner(serviceName, regionId) : SignerFactory.getSignerByTypeAndService(signerType, serviceName);
      if (signer instanceof RegionAwareSigner) {
         RegionAwareSigner regionAwareSigner = (RegionAwareSigner)signer;
         if (signerRegionOverride != null) {
            regionAwareSigner.setRegionName(signerRegionOverride);
         } else if (regionId != null && isRegionIdAsSignerParam) {
            regionAwareSigner.setRegionName(regionId);
         }
      }

      if (signer instanceof EndpointPrefixAwareSigner) {
         EndpointPrefixAwareSigner endpointPrefixAwareSigner = (EndpointPrefixAwareSigner)signer;
         endpointPrefixAwareSigner.setEndpointPrefix(this.endpointPrefix);
      }

      if (signer instanceof RegionFromEndpointResolverAwareSigner) {
         RegionFromEndpointResolverAwareSigner awareSigner = (RegionFromEndpointResolverAwareSigner)signer;
         awareSigner.setRegionFromEndpointResolver(new MetadataSupportedRegionFromEndpointProvider());
      }

      return signer;
   }

   @Deprecated
   public void setRegion(Region region) throws IllegalArgumentException {
      this.checkMutability();
      if (region == null) {
         throw new IllegalArgumentException("No region provided");
      } else {
         String serviceNameForEndpoint = this.getEndpointPrefix();
         String serviceNameForSigner = this.getServiceNameIntern();
         URI uri = new DefaultServiceEndpointBuilder(serviceNameForEndpoint, this.clientConfiguration.getProtocol().toString())
            .withRegion(region)
            .getServiceEndpoint();
         Signer signer = this.computeSignerByServiceRegion(serviceNameForSigner, region.getName(), this.signerRegionOverride, false);
         synchronized(this) {
            this.isEndpointOverridden = false;
            this.endpoint = uri;
            this.signerProvider = this.createSignerProvider(signer);
            this.signingRegion = EndpointToRegion.guessRegionNameForEndpoint(this.endpoint.toString(), this.getEndpointPrefix());
         }
      }
   }

   @Deprecated
   public final void configureRegion(Regions region) {
      this.checkMutability();
      if (region == null) {
         throw new IllegalArgumentException("No region provided");
      } else {
         this.setRegion(Region.getRegion(region));
      }
   }

   public void shutdown() {
      if (this.agentMonitoringListener != null) {
         this.agentMonitoringListener.shutdown();
      }

      this.client.shutdown();
   }

   @Deprecated
   public void addRequestHandler(RequestHandler requestHandler) {
      this.checkMutability();
      this.requestHandler2s.add(RequestHandler2.adapt(requestHandler));
   }

   @Deprecated
   public void addRequestHandler(RequestHandler2 requestHandler2) {
      this.checkMutability();
      this.requestHandler2s.add(requestHandler2);
   }

   @Deprecated
   public void removeRequestHandler(RequestHandler requestHandler) {
      this.checkMutability();
      this.requestHandler2s.remove(RequestHandler2.adapt(requestHandler));
   }

   @Deprecated
   public void removeRequestHandler(RequestHandler2 requestHandler2) {
      this.checkMutability();
      this.requestHandler2s.remove(requestHandler2);
   }

   protected final <T extends AmazonWebServiceRequest> T beforeMarshalling(T request) {
      T local = request;

      for(RequestHandler2 handler : this.requestHandler2s) {
         local = handler.beforeMarshalling(local);
      }

      return local;
   }

   protected ExecutionContext createExecutionContext(AmazonWebServiceRequest req) {
      return this.createExecutionContext(req, this.signerProvider);
   }

   protected ExecutionContext createExecutionContext(AmazonWebServiceRequest req, SignerProvider signerProvider) {
      boolean isMetricsEnabled = this.isRequestMetricsEnabled(req) || isProfilingEnabled() || this.shouldGenerateClientSideMonitoringEvents();
      return ExecutionContext.builder()
         .withRequestHandler2s(this.requestHandler2s)
         .withUseRequestMetrics(isMetricsEnabled)
         .withAwsClient(this)
         .withSignerProvider(signerProvider)
         .build();
   }

   protected final ExecutionContext createExecutionContext(Request<?> req) {
      return this.createExecutionContext(req.getOriginalRequest());
   }

   protected SignerProvider createSignerProvider(Signer signer) {
      return new DefaultSignerProvider(this, signer);
   }

   protected static boolean isProfilingEnabled() {
      return System.getProperty("com.amazonaws.sdk.enableRuntimeProfiling") != null;
   }

   protected boolean shouldGenerateClientSideMonitoringEvents() {
      return !this.monitoringListeners.isEmpty();
   }

   protected final boolean isRequestMetricsEnabled(AmazonWebServiceRequest req) {
      RequestMetricCollector c = req.getRequestMetricCollector();
      return c != null && c.isEnabled() ? true : this.isRMCEnabledAtClientOrSdkLevel();
   }

   private boolean isRMCEnabledAtClientOrSdkLevel() {
      RequestMetricCollector c = this.requestMetricCollector();
      return c != null && c.isEnabled();
   }

   public void setTimeOffset(int timeOffset) {
      this.checkMutability();
      this.timeOffset = timeOffset;
   }

   public AmazonWebServiceClient withTimeOffset(int timeOffset) {
      this.checkMutability();
      this.setTimeOffset(timeOffset);
      return this;
   }

   public int getTimeOffset() {
      return this.timeOffset;
   }

   public RequestMetricCollector getRequestMetricsCollector() {
      return this.client.getRequestMetricCollector();
   }

   public Collection<MonitoringListener> getMonitoringListeners() {
      return Collections.unmodifiableCollection(this.monitoringListeners);
   }

   protected RequestMetricCollector requestMetricCollector() {
      RequestMetricCollector mc = this.client.getRequestMetricCollector();
      return mc == null ? AwsSdkMetrics.getRequestMetricCollector() : mc;
   }

   private final RequestMetricCollector findRequestMetricCollector(RequestMetricCollector reqLevelMetricsCollector) {
      RequestMetricCollector requestMetricCollector;
      if (reqLevelMetricsCollector != null) {
         requestMetricCollector = reqLevelMetricsCollector;
      } else if (this.getRequestMetricsCollector() != null) {
         requestMetricCollector = this.getRequestMetricsCollector();
      } else {
         requestMetricCollector = AwsSdkMetrics.getRequestMetricCollector();
      }

      return requestMetricCollector;
   }

   protected final <T extends AmazonWebServiceRequest> T beforeClientExecution(T request) {
      T local = request;

      for(RequestHandler2 handler : this.requestHandler2s) {
         local = handler.beforeExecution(local);
      }

      return local;
   }

   protected final void endClientExecution(AWSRequestMetrics awsRequestMetrics, Request<?> request, Response<?> response) {
      this.endClientExecution(awsRequestMetrics, request, response, false);
   }

   protected final void endClientExecution(
      AWSRequestMetrics awsRequestMetrics, Request<?> request, Response<?> response, @Deprecated boolean loggingAwsRequestMetrics
   ) {
      if (request != null) {
         awsRequestMetrics.endEvent(AWSRequestMetrics.Field.ClientExecuteTime);
         awsRequestMetrics.getTimingInfo().endTiming();
         RequestMetricCollector c = this.findRequestMetricCollector(request.getOriginalRequest().getRequestMetricCollector());
         c.collectMetrics(request, response);
         awsRequestMetrics.log();
      }
   }

   @Deprecated
   protected String getServiceAbbreviation() {
      return this.getServiceNameIntern();
   }

   public String getServiceName() {
      return this.getServiceNameIntern();
   }

   public String getEndpointPrefix() {
      if (this.endpointPrefix != null) {
         return this.endpointPrefix;
      } else {
         String httpClientName = this.getHttpClientName();
         String serviceNameInRegionMetadata = ServiceNameFactory.getServiceNameInRegionMetadata(httpClientName);
         synchronized(this) {
            if (this.endpointPrefix != null) {
               return this.endpointPrefix;
            } else {
               return serviceNameInRegionMetadata != null
                  ? (this.endpointPrefix = serviceNameInRegionMetadata)
                  : (this.endpointPrefix = this.getServiceNameIntern());
            }
         }
      }
   }

   @SdkProtectedApi
   protected String getSigningRegion() {
      return this.signingRegion;
   }

   protected void setEndpointPrefix(String endpointPrefix) {
      if (endpointPrefix == null) {
         throw new IllegalArgumentException("The parameter endpointPrefix must be specified!");
      } else {
         this.endpointPrefix = endpointPrefix;
      }
   }

   protected String getServiceNameIntern() {
      if (this.serviceName == null) {
         synchronized(this) {
            if (this.serviceName == null) {
               return this.serviceName = this.computeServiceName();
            }
         }
      }

      return this.serviceName;
   }

   public final void setServiceNameIntern(String serviceName) {
      if (serviceName == null) {
         throw new IllegalArgumentException("The parameter serviceName must be specified!");
      } else {
         this.serviceName = serviceName;
      }
   }

   private String computeServiceName() {
      String httpClientName = this.getHttpClientName();
      String service = ServiceNameFactory.getServiceName(httpClientName);
      if (service != null) {
         return service;
      } else {
         int j = httpClientName.indexOf("JavaClient");
         if (j == -1) {
            j = httpClientName.indexOf("Client");
            if (j == -1) {
               throw new IllegalStateException("Unrecognized suffix for the AWS http client class name " + httpClientName);
            }
         }

         int i = httpClientName.indexOf("Amazon");
         int len;
         if (i == -1) {
            i = httpClientName.indexOf("AWS");
            if (i == -1) {
               throw new IllegalStateException("Unrecognized prefix for the AWS http client class name " + httpClientName);
            }

            len = "AWS".length();
         } else {
            len = "Amazon".length();
         }

         if (i >= j) {
            throw new IllegalStateException("Unrecognized AWS http client class name " + httpClientName);
         } else {
            String serviceName = httpClientName.substring(i + len, j);
            return StringUtils.lowerCase(serviceName);
         }
      }
   }

   private String getHttpClientName() {
      Class<?> httpClientClass = Classes.childClassOf(AmazonWebServiceClient.class, this);
      return httpClientClass.getSimpleName();
   }

   public final String getSignerRegionOverride() {
      return this.signerRegionOverride;
   }

   public final void setSignerRegionOverride(String signerRegionOverride) {
      this.checkMutability();
      Signer signer = this.computeSignerByURI(this.endpoint, signerRegionOverride, true);
      synchronized(this) {
         this.signerRegionOverride = signerRegionOverride;
         this.signerProvider = this.createSignerProvider(signer);
         this.signingRegion = signerRegionOverride;
      }
   }

   @Deprecated
   public <T extends AmazonWebServiceClient> T withRegion(Region region) {
      this.setRegion(region);
      return (T)this;
   }

   @Deprecated
   public <T extends AmazonWebServiceClient> T withRegion(Regions region) {
      this.configureRegion(region);
      return (T)this;
   }

   @Deprecated
   public <T extends AmazonWebServiceClient> T withEndpoint(String endpoint) {
      this.setEndpoint(endpoint);
      return (T)this;
   }

   @Deprecated
   @SdkInternalApi
   public final void makeImmutable() {
      this.isImmutable = true;
   }

   @SdkProtectedApi
   protected final void checkMutability() {
      if (this.isImmutable) {
         throw new UnsupportedOperationException("Client is immutable when created with the builder.");
      }
   }

   protected boolean useStrictHostNameVerification() {
      return true;
   }

   protected boolean calculateCRC32FromCompressedData() {
      return false;
   }

   public String getSignerOverride() {
      return this.clientConfiguration.getSignerOverride();
   }

   public ClientConfiguration getClientConfiguration() {
      return new ClientConfiguration(this.clientConfiguration);
   }

   protected final boolean isCsmEnabled() {
      return this.csmConfiguration != null && this.csmConfiguration.isEnabled();
   }

   protected String getClientId() {
      return this.csmConfiguration == null ? "" : this.csmConfiguration.getClientId();
   }

   private CsmConfiguration getCsmConfiguration(CsmConfigurationProvider csmConfigurationProvider) {
      try {
         return csmConfigurationProvider.getConfiguration();
      } catch (SdkClientException var3) {
         return null;
      }
   }

   static {
      boolean success = InternalLogFactory.configureFactory(new CommonsLogFactory());
      if (log.isDebugEnabled()) {
         log.debug("Internal logging successfully configured to commons logger: " + success);
      }
   }
}
