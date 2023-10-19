package com.amazonaws.services.s3.model;

public enum ObjectLockMode {
   GOVERNANCE("GOVERNANCE"),
   COMPLIANCE("COMPLIANCE");

   private final String objectLockMode;

   private ObjectLockMode(String objectLockMode) {
      this.objectLockMode = objectLockMode;
   }

   public static ObjectLockMode fromString(String objectLockModeString) {
      for(ObjectLockMode v : values()) {
         if (v.toString().equals(objectLockModeString)) {
            return v;
         }
      }

      throw new IllegalArgumentException("Cannot create enum from " + objectLockModeString + " value!");
   }

   @Override
   public String toString() {
      return this.objectLockMode;
   }
}
