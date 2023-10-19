package com.amazonaws.services.s3.model;

public class DeleteBucketReplicationConfigurationRequest extends GenericBucketRequest implements ExpectedBucketOwnerRequest {
   private String expectedBucketOwner;

   public DeleteBucketReplicationConfigurationRequest(String bucketName) {
      super(bucketName);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public DeleteBucketReplicationConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
