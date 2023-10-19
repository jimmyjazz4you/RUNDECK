package com.amazonaws.services.s3.model;

public enum ReplicationTimeStatus {
   Enabled("Enabled"),
   Disabled("Disabled");

   private final String status;

   private ReplicationTimeStatus(String status) {
      this.status = status;
   }

   @Override
   public String toString() {
      return this.status;
   }

   public static ReplicationTimeStatus fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(ReplicationTimeStatus enumEntry : values()) {
            if (enumEntry.toString().equals(value)) {
               return enumEntry;
            }
         }

         throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
      } else {
         throw new IllegalArgumentException("Value cannot be null or empty!");
      }
   }
}
