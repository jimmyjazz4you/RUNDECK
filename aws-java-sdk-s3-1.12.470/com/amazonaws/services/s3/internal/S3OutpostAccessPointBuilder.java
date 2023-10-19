package com.amazonaws.services.s3.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.HostnameValidator;
import java.net.URI;

@SdkInternalApi
public final class S3OutpostAccessPointBuilder {
   private URI endpointOverride;
   private String accessPointName;
   private String outpostId;
   private String region;
   private String accountId;
   private String protocol;
   private String domain;

   private S3OutpostAccessPointBuilder() {
   }

   public static S3OutpostAccessPointBuilder create() {
      return new S3OutpostAccessPointBuilder();
   }

   public S3OutpostAccessPointBuilder withEndpointOverride(URI endpointOverride) {
      this.endpointOverride = endpointOverride;
      return this;
   }

   public S3OutpostAccessPointBuilder withAccessPointName(String accessPointName) {
      this.accessPointName = accessPointName;
      return this;
   }

   public S3OutpostAccessPointBuilder withRegion(String region) {
      this.region = region;
      return this;
   }

   public S3OutpostAccessPointBuilder withAccountId(String accountId) {
      this.accountId = accountId;
      return this;
   }

   public S3OutpostAccessPointBuilder withOutpostId(String outpostId) {
      this.outpostId = outpostId;
      return this;
   }

   public S3OutpostAccessPointBuilder withProtocol(String protocol) {
      this.protocol = protocol;
      return this;
   }

   public S3OutpostAccessPointBuilder withDomain(String domain) {
      this.domain = domain;
      return this;
   }

   public URI toURI() {
      HostnameValidator.validateHostnameCompliant(this.outpostId, "outpostId", "outpost ARN");
      HostnameValidator.validateHostnameCompliant(this.accountId, "accountId", "outpost ARN");
      HostnameValidator.validateHostnameCompliant(this.accessPointName, "accessPointName", "outpost ARN");
      String uriString;
      if (this.endpointOverride == null) {
         uriString = String.format(
            "%s://%s-%s.%s.s3-outposts.%s.%s", this.protocol, this.accessPointName, this.accountId, this.outpostId, this.region, this.domain
         );
      } else {
         StringBuilder uriSuffix = new StringBuilder(this.endpointOverride.getHost());
         if (this.endpointOverride.getPort() > 0) {
            uriSuffix.append(":").append(this.endpointOverride.getPort());
         }

         if (this.endpointOverride.getPath() != null) {
            uriSuffix.append(this.endpointOverride.getPath());
         }

         uriString = String.format("%s://%s-%s.%s.%s", this.protocol, this.accessPointName, this.accountId, this.outpostId, uriSuffix);
      }

      return URI.create(uriString);
   }
}
