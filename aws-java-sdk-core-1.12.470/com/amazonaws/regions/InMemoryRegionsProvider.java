package com.amazonaws.regions;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SdkInternalApi
public class InMemoryRegionsProvider extends AbstractRegionMetadataProvider {
   private final List<Region> regions;

   public InMemoryRegionsProvider(List<Region> regions) {
      ValidationUtils.assertNotNull(regions, "regions");
      this.regions = Collections.unmodifiableList(new ArrayList<>(regions));
   }

   @Override
   public List<Region> getRegions() {
      return Collections.unmodifiableList(new ArrayList<>(this.regions));
   }

   @Override
   public Region getRegion(String regionName) {
      for(Region region : this.regions) {
         if (region.getName().equals(regionName)) {
            return region;
         }
      }

      return null;
   }

   @Override
   public List<Region> getRegionsForService(String serviceName) {
      List<Region> results = new LinkedList<>();

      for(Region region : this.regions) {
         if (region.isServiceSupported(serviceName)) {
            results.add(region);
         }
      }

      return results;
   }

   @Override
   public Region tryGetRegionByExplicitEndpoint(String endpoint) {
      return null;
   }

   @Override
   public Region tryGetRegionByEndpointDnsSuffix(String endpoint) {
      return null;
   }

   @Override
   public String toString() {
      return this.regions.toString();
   }
}
