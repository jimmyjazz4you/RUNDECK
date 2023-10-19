package com.amazonaws.services.s3.internal;

import com.amazonaws.internal.ServiceEndpointBuilder;
import com.amazonaws.regions.Region;
import java.net.URI;
import java.net.URISyntaxException;

public class DualstackEndpointBuilder extends ServiceEndpointBuilder {
   private final String serviceName;
   private final String protocol;
   private Region region;

   public DualstackEndpointBuilder(String serviceName, String protocol, Region region) {
      this.serviceName = serviceName;
      this.protocol = protocol;
      this.region = region;
   }

   public DualstackEndpointBuilder withRegion(Region region) {
      if (region == null) {
         throw new IllegalArgumentException("Region cannot be null");
      } else {
         this.region = region;
         return this;
      }
   }

   public URI getServiceEndpoint() {
      String serviceEndpoint = String.format("%s.%s.%s.%s", this.serviceName, "dualstack", this.region.getName(), this.region.getDomain());
      return this.toURI(this.stripProtocol(serviceEndpoint));
   }

   private String stripProtocol(String endpoint) {
      int protocolIndex = endpoint.indexOf("://");
      return protocolIndex >= 0 ? endpoint.substring(protocolIndex + "://".length()) : endpoint;
   }

   private URI toURI(String endpoint) throws IllegalArgumentException {
      try {
         return new URI(String.format("%s://%s", this.protocol, endpoint));
      } catch (URISyntaxException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public Region getRegion() {
      return this.region;
   }
}
