package com.amazonaws.util.endpoint;

import com.amazonaws.util.AwsHostNameUtils;

public class DefaultRegionFromEndpointResolver implements RegionFromEndpointResolver {
   @Override
   public String guessRegionFromEndpoint(String host, String serviceHint) {
      return AwsHostNameUtils.parseRegion(host, serviceHint);
   }
}
