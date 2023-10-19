package com.amazonaws.services.s3.model;

public enum QuoteFields {
   ALWAYS("ALWAYS"),
   ASNEEDED("ASNEEDED");

   private final String value;

   private QuoteFields(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }

   public static QuoteFields fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(QuoteFields enumEntry : values()) {
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
