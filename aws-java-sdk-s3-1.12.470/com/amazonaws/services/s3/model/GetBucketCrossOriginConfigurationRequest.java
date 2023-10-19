package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class GetBucketCrossOriginConfigurationRequest extends GenericBucketRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String expectedBucketOwner;

   public GetBucketCrossOriginConfigurationRequest(String bucketName) {
      super(bucketName);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetBucketCrossOriginConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
