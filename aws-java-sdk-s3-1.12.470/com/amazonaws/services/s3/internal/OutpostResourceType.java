package com.amazonaws.services.s3.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.StringUtils;

@SdkInternalApi
public enum OutpostResourceType {
   OUTPOST_BUCKET("bucket"),
   OUTPOST_ACCESS_POINT("accesspoint");

   private final String value;

   private OutpostResourceType(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public static OutpostResourceType fromValue(String value) {
      if (StringUtils.isNullOrEmpty(value)) {
         throw new IllegalArgumentException("value cannot be null or empty!");
      } else {
         for(OutpostResourceType enumEntry : values()) {
            if (enumEntry.toString().equals(value)) {
               return enumEntry;
            }
         }

         throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
      }
   }
}
