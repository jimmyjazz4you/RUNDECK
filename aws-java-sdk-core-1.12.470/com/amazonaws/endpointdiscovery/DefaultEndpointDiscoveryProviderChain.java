package com.amazonaws.endpointdiscovery;

public class DefaultEndpointDiscoveryProviderChain extends EndpointDiscoveryProviderChain {
   public DefaultEndpointDiscoveryProviderChain() {
      super(new EnvironmentVariableEndpointDiscoveryProvider(), new SystemPropertyEndpointDiscoveryProvider(), new AwsProfileEndpointDiscoveryProvider());
   }
}
