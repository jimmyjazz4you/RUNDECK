package com.amazonaws.internal.config;

public class InternalConfigJsonHelper {
   private SignerConfigJsonHelper defaultSigner;
   private JsonIndex<SignerConfigJsonHelper, SignerConfig>[] serviceSigners;
   private JsonIndex<SignerConfigJsonHelper, SignerConfig>[] regionSigners;
   private JsonIndex<SignerConfigJsonHelper, SignerConfig>[] serviceRegionSigners;
   private JsonIndex<HttpClientConfigJsonHelper, HttpClientConfig>[] httpClients;
   private HostRegexToRegionMappingJsonHelper[] hostRegexToRegionMappings;
   private String userAgentTemplate;
   private boolean endpointDiscoveryEnabled;
   private String defaultRetryMode;

   public SignerConfigJsonHelper getDefaultSigner() {
      return this.defaultSigner;
   }

   public void setDefaultSigner(SignerConfigJsonHelper defaultSigner) {
      this.defaultSigner = defaultSigner;
   }

   public JsonIndex<SignerConfigJsonHelper, SignerConfig>[] getServiceSigners() {
      return this.serviceSigners;
   }

   public void setServiceSigners(JsonIndex<SignerConfigJsonHelper, SignerConfig>... serviceSigners) {
      this.serviceSigners = serviceSigners;
   }

   public JsonIndex<SignerConfigJsonHelper, SignerConfig>[] getRegionSigners() {
      return this.regionSigners;
   }

   public void setRegionSigners(JsonIndex<SignerConfigJsonHelper, SignerConfig>... regionSigners) {
      this.regionSigners = regionSigners;
   }

   public JsonIndex<SignerConfigJsonHelper, SignerConfig>[] getServiceRegionSigners() {
      return this.serviceRegionSigners;
   }

   public void setServiceRegionSigners(JsonIndex<SignerConfigJsonHelper, SignerConfig>... serviceRegionSigners) {
      this.serviceRegionSigners = serviceRegionSigners;
   }

   public JsonIndex<HttpClientConfigJsonHelper, HttpClientConfig>[] getHttpClients() {
      return this.httpClients;
   }

   public void setHttpClients(JsonIndex<HttpClientConfigJsonHelper, HttpClientConfig>... httpClients) {
      this.httpClients = httpClients;
   }

   public HostRegexToRegionMappingJsonHelper[] getHostRegexToRegionMappings() {
      return this.hostRegexToRegionMappings;
   }

   public void setHostRegexToRegionMappings(HostRegexToRegionMappingJsonHelper[] hostRegexToRegionMappings) {
      this.hostRegexToRegionMappings = hostRegexToRegionMappings;
   }

   public String getUserAgentTemplate() {
      return this.userAgentTemplate;
   }

   public void setUserAgentTemplate(String userAgentTemplate) {
      this.userAgentTemplate = userAgentTemplate;
   }

   public boolean isEndpointDiscoveryEnabled() {
      return this.endpointDiscoveryEnabled;
   }

   public void setEndpointDiscoveryEnabled(boolean endpointDiscoveryEnabled) {
      this.endpointDiscoveryEnabled = endpointDiscoveryEnabled;
   }

   public String getDefaultRetryMode() {
      return this.defaultRetryMode;
   }

   public void setDefaultRetryMode(String defaultRetryMode) {
      this.defaultRetryMode = defaultRetryMode;
   }
}
