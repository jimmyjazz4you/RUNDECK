package com.amazonaws.partitions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Map;

public class Service {
   private final Map<String, Endpoint> endpoints;
   private Endpoint defaults;
   private String partitionEndpoint;
   private boolean isRegionalized;

   public Service(@JsonProperty("endpoints") Map<String, Endpoint> endpoints) {
      this.endpoints = endpoints == null ? Collections.emptyMap() : endpoints;
   }

   public Map<String, Endpoint> getEndpoints() {
      return this.endpoints;
   }

   public Endpoint getDefaults() {
      return this.defaults;
   }

   public void setDefaults(Endpoint defaults) {
      this.defaults = defaults;
   }

   public String getPartitionEndpoint() {
      return this.partitionEndpoint;
   }

   @JsonProperty("partitionEndpoint")
   public void setPartitionEndpoint(String partitionEndpoint) {
      this.partitionEndpoint = partitionEndpoint;
   }

   public boolean isRegionalized() {
      return this.isRegionalized;
   }

   @JsonProperty("isRegionalized")
   public void setRegionalized(boolean regionalized) {
      this.isRegionalized = regionalized;
   }

   public boolean isPartitionWideEndpointAvailable() {
      return this.partitionEndpoint != null;
   }
}
