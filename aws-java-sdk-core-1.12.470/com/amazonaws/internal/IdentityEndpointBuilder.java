package com.amazonaws.internal;

import com.amazonaws.regions.Region;
import java.net.URI;

public class IdentityEndpointBuilder extends ServiceEndpointBuilder {
   private final URI endpoint;

   public IdentityEndpointBuilder(URI endpoint) {
      this.endpoint = endpoint;
   }

   @Override
   public URI getServiceEndpoint() {
      return this.endpoint;
   }

   public DefaultServiceEndpointBuilder withRegion(Region region) {
      return null;
   }

   @Override
   public Region getRegion() {
      return null;
   }
}
