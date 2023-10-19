package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetObjectLockConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucket;
   private ObjectLockConfiguration objectLockConfiguration;
   private boolean isRequesterPays;
   private String token;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetObjectLockConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.bucket;
   }

   public SetObjectLockConfigurationRequest withBucketName(String bucket) {
      this.bucket = bucket;
      return this;
   }

   public void setBucketName(String bucket) {
      this.withBucketName(bucket);
   }

   public ObjectLockConfiguration getObjectLockConfiguration() {
      return this.objectLockConfiguration;
   }

   public SetObjectLockConfigurationRequest withObjectLockConfiguration(ObjectLockConfiguration objectLockConfiguration) {
      this.objectLockConfiguration = objectLockConfiguration;
      return this;
   }

   public void setObjectLockConfiguration(ObjectLockConfiguration objectLockConfiguration) {
      this.withObjectLockConfiguration(objectLockConfiguration);
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public SetObjectLockConfigurationRequest withRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
      return this;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public String getToken() {
      return this.token;
   }

   public SetObjectLockConfigurationRequest withToken(String token) {
      this.token = token;
      return this;
   }

   public void setToken(String token) {
      this.withToken(token);
   }
}
