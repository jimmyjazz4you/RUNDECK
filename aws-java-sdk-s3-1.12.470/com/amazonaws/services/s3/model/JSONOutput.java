package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class JSONOutput implements Serializable {
   private String recordDelimiter;

   public Character getRecordDelimiter() {
      return this.stringToChar(this.recordDelimiter);
   }

   public String getRecordDelimiterAsString() {
      return this.recordDelimiter;
   }

   public void setRecordDelimiter(String recordDelimiter) {
      this.validateNotEmpty(recordDelimiter, "recordDelimiter");
      this.recordDelimiter = recordDelimiter;
   }

   public JSONOutput withRecordDelimiter(String recordDelimiter) {
      this.setRecordDelimiter(recordDelimiter);
      return this;
   }

   public void setRecordDelimiter(Character recordDelimiter) {
      this.setRecordDelimiter(this.charToString(recordDelimiter));
   }

   public JSONOutput withRecordDelimiter(Character recordDelimiter) {
      this.setRecordDelimiter(recordDelimiter);
      return this;
   }

   private String charToString(Character character) {
      return character == null ? null : character.toString();
   }

   private Character stringToChar(String string) {
      return string == null ? null : string.charAt(0);
   }

   private void validateNotEmpty(String value, String valueName) {
      if ("".equals(value)) {
         throw new IllegalArgumentException(valueName + " must not be empty-string.");
      }
   }
}
