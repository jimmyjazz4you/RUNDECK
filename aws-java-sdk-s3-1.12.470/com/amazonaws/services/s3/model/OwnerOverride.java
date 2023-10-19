package com.amazonaws.services.s3.model;

public enum OwnerOverride {
   DESTINATION("Destination");

   private final String id;

   private OwnerOverride(String id) {
      this.id = id;
   }

   @Override
   public String toString() {
      return this.id;
   }

   public static OwnerOverride fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(OwnerOverride enumEntry : values()) {
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
