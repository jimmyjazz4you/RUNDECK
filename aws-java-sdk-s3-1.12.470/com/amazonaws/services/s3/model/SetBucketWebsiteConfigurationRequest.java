package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketWebsiteConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private BucketWebsiteConfiguration configuration;
   private String expectedBucketOwner;

   public SetBucketWebsiteConfigurationRequest(String bucketName, BucketWebsiteConfiguration configuration) {
      this.bucketName = bucketName;
      this.configuration = configuration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketWebsiteConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketWebsiteConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public void setConfiguration(BucketWebsiteConfiguration configuration) {
      this.configuration = configuration;
   }

   public BucketWebsiteConfiguration getConfiguration() {
      return this.configuration;
   }

   public SetBucketWebsiteConfigurationRequest withConfiguration(BucketWebsiteConfiguration configuration) {
      this.setConfiguration(configuration);
      return this;
   }
}
