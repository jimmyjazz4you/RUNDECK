package com.amazonaws.services.s3.model;

public enum MetadataDirective {
   COPY("COPY"),
   REPLACE("REPLACE");

   private String value;

   private MetadataDirective(String value) {
      this.value = value;
   }

   public static MetadataDirective fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(MetadataDirective enumEntry : values()) {
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
