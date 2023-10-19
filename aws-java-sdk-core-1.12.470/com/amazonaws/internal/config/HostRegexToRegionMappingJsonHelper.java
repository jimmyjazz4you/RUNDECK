package com.amazonaws.internal.config;

public class HostRegexToRegionMappingJsonHelper implements Builder<HostRegexToRegionMapping> {
   private String hostNameRegex;
   private String regionName;

   public String getHostNameRegex() {
      return this.hostNameRegex;
   }

   public void setHostNameRegex(String hostNameRegex) {
      this.hostNameRegex = hostNameRegex;
   }

   public String getRegionName() {
      return this.regionName;
   }

   public void setRegionName(String regionName) {
      this.regionName = regionName;
   }

   public HostRegexToRegionMapping build() {
      return new HostRegexToRegionMapping(this.hostNameRegex, this.regionName);
   }
}
