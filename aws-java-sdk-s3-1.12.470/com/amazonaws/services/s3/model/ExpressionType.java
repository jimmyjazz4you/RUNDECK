package com.amazonaws.services.s3.model;

public enum ExpressionType {
   SQL("SQL");

   private final String expressionType;

   private ExpressionType(String expressionType) {
      this.expressionType = expressionType;
   }

   @Override
   public String toString() {
      return this.expressionType;
   }

   public static ExpressionType fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(ExpressionType enumEntry : values()) {
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
