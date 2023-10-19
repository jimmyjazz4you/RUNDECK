package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetObjectLockConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucket;
   private String expectedBucketOwner;

   public String getBucketName() {
      return this.bucket;
   }

   public GetObjectLockConfigurationRequest withBucketName(String bucket) {
      this.bucket = bucket;
      return this;
   }

   public void setBucketName(String bucket) {
      this.withBucketName(bucket);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetObjectLockConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
