package com.amazonaws.partitions;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.partitions.model.Endpoint;
import com.amazonaws.partitions.model.Partition;
import com.amazonaws.partitions.model.Service;
import com.amazonaws.regions.AbstractRegionMetadataProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@SdkInternalApi
public class PartitionMetadataProvider extends AbstractRegionMetadataProvider {
   private static final String STANDARD_PARTITION_HOSTNAME = "{service}.{region}.{dnsSuffix}";
   private final Map<String, Partition> partitionMap = new HashMap<>();
   private final Map<String, Region> credentialScopeRegionByHost = new HashMap<>();
   private final Set<String> standardHostnamePatternDnsSuffixes = new HashSet<>();
   private final Map<String, Region> regionCache = new ConcurrentHashMap<>();

   public PartitionMetadataProvider(List<Partition> partitions) {
      ValidationUtils.assertNotNull(partitions, "partitions");

      for(Partition p : partitions) {
         this.partitionMap.put(p.getPartition(), p);
         if (p.getDefaults() != null && "{service}.{region}.{dnsSuffix}".equals(p.getDefaults().getHostName())) {
            this.standardHostnamePatternDnsSuffixes.add(p.getDnsSuffix());
         }

         for(Service service : p.getServices().values()) {
            for(Endpoint endpoint : service.getEndpoints().values()) {
               if (endpoint.getHostName() != null && endpoint.getCredentialScope() != null && endpoint.getCredentialScope().getRegion() != null) {
                  Region region = this.cacheRegion(new PartitionRegionImpl(endpoint.getCredentialScope().getRegion(), p));
                  this.credentialScopeRegionByHost.put(endpoint.getHostName(), region);
               }
            }
         }
      }
   }

   @Override
   public List<Region> getRegions() {
      List<Region> regions = new ArrayList<>();

      for(Partition p : this.partitionMap.values()) {
         for(Entry<String, com.amazonaws.partitions.model.Region> entry : p.getRegions().entrySet()) {
            regions.add(new Region(new PartitionRegionImpl(entry.getKey(), p)));
         }
      }

      return Collections.unmodifiableList(regions);
   }

   @Override
   public Region getRegion(String regionName) {
      if (regionName == null) {
         return null;
      } else {
         Region regionFromCache = this.getRegionFromCache(regionName);
         return regionFromCache != null ? regionFromCache : this.createNewRegion(regionName);
      }
   }

   private Region createNewRegion(String regionName) {
      for(Partition p : this.partitionMap.values()) {
         if (p.hasRegion(regionName)) {
            return this.cacheRegion(new PartitionRegionImpl(regionName, p));
         }
      }

      Partition awsPartition = this.partitionMap.get("aws");
      return awsPartition != null ? this.cacheRegion(new PartitionRegionImpl(regionName, awsPartition)) : null;
   }

   private Region getRegionFromCache(String regionName) {
      return this.regionCache.get(regionName);
   }

   private Region cacheRegion(PartitionRegionImpl regionImpl) {
      Region region = new Region(regionImpl);
      this.regionCache.put(region.getName(), region);
      return region;
   }

   @Override
   public List<Region> getRegionsForService(String serviceName) {
      List<Region> allRegions = this.getRegions();
      List<Region> serviceSupportedRegions = new ArrayList<>();

      for(Region r : allRegions) {
         if (r.isServiceSupported(serviceName)) {
            serviceSupportedRegions.add(r);
         }
      }

      return serviceSupportedRegions;
   }

   @Override
   public Region tryGetRegionByExplicitEndpoint(String endpoint) {
      String host = getHost(endpoint);
      return this.credentialScopeRegionByHost.get(host);
   }

   @Override
   public Region tryGetRegionByEndpointDnsSuffix(String endpoint) {
      String host = getHost(endpoint);

      for(String dnsSuffix : this.standardHostnamePatternDnsSuffixes) {
         dnsSuffix = "." + dnsSuffix;
         if (host.endsWith(dnsSuffix)) {
            String serviceRegion = host.substring(0, host.length() - dnsSuffix.length());
            String region = serviceRegion.substring(serviceRegion.lastIndexOf(46) + 1);
            if (region.isEmpty()) {
               return null;
            }

            return this.getRegion(region);
         }
      }

      return null;
   }
}
