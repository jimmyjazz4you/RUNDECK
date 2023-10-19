package com.amazonaws.auth;

import com.amazonaws.util.endpoint.RegionFromEndpointResolver;

public interface RegionFromEndpointResolverAwareSigner extends Signer {
   void setRegionFromEndpointResolver(RegionFromEndpointResolver var1);
}
