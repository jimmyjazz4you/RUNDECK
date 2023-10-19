package com.amazonaws.services.s3.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.HostnameValidator;
import java.net.URI;

@SdkInternalApi
public class S3AccessPointBuilder {
   private URI endpointOverride;
   private Boolean dualstackEnabled;
   private Boolean fipsEnabled;
   private String accessPointName;
   private String region;
   private String accountId;
   private String protocol;
   private String domain;

   private S3AccessPointBuilder() {
   }

   public static S3AccessPointBuilder create() {
      return new S3AccessPointBuilder();
   }

   public void setEndpointOverride(URI endpointOverride) {
      this.endpointOverride = endpointOverride;
   }

   public S3AccessPointBuilder withEndpointOverride(URI endpointOverride) {
      this.setEndpointOverride(endpointOverride);
      return this;
   }

   public void setDualstackEnabled(Boolean dualstackEnabled) {
      this.dualstackEnabled = dualstackEnabled;
   }

   public S3AccessPointBuilder withDualstackEnabled(Boolean dualstackEnabled) {
      this.setDualstackEnabled(dualstackEnabled);
      return this;
   }

   public void setFipsEnabled(Boolean fipsEnabled) {
      this.fipsEnabled = fipsEnabled;
   }

   public S3AccessPointBuilder withFipsEnabled(Boolean fipsEnabled) {
      this.fipsEnabled = fipsEnabled;
      return this;
   }

   public void setAccessPointName(String accessPointName) {
      this.accessPointName = accessPointName;
   }

   public S3AccessPointBuilder withAccessPointName(String accessPointName) {
      this.setAccessPointName(accessPointName);
      return this;
   }

   public void setRegion(String region) {
      this.region = region;
   }

   public S3AccessPointBuilder withRegion(String region) {
      this.setRegion(region);
      return this;
   }

   public void setAccountId(String accountId) {
      this.accountId = accountId;
   }

   public S3AccessPointBuilder withAccountId(String accountId) {
      this.setAccountId(accountId);
      return this;
   }

   public void setProtocol(String protocol) {
      this.protocol = protocol;
   }

   public S3AccessPointBuilder withProtocol(String protocol) {
      this.setProtocol(protocol);
      return this;
   }

   public void setDomain(String domain) {
      this.domain = domain;
   }

   public S3AccessPointBuilder withDomain(String domain) {
      this.setDomain(domain);
      return this;
   }

   public URI toURI() {
      HostnameValidator.validateHostnameCompliant(this.accountId, "accountId", "access point ARN");
      HostnameValidator.validateHostnameCompliant(this.accessPointName, "accessPointName", "access point ARN");
      String uriString;
      if (this.endpointOverride == null) {
         String fipsSegment = Boolean.TRUE.equals(this.fipsEnabled) ? "-fips" : "";
         String dualStackSegment = Boolean.TRUE.equals(this.dualstackEnabled) ? "dualstack." : "";
         uriString = String.format(
            "%s://%s-%s.s3-accesspoint%s.%s%s.%s", this.protocol, this.accessPointName, this.accountId, fipsSegment, dualStackSegment, this.region, this.domain
         );
      } else {
         if (Boolean.TRUE.equals(this.fipsEnabled)) {
            throw new IllegalArgumentException("FIPS regions are not supported with an endpoint override specified");
         }

         if (Boolean.TRUE.equals(this.dualstackEnabled)) {
            throw new IllegalArgumentException("Dual stack is not supported with an endpoint override specified");
         }

         StringBuilder uriSuffix = new StringBuilder(this.endpointOverride.getHost());
         if (this.endpointOverride.getPort() > 0) {
            uriSuffix.append(":").append(this.endpointOverride.getPort());
         }

         if (this.endpointOverride.getPath() != null) {
            uriSuffix.append(this.endpointOverride.getPath());
         }

         uriString = String.format("%s://%s-%s.%s", this.protocol, this.accessPointName, this.accountId, uriSuffix);
      }

      return URI.create(uriString);
   }
}
