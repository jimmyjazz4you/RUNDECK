package com.amazonaws.internal.config;

import com.amazonaws.annotation.Immutable;

@Immutable
public class HttpClientConfig {
   private final String serviceName;
   private final String regionMetadataServiceName;

   HttpClientConfig(String serviceName, String regionMetadataServiceName) {
      this.serviceName = serviceName;
      this.regionMetadataServiceName = regionMetadataServiceName;
   }

   @Override
   public String toString() {
      return "serviceName: " + this.serviceName + ", regionMetadataServiceName: " + this.regionMetadataServiceName;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public String getRegionMetadataServiceName() {
      return this.regionMetadataServiceName;
   }
}
