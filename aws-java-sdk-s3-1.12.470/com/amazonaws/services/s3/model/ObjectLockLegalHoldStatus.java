package com.amazonaws.services.s3.model;

public enum ObjectLockLegalHoldStatus {
   ON("ON"),
   OFF("OFF");

   private final String objectLockLegalHoldStatus;

   private ObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.objectLockLegalHoldStatus = objectLockLegalHoldStatus;
   }

   public static ObjectLockLegalHoldStatus fromString(String objectLockLegalHoldStatusString) {
      for(ObjectLockLegalHoldStatus v : values()) {
         if (v.toString().equals(objectLockLegalHoldStatusString)) {
            return v;
         }
      }

      throw new IllegalArgumentException("Cannot create enum from " + objectLockLegalHoldStatusString + " value!");
   }

   @Override
   public String toString() {
      return this.objectLockLegalHoldStatus;
   }
}
