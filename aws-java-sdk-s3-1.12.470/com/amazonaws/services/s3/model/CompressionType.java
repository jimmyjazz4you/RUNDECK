package com.amazonaws.services.s3.model;

public enum CompressionType {
   NONE("NONE"),
   GZIP("GZIP"),
   BZIP2("BZIP2");

   private final String compressionType;

   private CompressionType(String compressionType) {
      this.compressionType = compressionType;
   }

   @Override
   public String toString() {
      return this.compressionType;
   }

   public static CompressionType fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(CompressionType enumEntry : values()) {
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
