package com.amazonaws.regions;

import com.amazonaws.util.endpoint.RegionFromEndpointResolver;

public class MetadataSupportedRegionFromEndpointProvider implements RegionFromEndpointResolver {
   @Override
   public String guessRegionFromEndpoint(String host, String serviceHint) {
      return EndpointToRegion.guessRegionNameForEndpoint(host, serviceHint);
   }
}
