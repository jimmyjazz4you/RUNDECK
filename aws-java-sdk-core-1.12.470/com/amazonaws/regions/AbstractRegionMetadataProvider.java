package com.amazonaws.regions;

import java.net.URI;

public abstract class AbstractRegionMetadataProvider implements RegionMetadataProvider {
   @Override
   public Region getRegionByEndpoint(String endpoint) {
      String host = getHost(endpoint);

      for(Region region : this.getRegions()) {
         for(String serviceEndpoint : region.getAvailableEndpoints()) {
            if (host.equals(getHost(serviceEndpoint))) {
               return region;
            }
         }
      }

      throw new IllegalArgumentException("No region found with any service for endpoint " + endpoint);
   }

   protected static String getHost(String endpoint) {
      try {
         String host = URI.create(endpoint).getHost();
         if (host == null) {
            host = URI.create("http://" + endpoint).getHost();
         }

         return host == null ? "" : host;
      } catch (IllegalArgumentException var2) {
         return "";
      }
   }
}
