package com.amazonaws.services.s3.model;

public enum Tier {
   Standard("Standard"),
   Bulk("Bulk"),
   Expedited("Expedited");

   private final String value;

   private Tier(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public static Tier fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(Tier enumEntry : values()) {
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
