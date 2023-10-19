package com.amazonaws.services.s3.model;

public enum JSONType {
   DOCUMENT("DOCUMENT"),
   LINES("LINES");

   private final String jsonType;

   private JSONType(String jsonType) {
      this.jsonType = jsonType;
   }

   @Override
   public String toString() {
      return this.jsonType;
   }

   public static JSONType fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(JSONType enumEntry : values()) {
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
