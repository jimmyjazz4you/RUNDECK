package com.amazonaws.services.s3.model;

public enum ExistingObjectReplicationStatus {
   ENABLED("Enabled"),
   DISABLED("Disabled");

   private final String value;

   private ExistingObjectReplicationStatus(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public static ExistingObjectReplicationStatus fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(ExistingObjectReplicationStatus enumEntry : values()) {
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
