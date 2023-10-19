package com.amazonaws.monitoring;

import com.amazonaws.SdkClientException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CsmConfigurationProviderChain implements CsmConfigurationProvider {
   private static final Log log = LogFactory.getLog(CsmConfigurationProviderChain.class);
   private final List<CsmConfigurationProvider> providers = new ArrayList<>();

   public CsmConfigurationProviderChain(CsmConfigurationProvider... providers) {
      if (providers != null) {
         Collections.addAll(this.providers, providers);
      }
   }

   @Override
   public CsmConfiguration getConfiguration() {
      for(CsmConfigurationProvider p : this.providers) {
         try {
            return p.getConfiguration();
         } catch (SdkClientException var4) {
            if (log.isDebugEnabled()) {
               log.debug("Unable to load configuration from " + p.toString() + ": " + var4.getMessage());
            }
         }
      }

      throw new SdkClientException("Could not resolve client side monitoring configuration from the configured providers in the chain");
   }
}
