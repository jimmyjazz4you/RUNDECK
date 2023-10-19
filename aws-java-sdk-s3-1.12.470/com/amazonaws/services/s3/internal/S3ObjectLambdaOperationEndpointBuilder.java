package com.amazonaws.services.s3.internal;

import com.amazonaws.util.StringUtils;
import java.net.URI;

public class S3ObjectLambdaOperationEndpointBuilder {
   private String region;
   private String protocol;
   private String domain;

   private S3ObjectLambdaOperationEndpointBuilder() {
   }

   public static S3ObjectLambdaOperationEndpointBuilder create() {
      return new S3ObjectLambdaOperationEndpointBuilder();
   }

   public S3ObjectLambdaOperationEndpointBuilder withRegion(String region) {
      this.region = region;
      return this;
   }

   public S3ObjectLambdaOperationEndpointBuilder withProtocol(String protocol) {
      this.protocol = protocol;
      return this;
   }

   public S3ObjectLambdaOperationEndpointBuilder withDomain(String domain) {
      this.domain = domain;
      return this;
   }

   public URI toURI() {
      if (StringUtils.isNullOrEmpty(this.protocol)) {
         throw new IllegalArgumentException("protocol must not be empty");
      } else if (StringUtils.isNullOrEmpty(this.domain)) {
         throw new IllegalArgumentException("domain must not be empty");
      } else if (StringUtils.isNullOrEmpty(this.region)) {
         throw new IllegalArgumentException("region must not be empty");
      } else {
         String uriString = String.format("%s://s3-object-lambda.%s.%s", this.protocol, this.region, this.domain);
         return URI.create(uriString);
      }
   }
}
