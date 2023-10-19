package com.amazonaws.services.s3.model;

public enum RestoreRequestType {
   SELECT("SELECT");

   private final String type;

   private RestoreRequestType(String type) {
      this.type = type;
   }

   @Override
   public String toString() {
      return this.type;
   }

   public static RestoreRequestType fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(RestoreRequestType enumEntry : values()) {
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
