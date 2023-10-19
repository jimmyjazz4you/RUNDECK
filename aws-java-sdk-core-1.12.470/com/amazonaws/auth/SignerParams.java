package com.amazonaws.auth;

public class SignerParams {
   private String serviceName;
   private String regionName;

   public SignerParams(String serviceName, String regionName) {
      this.serviceName = serviceName;
      this.regionName = regionName;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public String getRegionName() {
      return this.regionName;
   }
}
