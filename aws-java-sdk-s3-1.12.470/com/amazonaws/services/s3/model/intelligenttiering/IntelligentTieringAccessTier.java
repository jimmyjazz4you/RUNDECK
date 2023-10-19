package com.amazonaws.services.s3.model.intelligenttiering;

public enum IntelligentTieringAccessTier {
   ARCHIVE_ACCESS("ARCHIVE_ACCESS"),
   DEEP_ARCHIVE_ACCESS("DEEP_ARCHIVE_ACCESS");

   private final String intelligentTieringAccessTier;

   private IntelligentTieringAccessTier(String intelligentTieringAccessTier) {
      this.intelligentTieringAccessTier = intelligentTieringAccessTier;
   }

   @Override
   public String toString() {
      return this.intelligentTieringAccessTier;
   }

   public static IntelligentTieringAccessTier fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(IntelligentTieringAccessTier enumEntry : values()) {
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
