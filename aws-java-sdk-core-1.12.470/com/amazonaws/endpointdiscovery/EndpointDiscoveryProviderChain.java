package com.amazonaws.endpointdiscovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EndpointDiscoveryProviderChain implements EndpointDiscoveryProvider {
   private static final Log LOG = LogFactory.getLog(EndpointDiscoveryProviderChain.class);
   private final List<EndpointDiscoveryProvider> providers;

   public EndpointDiscoveryProviderChain(EndpointDiscoveryProvider... providers) {
      this.providers = new ArrayList<>(providers.length);
      Collections.addAll(this.providers, providers);
   }

   @Override
   public Boolean endpointDiscoveryEnabled() {
      Boolean endpointDiscoveryEnabled = null;

      for(EndpointDiscoveryProvider provider : this.providers) {
         try {
            endpointDiscoveryEnabled = provider.endpointDiscoveryEnabled();
            if (endpointDiscoveryEnabled != null) {
               return endpointDiscoveryEnabled;
            }
         } catch (Exception var5) {
            LOG.debug("Unable to discover endpoint discovery setting " + provider.toString() + ": " + var5.getMessage());
         }
      }

      return endpointDiscoveryEnabled;
   }
}
