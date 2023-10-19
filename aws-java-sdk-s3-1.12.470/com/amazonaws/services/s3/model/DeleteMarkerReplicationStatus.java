package com.amazonaws.services.s3.model;

public enum DeleteMarkerReplicationStatus {
   ENABLED("Enabled"),
   DISABLED("Disabled");

   private final String value;

   private DeleteMarkerReplicationStatus(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public static DeleteMarkerReplicationStatus fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(DeleteMarkerReplicationStatus enumEntry : values()) {
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
