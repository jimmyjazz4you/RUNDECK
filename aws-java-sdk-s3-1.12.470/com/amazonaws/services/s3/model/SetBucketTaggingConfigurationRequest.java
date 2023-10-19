package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketTaggingConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private BucketTaggingConfiguration taggingConfiguration;
   private String expectedBucketOwner;

   public SetBucketTaggingConfigurationRequest(String bucketName, BucketTaggingConfiguration taggingConfiguration) {
      this.bucketName = bucketName;
      this.taggingConfiguration = taggingConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketTaggingConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public SetBucketTaggingConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public BucketTaggingConfiguration getTaggingConfiguration() {
      return this.taggingConfiguration;
   }

   public void setTaggingConfiguration(BucketTaggingConfiguration taggingConfiguration) {
      this.taggingConfiguration = taggingConfiguration;
   }

   public SetBucketTaggingConfigurationRequest withTaggingConfiguration(BucketTaggingConfiguration taggingConfiguration) {
      this.setTaggingConfiguration(taggingConfiguration);
      return this;
   }
}
