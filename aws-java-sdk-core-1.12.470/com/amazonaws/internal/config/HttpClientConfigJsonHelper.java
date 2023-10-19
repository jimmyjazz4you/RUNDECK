package com.amazonaws.internal.config;

public class HttpClientConfigJsonHelper implements Builder<HttpClientConfig> {
   private String serviceName;
   private String regionMetadataServiceName;

   public HttpClientConfigJsonHelper() {
   }

   public HttpClientConfigJsonHelper(String serviceName, String regionMetadataServiceName) {
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

   public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
   }

   public String getRegionMetadataServiceName() {
      return this.regionMetadataServiceName;
   }

   public void setRegionMetadataServiceName(String regionMetadataServiceName) {
      this.regionMetadataServiceName = regionMetadataServiceName;
   }

   public HttpClientConfig build() {
      return new HttpClientConfig(this.serviceName, this.regionMetadataServiceName);
   }
}
