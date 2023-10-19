package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class GetBucketReplicationConfigurationRequest extends GenericBucketRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String expectedBucketOwner;

   public GetBucketReplicationConfigurationRequest(String bucketName) {
      super(bucketName);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetBucketReplicationConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
