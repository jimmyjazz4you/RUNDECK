package com.amazonaws.services.s3.model;

public enum BucketAccelerateStatus {
   Enabled("Enabled"),
   Suspended("Suspended");

   private final String accelerateStatus;

   public static BucketAccelerateStatus fromValue(String statusString) throws IllegalArgumentException {
      for(BucketAccelerateStatus accelerateStatus : values()) {
         if (accelerateStatus.toString().equals(statusString)) {
            return accelerateStatus;
         }
      }

      throw new IllegalArgumentException("Cannot create enum from " + statusString + " value!");
   }

   private BucketAccelerateStatus(String status) {
      this.accelerateStatus = status;
   }

   @Override
   public String toString() {
      return this.accelerateStatus;
   }
}
