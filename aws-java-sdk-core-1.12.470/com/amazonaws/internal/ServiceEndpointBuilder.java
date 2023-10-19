package com.amazonaws.internal;

import com.amazonaws.regions.Region;
import java.net.URI;

public abstract class ServiceEndpointBuilder {
   public abstract URI getServiceEndpoint();

   public abstract Region getRegion();

   public abstract ServiceEndpointBuilder withRegion(Region var1);
}
