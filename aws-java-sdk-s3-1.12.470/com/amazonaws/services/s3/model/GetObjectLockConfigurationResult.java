package com.amazonaws.services.s3.model;

public class GetObjectLockConfigurationResult {
   private ObjectLockConfiguration objectLockConfiguration;

   public ObjectLockConfiguration getObjectLockConfiguration() {
      return this.objectLockConfiguration;
   }

   public GetObjectLockConfigurationResult withObjectLockConfiguration(ObjectLockConfiguration objectLockConfiguration) {
      this.objectLockConfiguration = objectLockConfiguration;
      return this;
   }

   public void setObjectLockConfiguration(ObjectLockConfiguration objectLockConfiguration) {
      this.withObjectLockConfiguration(objectLockConfiguration);
   }
}
