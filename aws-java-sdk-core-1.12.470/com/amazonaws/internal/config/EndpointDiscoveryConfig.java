package com.amazonaws.internal.config;

public class EndpointDiscoveryConfig {
   private boolean endpointDiscoveryEnabled = false;

   public boolean isEndpointDiscoveryEnabled() {
      return this.endpointDiscoveryEnabled;
   }

   public void setEndpointDiscoveryEnabled(boolean endpointDiscoveryEnabled) {
      this.endpointDiscoveryEnabled = endpointDiscoveryEnabled;
   }
}
