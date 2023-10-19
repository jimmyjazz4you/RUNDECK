package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public final class EndpointParams {
   private String protocol;
   private String domain;
   private boolean dualStackEnabled;
   private boolean fipsEnabled;

   public String getProtocol() {
      return this.protocol;
   }

   public EndpointParams withProtocol(String protocol) {
      this.protocol = protocol;
      return this;
   }

   public String getDomain() {
      return this.domain;
   }

   public EndpointParams withDomain(String domain) {
      this.domain = domain;
      return this;
   }

   public boolean isDualStackEnabled() {
      return this.dualStackEnabled;
   }

   public EndpointParams withDualStackEnabled(boolean dualStackEnabled) {
      this.dualStackEnabled = dualStackEnabled;
      return this;
   }

   public boolean isFipsEnabled() {
      return this.fipsEnabled;
   }

   public EndpointParams withFipsEnabled(boolean fipsEnabled) {
      this.fipsEnabled = fipsEnabled;
      return this;
   }
}
