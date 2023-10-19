package com.amazonaws.endpointdiscovery;

public class SystemPropertyEndpointDiscoveryProvider implements EndpointDiscoveryProvider {
   @Override
   public Boolean endpointDiscoveryEnabled() {
      Boolean endpointDiscoveryEnabled = null;
      String endpointDiscoveryEnabledString = System.getProperty("AWS_ENABLE_ENDPOINT_DISCOVERY");
      if (endpointDiscoveryEnabledString != null) {
         try {
            endpointDiscoveryEnabled = Boolean.parseBoolean(endpointDiscoveryEnabledString);
         } catch (Exception var4) {
            throw new RuntimeException("Unable to parse environment variable AWS_ENABLE_ENDPOINT_DISCOVERY");
         }
      }

      return endpointDiscoveryEnabled;
   }
}
