package com.amazonaws.services.s3.model;

public enum ObjectLockEnabled {
   ENABLED("Enabled");

   private final String objectLockEnabled;

   private ObjectLockEnabled(String objectLockEnabled) {
      this.objectLockEnabled = objectLockEnabled;
   }

   public static ObjectLockEnabled fromString(String objectLockEnabledString) {
      for(ObjectLockEnabled v : values()) {
         if (v.toString().equals(objectLockEnabledString)) {
            return v;
         }
      }

      throw new IllegalArgumentException("Cannot create enum from " + objectLockEnabledString + " value!");
   }

   @Override
   public String toString() {
      return this.objectLockEnabled;
   }
}
