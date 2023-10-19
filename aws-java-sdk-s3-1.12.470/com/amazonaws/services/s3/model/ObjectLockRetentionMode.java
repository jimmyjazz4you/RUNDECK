package com.amazonaws.services.s3.model;

public enum ObjectLockRetentionMode {
   GOVERNANCE("GOVERNANCE"),
   COMPLIANCE("COMPLIANCE");

   private final String objectLockRetentionMode;

   private ObjectLockRetentionMode(String objectLockRetentionMode) {
      this.objectLockRetentionMode = objectLockRetentionMode;
   }

   public static ObjectLockRetentionMode fromString(String objectLockRetentionModeString) {
      for(ObjectLockRetentionMode v : values()) {
         if (v.toString().equals(objectLockRetentionModeString)) {
            return v;
         }
      }

      throw new IllegalArgumentException("Cannot create enum from " + objectLockRetentionModeString + " value!");
   }

   @Override
   public String toString() {
      return this.objectLockRetentionMode;
   }
}
