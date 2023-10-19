package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public enum S3ResourceType {
   BUCKET("bucket_name"),
   ACCESS_POINT("accesspoint"),
   OBJECT("object"),
   OUTPOST("outpost"),
   OBJECT_LAMBDAS("object-lambda");

   private final String value;

   private S3ResourceType(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public static S3ResourceType fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(S3ResourceType enumEntry : values()) {
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
