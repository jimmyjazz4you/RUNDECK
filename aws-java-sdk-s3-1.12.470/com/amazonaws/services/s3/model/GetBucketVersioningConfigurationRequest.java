package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class GetBucketVersioningConfigurationRequest extends GenericBucketRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String expectedBucketOwner;

   public GetBucketVersioningConfigurationRequest(String bucketName) {
      super(bucketName);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetBucketVersioningConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
