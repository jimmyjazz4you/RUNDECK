package com.amazonaws.services.s3.model;

public class GetBucketAccelerateConfigurationRequest extends GenericBucketRequest implements ExpectedBucketOwnerRequest {
   private String expectedBucketOwner;

   public GetBucketAccelerateConfigurationRequest(String bucketName) {
      super(bucketName);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetBucketAccelerateConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
