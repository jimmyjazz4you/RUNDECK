package com.amazonaws.regions;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.AwsHostNameUtils;
import java.net.URI;

@SdkProtectedApi
public class EndpointToRegion {
   public static String guessRegionNameForEndpoint(String hostname) {
      return guessRegionNameForEndpoint(hostname, null);
   }

   public static String guessRegionNameForEndpoint(String endpoint, String serviceHint) {
      return guessRegionOrRegionNameForEndpoint(endpoint, serviceHint).getRegionName();
   }

   public static String guessRegionNameForEndpointWithDefault(String hostname, String serviceHint, String defaultRegion) {
      String region = guessRegionNameForEndpoint(hostname, serviceHint);
      return region != null ? region : defaultRegion;
   }

   public static Region guessRegionForEndpoint(String hostname) {
      return guessRegionForEndpoint(hostname, null);
   }

   public static Region guessRegionForEndpoint(String endpoint, String serviceHint) {
      return guessRegionOrRegionNameForEndpoint(endpoint, serviceHint).getRegion();
   }

   private static EndpointToRegion.RegionOrRegionName guessRegionOrRegionNameForEndpoint(String endpoint, String serviceHint) {
      if (endpoint == null) {
         return new EndpointToRegion.RegionOrRegionName();
      } else {
         String host = null;

         try {
            host = URI.create(endpoint).getHost();
         } catch (Exception var10) {
         }

         if (host == null) {
            host = URI.create("http://" + endpoint).getHost();
         }

         if (host == null) {
            return new EndpointToRegion.RegionOrRegionName();
         } else {
            String regionFromInternalConfig = AwsHostNameUtils.parseRegionFromInternalConfig(host);
            if (regionFromInternalConfig != null) {
               return new EndpointToRegion.RegionOrRegionName(regionFromInternalConfig);
            } else {
               RegionMetadata regionMetadata = RegionUtils.getRegionMetadata();
               Region regionByExplicitEndpoint = regionMetadata.tryGetRegionByExplicitEndpoint(host);
               if (regionByExplicitEndpoint != null) {
                  return new EndpointToRegion.RegionOrRegionName(regionByExplicitEndpoint);
               } else {
                  String regionFromAwsPartitionPattern = AwsHostNameUtils.parseRegionFromAwsPartitionPattern(host);
                  if (regionFromAwsPartitionPattern != null) {
                     return new EndpointToRegion.RegionOrRegionName(regionFromAwsPartitionPattern);
                  } else {
                     String serviceHintRegion = AwsHostNameUtils.parseRegionUsingServiceHint(host, serviceHint);
                     if (serviceHintRegion != null) {
                        return new EndpointToRegion.RegionOrRegionName(serviceHintRegion);
                     } else {
                        Region regionByDnsSuffix = regionMetadata.tryGetRegionByEndpointDnsSuffix(host);
                        if (regionByDnsSuffix != null) {
                           return new EndpointToRegion.RegionOrRegionName(regionByDnsSuffix);
                        } else {
                           String regionFromAfterServiceName = AwsHostNameUtils.parseRegionFromAfterServiceName(host, serviceHint);
                           return regionFromAfterServiceName != null
                              ? new EndpointToRegion.RegionOrRegionName(regionFromAfterServiceName)
                              : new EndpointToRegion.RegionOrRegionName();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static class RegionOrRegionName {
      private final Region region;
      private final String regionName;

      private RegionOrRegionName(Region region) {
         this.region = region;
         this.regionName = null;
      }

      private RegionOrRegionName(String regionName) {
         this.region = null;
         this.regionName = regionName;
      }

      private RegionOrRegionName() {
         this.region = null;
         this.regionName = null;
      }

      public Region getRegion() {
         return this.regionName != null ? RegionUtils.getRegion(this.regionName) : this.region;
      }

      public String getRegionName() {
         return this.region != null ? this.region.getName() : this.regionName;
      }
   }
}
