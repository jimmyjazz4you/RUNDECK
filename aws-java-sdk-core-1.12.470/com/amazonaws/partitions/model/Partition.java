package com.amazonaws.partitions.model;

import com.amazonaws.util.ValidationUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.regex.Pattern;

public class Partition {
   private final String partition;
   private final Map<String, Region> regions;
   private final Map<String, Service> services;
   private String partitionName;
   private String dnsSuffix;
   private String regionRegex;
   private Endpoint defaults;

   public Partition(
      @JsonProperty("partition") String partition,
      @JsonProperty("regions") Map<String, Region> regions,
      @JsonProperty("services") Map<String, Service> services
   ) {
      this.partition = ValidationUtils.assertNotNull(partition, "Partition");
      this.regions = regions;
      this.services = services;
   }

   public String getPartition() {
      return this.partition;
   }

   public String getPartitionName() {
      return this.partitionName;
   }

   public void setPartitionName(String partitionName) {
      this.partitionName = partitionName;
   }

   public String getDnsSuffix() {
      return this.dnsSuffix;
   }

   public void setDnsSuffix(String dnsSuffix) {
      this.dnsSuffix = dnsSuffix;
   }

   public String getRegionRegex() {
      return this.regionRegex;
   }

   public void setRegionRegex(String regionRegex) {
      this.regionRegex = regionRegex;
   }

   public Endpoint getDefaults() {
      return this.defaults;
   }

   public void setDefaults(Endpoint defaults) {
      this.defaults = defaults;
   }

   public Map<String, Region> getRegions() {
      return this.regions;
   }

   public Map<String, Service> getServices() {
      return this.services;
   }

   public boolean hasRegion(String region) {
      return this.regions.containsKey(region) || this.matchesRegionRegex(region) || this.hasServiceEndpoint(region);
   }

   private boolean matchesRegionRegex(String region) {
      Pattern p = Pattern.compile(this.regionRegex);
      return p.matcher(region).matches();
   }

   @Deprecated
   private boolean hasServiceEndpoint(String endpoint) {
      for(Service s : this.services.values()) {
         if (s.getEndpoints().containsKey(endpoint)) {
            return true;
         }
      }

      return false;
   }
}
