package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class CSVOutput implements Serializable, Cloneable {
   private String quoteFields;
   private String quoteEscapeCharacter;
   private String recordDelimiter;
   private String fieldDelimiter;
   private String quoteCharacter;

   public String getQuoteFields() {
      return this.quoteFields;
   }

   public void setQuoteFields(String quoteFields) {
      this.quoteFields = quoteFields;
   }

   public CSVOutput withQuoteFields(String quoteFields) {
      this.setQuoteFields(quoteFields);
      return this;
   }

   public void setQuoteFields(QuoteFields quoteFields) {
      this.setQuoteFields(quoteFields == null ? null : quoteFields.toString());
   }

   public CSVOutput withQuoteFields(QuoteFields quoteFields) {
      this.setQuoteFields(quoteFields);
      return this;
   }

   public Character getQuoteEscapeCharacter() {
      return this.stringToChar(this.quoteEscapeCharacter);
   }

   public String getQuoteEscapeCharacterAsString() {
      return this.quoteEscapeCharacter;
   }

   public void setQuoteEscapeCharacter(String quoteEscapeCharacter) {
      this.validateNotEmpty(quoteEscapeCharacter, "quoteEscapeCharacter");
      this.quoteEscapeCharacter = quoteEscapeCharacter;
   }

   public CSVOutput withQuoteEscapeCharacter(String quoteEscapeCharacter) {
      this.setQuoteEscapeCharacter(quoteEscapeCharacter);
      return this;
   }

   public void setQuoteEscapeCharacter(Character quoteEscapeCharacter) {
      this.setQuoteEscapeCharacter(this.charToString(quoteEscapeCharacter));
   }

   public CSVOutput withQuoteEscapeCharacter(Character quoteEscapeCharacter) {
      this.setQuoteEscapeCharacter(quoteEscapeCharacter);
      return this;
   }

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

   public CSVOutput withRecordDelimiter(String recordDelimiter) {
      this.setRecordDelimiter(recordDelimiter);
      return this;
   }

   public void setRecordDelimiter(Character recordDelimiter) {
      this.setRecordDelimiter(this.charToString(recordDelimiter));
   }

   public CSVOutput withRecordDelimiter(Character recordDelimiter) {
      this.setRecordDelimiter(recordDelimiter);
      return this;
   }

   public Character getFieldDelimiter() {
      return this.stringToChar(this.fieldDelimiter);
   }

   public String getFieldDelimiterAsString() {
      return this.fieldDelimiter;
   }

   public void setFieldDelimiter(String fieldDelimiter) {
      this.validateNotEmpty(fieldDelimiter, "fieldDelimiter");
      this.fieldDelimiter = fieldDelimiter;
   }

   public CSVOutput withFieldDelimiter(String fieldDelimiter) {
      this.setFieldDelimiter(fieldDelimiter);
      return this;
   }

   public void setFieldDelimiter(Character fieldDelimiter) {
      this.setFieldDelimiter(this.charToString(fieldDelimiter));
   }

   public CSVOutput withFieldDelimiter(Character fieldDelimiter) {
      this.setFieldDelimiter(fieldDelimiter);
      return this;
   }

   public Character getQuoteCharacter() {
      return this.stringToChar(this.quoteCharacter);
   }

   public String getQuoteCharacterAsString() {
      return this.quoteCharacter;
   }

   public void setQuoteCharacter(String quoteCharacter) {
      this.validateNotEmpty(quoteCharacter, "quoteCharacter");
      this.quoteCharacter = quoteCharacter;
   }

   public CSVOutput withQuoteCharacter(String quoteCharacter) {
      this.setQuoteCharacter(quoteCharacter);
      return this;
   }

   public void setQuoteCharacter(Character quoteCharacter) {
      this.setQuoteCharacter(this.charToString(quoteCharacter));
   }

   public CSVOutput withQuoteCharacter(Character quoteCharacter) {
      this.setQuoteCharacter(quoteCharacter);
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

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getQuoteFields() != null) {
         sb.append("QuoteFields: ").append(this.getQuoteFields()).append(",");
      }

      if (this.getQuoteEscapeCharacter() != null) {
         sb.append("QuoteEscapeCharacter: ").append(this.getQuoteEscapeCharacterAsString()).append(",");
      }

      if (this.getRecordDelimiter() != null) {
         sb.append("RecordDelimiter: ").append(this.getRecordDelimiterAsString()).append(",");
      }

      if (this.getFieldDelimiter() != null) {
         sb.append("FieldDelimiter: ").append(this.getFieldDelimiterAsString()).append(",");
      }

      if (this.getQuoteCharacter() != null) {
         sb.append("QuoteCharacter: ").append(this.getQuoteCharacterAsString());
      }

      sb.append("}");
      return sb.toString();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof CSVOutput) {
         CSVOutput other = (CSVOutput)obj;
         if (other.getQuoteEscapeCharacterAsString() == null ^ this.getQuoteEscapeCharacterAsString() == null) {
            return false;
         } else if (other.getQuoteEscapeCharacterAsString() != null && !other.getQuoteEscapeCharacterAsString().equals(this.getQuoteEscapeCharacterAsString())) {
            return false;
         } else if (other.getQuoteFields() == null ^ this.getQuoteFields() == null) {
            return false;
         } else if (other.getQuoteFields() != null && !other.getQuoteFields().equals(this.getQuoteFields())) {
            return false;
         } else if (other.getRecordDelimiterAsString() == null ^ this.getRecordDelimiterAsString() == null) {
            return false;
         } else if (other.getRecordDelimiterAsString() != null && !other.getRecordDelimiterAsString().equals(this.getRecordDelimiterAsString())) {
            return false;
         } else if (other.getFieldDelimiterAsString() == null ^ this.getFieldDelimiterAsString() == null) {
            return false;
         } else if (other.getFieldDelimiterAsString() != null && !other.getFieldDelimiterAsString().equals(this.getFieldDelimiterAsString())) {
            return false;
         } else if (other.getQuoteCharacterAsString() == null ^ this.getQuoteCharacterAsString() == null) {
            return false;
         } else {
            return other.getQuoteCharacterAsString() == null || other.getQuoteCharacterAsString().equals(this.getQuoteCharacterAsString());
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getQuoteFields() == null ? 0 : this.getQuoteFields().hashCode());
      hashCode = 31 * hashCode + (this.getQuoteEscapeCharacterAsString() == null ? 0 : this.getQuoteEscapeCharacterAsString().hashCode());
      hashCode = 31 * hashCode + (this.getRecordDelimiterAsString() == null ? 0 : this.getRecordDelimiterAsString().hashCode());
      hashCode = 31 * hashCode + (this.getFieldDelimiterAsString() == null ? 0 : this.getFieldDelimiterAsString().hashCode());
      return 31 * hashCode + (this.getQuoteCharacterAsString() != null ? this.getQuoteCharacterAsString().hashCode() : 0);
   }

   @Override
   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
