package com.amazonaws.regions;

import com.amazonaws.annotation.SdkInternalApi;
import java.util.List;

@SdkInternalApi
public interface RegionMetadataProvider {
   List<Region> getRegions();

   Region getRegion(String var1);

   List<Region> getRegionsForService(String var1);

   Region getRegionByEndpoint(String var1);

   Region tryGetRegionByExplicitEndpoint(String var1);

   Region tryGetRegionByEndpointDnsSuffix(String var1);
}
