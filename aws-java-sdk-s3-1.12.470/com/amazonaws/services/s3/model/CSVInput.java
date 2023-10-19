package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class CSVInput implements Serializable, Cloneable {
   private String fileHeaderInfo;
   private String comments;
   private String quoteEscapeCharacter;
   private String recordDelimiter;
   private String fieldDelimiter;
   private String quoteCharacter;
   private Boolean allowQuotedRecordDelimiter;

   public String getFileHeaderInfo() {
      return this.fileHeaderInfo;
   }

   public void setFileHeaderInfo(String fileHeaderInfo) {
      this.fileHeaderInfo = fileHeaderInfo;
   }

   public CSVInput withFileHeaderInfo(String fileHeaderInfo) {
      this.setFileHeaderInfo(fileHeaderInfo);
      return this;
   }

   public void setFileHeaderInfo(FileHeaderInfo fileHeaderInfo) {
      this.setFileHeaderInfo(fileHeaderInfo == null ? null : fileHeaderInfo.toString());
   }

   public CSVInput withFileHeaderInfo(FileHeaderInfo fileHeaderInfo) {
      this.setFileHeaderInfo(fileHeaderInfo);
      return this;
   }

   public Character getComments() {
      return this.stringToChar(this.comments);
   }

   public String getCommentsAsString() {
      return this.comments;
   }

   public void setComments(String comments) {
      this.validateNotEmpty(comments, "comments");
      this.comments = comments;
   }

   public CSVInput withComments(String comments) {
      this.setComments(comments);
      return this;
   }

   public void setComments(Character comments) {
      this.setComments(this.charToString(comments));
   }

   public CSVInput withComments(Character comments) {
      this.setComments(comments);
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

   public CSVInput withQuoteEscapeCharacter(String quoteEscapeCharacter) {
      this.setQuoteEscapeCharacter(quoteEscapeCharacter);
      return this;
   }

   public void setQuoteEscapeCharacter(Character quoteEscapeCharacter) {
      this.setQuoteEscapeCharacter(this.charToString(quoteEscapeCharacter));
   }

   public CSVInput withQuoteEscapeCharacter(Character quoteEscapeCharacter) {
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

   public CSVInput withRecordDelimiter(String recordDelimiter) {
      this.setRecordDelimiter(recordDelimiter);
      return this;
   }

   public void setRecordDelimiter(Character recordDelimiter) {
      this.setRecordDelimiter(this.charToString(recordDelimiter));
   }

   public CSVInput withRecordDelimiter(Character recordDelimiter) {
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

   public CSVInput withFieldDelimiter(String fieldDelimiter) {
      this.setFieldDelimiter(fieldDelimiter);
      return this;
   }

   public void setFieldDelimiter(Character fieldDelimiter) {
      this.setFieldDelimiter(this.charToString(fieldDelimiter));
   }

   public CSVInput withFieldDelimiter(Character fieldDelimiter) {
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

   public CSVInput withQuoteCharacter(String quoteCharacter) {
      this.setQuoteCharacter(quoteCharacter);
      return this;
   }

   public void setQuoteCharacter(Character quoteCharacter) {
      this.setQuoteCharacter(this.charToString(quoteCharacter));
   }

   public CSVInput withQuoteCharacter(Character quoteCharacter) {
      this.setQuoteCharacter(quoteCharacter);
      return this;
   }

   public Boolean getAllowQuotedRecordDelimiter() {
      return this.allowQuotedRecordDelimiter;
   }

   public void setAllowQuotedRecordDelimiter(Boolean allowQuotedRecordDelimiter) {
      this.allowQuotedRecordDelimiter = allowQuotedRecordDelimiter;
   }

   public CSVInput withAllowQuotedRecordDelimiter(Boolean allowQuotedRecordDelimiter) {
      this.setAllowQuotedRecordDelimiter(allowQuotedRecordDelimiter);
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
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof CSVInput) {
         CSVInput other = (CSVInput)obj;
         if (other.getFileHeaderInfo() == null ^ this.getFileHeaderInfo() == null) {
            return false;
         } else if (other.getFileHeaderInfo() != null && !other.getFileHeaderInfo().equals(this.getFileHeaderInfo())) {
            return false;
         } else if (other.getQuoteEscapeCharacterAsString() == null ^ this.getQuoteEscapeCharacterAsString() == null) {
            return false;
         } else if (other.getQuoteEscapeCharacterAsString() != null && !other.getQuoteEscapeCharacterAsString().equals(this.getQuoteEscapeCharacterAsString())) {
            return false;
         } else if (other.getCommentsAsString() == null ^ this.getCommentsAsString() == null) {
            return false;
         } else if (other.getCommentsAsString() != null && !other.getCommentsAsString().equals(this.getCommentsAsString())) {
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
         } else if (other.getQuoteCharacterAsString() != null && !other.getQuoteCharacterAsString().equals(this.getQuoteCharacterAsString())) {
            return false;
         } else {
            return other.getAllowQuotedRecordDelimiter() == null || other.getAllowQuotedRecordDelimiter().equals(this.getAllowQuotedRecordDelimiter());
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getFileHeaderInfo() == null ? 0 : this.getFileHeaderInfo().hashCode());
      hashCode = 31 * hashCode + (this.getCommentsAsString() == null ? 0 : this.getCommentsAsString().hashCode());
      hashCode = 31 * hashCode + (this.getQuoteEscapeCharacterAsString() == null ? 0 : this.getQuoteEscapeCharacterAsString().hashCode());
      hashCode = 31 * hashCode + (this.getRecordDelimiterAsString() == null ? 0 : this.getRecordDelimiterAsString().hashCode());
      hashCode = 31 * hashCode + (this.getFieldDelimiterAsString() == null ? 0 : this.getFieldDelimiterAsString().hashCode());
      hashCode = 31 * hashCode + (this.getQuoteCharacterAsString() != null ? this.getQuoteCharacterAsString().hashCode() : 0);
      return 31 * hashCode + (this.getAllowQuotedRecordDelimiter() == null ? 0 : this.getAllowQuotedRecordDelimiter().hashCode());
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getFileHeaderInfo() != null) {
         sb.append("FileHeaderInfo: ").append(this.getFileHeaderInfo()).append(",");
      }

      if (this.getCommentsAsString() != null) {
         sb.append("Comments: ").append(this.getCommentsAsString()).append(",");
      }

      if (this.getQuoteEscapeCharacterAsString() != null) {
         sb.append("QuoteEscapeCharacter: ").append(this.getQuoteEscapeCharacterAsString()).append(",");
      }

      if (this.getRecordDelimiterAsString() != null) {
         sb.append("RecordDelimiter: ").append(this.getRecordDelimiterAsString()).append(",");
      }

      if (this.getFieldDelimiterAsString() != null) {
         sb.append("FieldDelimiter: ").append(this.getFieldDelimiterAsString()).append(",");
      }

      if (this.getQuoteCharacterAsString() != null) {
         sb.append("QuoteCharacter: ").append(this.getQuoteCharacterAsString());
      }

      if (this.getAllowQuotedRecordDelimiter() != null) {
         sb.append("AllowQuotedRecordDelimiter: ").append(this.getAllowQuotedRecordDelimiter());
      }

      sb.append("}");
      return sb.toString();
   }

   public CSVInput clone() {
      try {
         return (CSVInput)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
