package com.amazonaws.services.s3.model;

public enum MetricsStatus {
   Enabled("Enabled"),
   Disabled("Disabled");

   private final String value;

   private MetricsStatus(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public static MetricsStatus fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(MetricsStatus enumEntry : values()) {
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
