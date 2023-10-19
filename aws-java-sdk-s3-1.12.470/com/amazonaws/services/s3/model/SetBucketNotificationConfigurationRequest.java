package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketNotificationConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private BucketNotificationConfiguration notificationConfiguration;
   private String bucketName;
   private String expectedBucketOwner;
   private Boolean skipDestinationValidation;

   @Deprecated
   public SetBucketNotificationConfigurationRequest(BucketNotificationConfiguration bucketNotificationConfiguration, String bucket) {
      this.notificationConfiguration = bucketNotificationConfiguration;
      this.bucketName = bucket;
   }

   public SetBucketNotificationConfigurationRequest(String bucketName, BucketNotificationConfiguration notificationConfiguration) {
      this.bucketName = bucketName;
      this.notificationConfiguration = notificationConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketNotificationConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   @Deprecated
   public BucketNotificationConfiguration getBucketNotificationConfiguration() {
      return this.notificationConfiguration;
   }

   public BucketNotificationConfiguration getNotificationConfiguration() {
      return this.notificationConfiguration;
   }

   @Deprecated
   public void setBucketNotificationConfiguration(BucketNotificationConfiguration bucketNotificationConfiguration) {
      this.notificationConfiguration = bucketNotificationConfiguration;
   }

   public void setNotificationConfiguration(BucketNotificationConfiguration notificationConfiguration) {
      this.notificationConfiguration = notificationConfiguration;
   }

   public SetBucketNotificationConfigurationRequest withNotificationConfiguration(BucketNotificationConfiguration notificationConfiguration) {
      this.setNotificationConfiguration(notificationConfiguration);
      return this;
   }

   @Deprecated
   public String getBucket() {
      return this.bucketName;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   @Deprecated
   public void setBucket(String bucket) {
      this.bucketName = bucket;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public SetBucketNotificationConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public Boolean getSkipDestinationValidation() {
      return this.skipDestinationValidation;
   }

   public void setSkipDestinationValidation(Boolean skipDestinationValidation) {
      this.skipDestinationValidation = skipDestinationValidation;
   }

   public SetBucketNotificationConfigurationRequest withSkipDestinationValidation(Boolean skipDestinationValidation) {
      this.skipDestinationValidation = skipDestinationValidation;
      return this;
   }
}
