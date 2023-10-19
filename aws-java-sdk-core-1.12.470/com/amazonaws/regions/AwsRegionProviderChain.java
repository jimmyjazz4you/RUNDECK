package com.amazonaws.regions;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AwsRegionProviderChain extends AwsRegionProvider {
   private static final Log LOG = LogFactory.getLog(AWSCredentialsProviderChain.class);
   private final List<AwsRegionProvider> providers;

   public AwsRegionProviderChain(AwsRegionProvider... providers) {
      this.providers = new ArrayList<>(providers.length);
      Collections.addAll(this.providers, providers);
   }

   @Override
   public String getRegion() throws SdkClientException {
      for(AwsRegionProvider provider : this.providers) {
         try {
            String region = provider.getRegion();
            if (region != null) {
               return region;
            }
         } catch (Exception var4) {
            LOG.debug("Unable to load region from " + provider.toString() + ": " + var4.getMessage());
         }
      }

      throw new SdkClientException("Unable to load region information from any provider in the chain");
   }
}
