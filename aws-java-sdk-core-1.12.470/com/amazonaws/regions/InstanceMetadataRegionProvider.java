package com.amazonaws.regions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.util.EC2MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InstanceMetadataRegionProvider extends AwsRegionProvider {
   private static final Log LOG = LogFactory.getLog(InstanceMetadataRegionProvider.class);
   private volatile String region;

   @Override
   public String getRegion() {
      if (SDKGlobalConfiguration.isEc2MetadataDisabled()) {
         throw new AmazonClientException("AWS_EC2_METADATA_DISABLED is set to true, not loading region from EC2 Instance Metadata service");
      } else {
         if (this.region == null) {
            synchronized(this) {
               if (this.region == null) {
                  this.region = this.tryDetectRegion();
               }
            }
         }

         return this.region;
      }
   }

   private String tryDetectRegion() {
      try {
         return EC2MetadataUtils.getEC2InstanceRegion();
      } catch (AmazonClientException var2) {
         LOG.debug("Ignoring failure to retrieve the region: " + var2.getMessage());
         return null;
      }
   }
}
