package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetBucketWebsiteConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String expectedBucketOwner;

   public GetBucketWebsiteConfigurationRequest(String bucketName) {
      this.bucketName = bucketName;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetBucketWebsiteConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public GetBucketWebsiteConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }
}
