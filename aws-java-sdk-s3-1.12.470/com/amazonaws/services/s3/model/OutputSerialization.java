package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class OutputSerialization implements Serializable, Cloneable {
   private CSVOutput csv;
   private JSONOutput json;

   public CSVOutput getCsv() {
      return this.csv;
   }

   public void setCsv(CSVOutput csv) {
      this.csv = csv;
   }

   public OutputSerialization withCsv(CSVOutput csvOutput) {
      this.setCsv(csvOutput);
      return this;
   }

   public JSONOutput getJson() {
      return this.json;
   }

   public void setJson(JSONOutput json) {
      this.json = json;
   }

   public OutputSerialization withJson(JSONOutput json) {
      this.setJson(json);
      return this;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof OutputSerialization) {
         OutputSerialization other = (OutputSerialization)obj;
         if (other.getCsv() == null ^ this.getCsv() == null) {
            return false;
         } else if (other.getCsv() != null && !other.getCsv().equals(this.getCsv())) {
            return false;
         } else if (other.getJson() == null ^ this.getJson() == null) {
            return false;
         } else {
            return other.getJson() == null || other.getJson().equals(this.getJson());
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
      return 31 * hashCode + (this.getJson() == null ? 0 : this.getJson().hashCode());
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getCsv() != null) {
         sb.append("CSV: ").append(this.getCsv());
      }

      if (this.getJson() != null) {
         sb.append("JSON: ").append(this.getJson());
      }

      sb.append("}");
      return sb.toString();
   }

   public OutputSerialization clone() {
      try {
         return (OutputSerialization)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
