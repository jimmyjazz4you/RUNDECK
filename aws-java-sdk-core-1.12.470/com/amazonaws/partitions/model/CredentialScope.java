package com.amazonaws.partitions.model;

public class CredentialScope {
   private String region;
   private String service;

   public String getRegion() {
      return this.region;
   }

   public void setRegion(String region) {
      this.region = region;
   }

   public String getService() {
      return this.service;
   }

   public void setService(String service) {
      this.service = service;
   }
}
