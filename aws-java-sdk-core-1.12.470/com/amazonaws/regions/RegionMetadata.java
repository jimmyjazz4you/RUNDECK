package com.amazonaws.regions;

import java.util.List;

public class RegionMetadata {
   private final RegionMetadataProvider provider;

   public RegionMetadata(List<Region> regions) {
      this.provider = new InMemoryRegionsProvider(regions);
   }

   public RegionMetadata(RegionMetadataProvider provider) {
      this.provider = provider;
   }

   public List<Region> getRegions() {
      return this.provider.getRegions();
   }

   public Region getRegion(String name) {
      return this.provider.getRegion(name);
   }

   public List<Region> getRegionsForService(String service) {
      return this.provider.getRegionsForService(service);
   }

   public Region tryGetRegionByExplicitEndpoint(String endpoint) {
      return this.provider.tryGetRegionByExplicitEndpoint(endpoint);
   }

   public Region tryGetRegionByEndpointDnsSuffix(String endpoint) {
      return this.provider.tryGetRegionByEndpointDnsSuffix(endpoint);
   }

   @Deprecated
   public Region getRegionByEndpoint(String endpoint) {
      return this.provider.getRegionByEndpoint(endpoint);
   }

   @Override
   public String toString() {
      return this.provider.toString();
   }
}
