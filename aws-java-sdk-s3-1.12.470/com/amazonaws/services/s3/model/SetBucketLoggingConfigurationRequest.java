package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketLoggingConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private BucketLoggingConfiguration loggingConfiguration;
   private String expectedBucketOwner;

   public SetBucketLoggingConfigurationRequest(String bucketName, BucketLoggingConfiguration loggingConfiguration) {
      this.bucketName = bucketName;
      this.loggingConfiguration = loggingConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketLoggingConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketLoggingConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public BucketLoggingConfiguration getLoggingConfiguration() {
      return this.loggingConfiguration;
   }

   public void setLoggingConfiguration(BucketLoggingConfiguration loggingConfiguration) {
      this.loggingConfiguration = loggingConfiguration;
   }

   public SetBucketLoggingConfigurationRequest withLoggingConfiguration(BucketLoggingConfiguration loggingConfiguration) {
      this.setLoggingConfiguration(loggingConfiguration);
      return this;
   }
}
