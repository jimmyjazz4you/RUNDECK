package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class InputSerialization implements Serializable, Cloneable {
   private CSVInput csv;
   private JSONInput json;
   private ParquetInput parquet;
   private String compressionType;

   public CSVInput getCsv() {
      return this.csv;
   }

   public void setCsv(CSVInput csv) {
      this.csv = csv;
   }

   public InputSerialization withCsv(CSVInput csvInput) {
      this.setCsv(csvInput);
      return this;
   }

   public JSONInput getJson() {
      return this.json;
   }

   public void setJson(JSONInput json) {
      this.json = json;
   }

   public InputSerialization withJson(JSONInput json) {
      this.setJson(json);
      return this;
   }

   public ParquetInput getParquet() {
      return this.parquet;
   }

   public void setParquet(ParquetInput parquet) {
      this.parquet = parquet;
   }

   public InputSerialization withParquet(ParquetInput parquet) {
      this.setParquet(parquet);
      return this;
   }

   public String getCompressionType() {
      return this.compressionType;
   }

   public void setCompressionType(String compressionType) {
      this.compressionType = compressionType;
   }

   public void setCompressionType(CompressionType compressionType) {
      this.setCompressionType(compressionType == null ? null : compressionType.toString());
   }

   public InputSerialization withCompressionType(String compressionType) {
      this.setCompressionType(compressionType);
      return this;
   }

   public InputSerialization withCompressionType(CompressionType compressionType) {
      this.setCompressionType(compressionType);
      return this;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof InputSerialization) {
         InputSerialization other = (InputSerialization)obj;
         if (other.getCsv() == null ^ this.getCsv() == null) {
            return false;
         } else if (other.getCsv() != null && !other.getCsv().equals(this.getCsv())) {
            return false;
         } else if (other.getJson() == null ^ this.getJson() == null) {
            return false;
         } else if (other.getJson() != null && !other.getJson().equals(this.getJson())) {
            return false;
         } else if (other.getCompressionType() == null ^ this.getCompressionType() == null) {
            return false;
         } else {
            return other.getCompressionType() == null || other.getCompressionType().equals(this.getCompressionType());
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getCsv() == null ? 0 : this.getCsv().hashCode());
      hashCode = 31 * hashCode + (this.getJson() == null ? 0 : this.getJson().hashCode());
      return 31 * hashCode + (this.getCompressionType() == null ? 0 : this.getCompressionType().hashCode());
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getCsv() != null) {
         sb.append("Csv: ").append(this.getCsv());
      }

      if (this.getJson() != null) {
         sb.append("Json: ").append(this.getJson());
      }

      if (this.getCompressionType() != null) {
         sb.append("CompressionType: ").append(this.getCompressionType());
      }

      sb.append("}");
      return sb.toString();
   }

   public InputSerialization clone() {
      try {
         return (InputSerialization)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
