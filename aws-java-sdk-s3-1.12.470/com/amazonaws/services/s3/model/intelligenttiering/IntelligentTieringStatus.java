package com.amazonaws.services.s3.model.intelligenttiering;

public enum IntelligentTieringStatus {
   Enabled("Enabled"),
   Disabled("Disabled");

   private final String intelligentTieringStatus;

   private IntelligentTieringStatus(String intelligentTieringStatus) {
      this.intelligentTieringStatus = intelligentTieringStatus;
   }

   @Override
   public String toString() {
      return this.intelligentTieringStatus;
   }

   public static IntelligentTieringStatus fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(IntelligentTieringStatus enumEntry : values()) {
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
