package com.amazonaws.services.s3.internal;

import com.amazonaws.util.HostnameValidator;
import java.net.URI;

public class S3ObjectLambdaEndpointBuilder {
   private URI endpointOverride;
   private String accessPointName;
   private String region;
   private String accountId;
   private String protocol;
   private String domain;
   private Boolean fipsEnabled;
   private Boolean dualstackEnabled;

   private S3ObjectLambdaEndpointBuilder() {
   }

   public static S3ObjectLambdaEndpointBuilder create() {
      return new S3ObjectLambdaEndpointBuilder();
   }

   public S3ObjectLambdaEndpointBuilder withEndpointOverride(URI endpointOverride) {
      this.endpointOverride = endpointOverride;
      return this;
   }

   public S3ObjectLambdaEndpointBuilder withAccessPointName(String accessPointName) {
      this.accessPointName = accessPointName;
      return this;
   }

   public S3ObjectLambdaEndpointBuilder withRegion(String region) {
      this.region = region;
      return this;
   }

   public S3ObjectLambdaEndpointBuilder withAccountId(String accountId) {
      this.accountId = accountId;
      return this;
   }

   public S3ObjectLambdaEndpointBuilder withProtocol(String protocol) {
      this.protocol = protocol;
      return this;
   }

   public S3ObjectLambdaEndpointBuilder withDomain(String domain) {
      this.domain = domain;
      return this;
   }

   public S3ObjectLambdaEndpointBuilder withFipsEnabled(Boolean fipsEnabled) {
      this.fipsEnabled = fipsEnabled;
      return this;
   }

   public S3ObjectLambdaEndpointBuilder withDualstackEnabled(Boolean dualstackEnabled) {
      this.dualstackEnabled = dualstackEnabled;
      return this;
   }

   public URI toURI() {
      HostnameValidator.validateHostnameCompliant(this.accountId, "accountId", "object lambda ARN");
      HostnameValidator.validateHostnameCompliant(this.accessPointName, "accessPointName", "object lambda ARN");
      String fipsSegment = Boolean.TRUE.equals(this.fipsEnabled) ? "-fips" : "";
      String uriString;
      if (this.endpointOverride == null) {
         if (Boolean.TRUE.equals(this.dualstackEnabled)) {
            throw new IllegalArgumentException("S3 Object Lambda does not support Dual stack endpoints");
         }

         uriString = String.format(
            "%s://%s-%s.s3-object-lambda%s.%s.%s", this.protocol, this.accessPointName, this.accountId, fipsSegment, this.region, this.domain
         );
      } else {
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
