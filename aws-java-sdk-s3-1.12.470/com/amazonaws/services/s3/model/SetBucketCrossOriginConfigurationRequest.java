package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketCrossOriginConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private BucketCrossOriginConfiguration crossOriginConfiguration;
   private String expectedBucketOwner;

   public SetBucketCrossOriginConfigurationRequest(String bucketName, BucketCrossOriginConfiguration crossOriginConfiguration) {
      this.bucketName = bucketName;
      this.crossOriginConfiguration = crossOriginConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketCrossOriginConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketCrossOriginConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public BucketCrossOriginConfiguration getCrossOriginConfiguration() {
      return this.crossOriginConfiguration;
   }

   public void setCrossOriginConfiguration(BucketCrossOriginConfiguration crossOriginConfiguration) {
      this.crossOriginConfiguration = crossOriginConfiguration;
   }

   public SetBucketCrossOriginConfigurationRequest withCrossOriginConfiguration(BucketCrossOriginConfiguration crossOriginConfiguration) {
      this.setCrossOriginConfiguration(crossOriginConfiguration);
      return this;
   }
}
