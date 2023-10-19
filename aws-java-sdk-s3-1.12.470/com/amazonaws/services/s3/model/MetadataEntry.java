package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class MetadataEntry implements Serializable, Cloneable {
   private String name;
   private String value;

   public MetadataEntry(String name, String value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public MetadataEntry withName(String key) {
      this.setName(key);
      return this;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public MetadataEntry withValue(String metadataValue) {
      this.setValue(metadataValue);
      return this;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof MetadataEntry) {
         MetadataEntry other = (MetadataEntry)obj;
         if (other.getName() == null ^ this.getName() == null) {
            return false;
         } else if (other.getName() != null && !other.getName().equals(this.getName())) {
            return false;
         } else if (other.getValue() == null ^ this.getValue() == null) {
            return false;
         } else {
            return other.getValue() == null || other.getValue().equals(this.getValue());
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getName() == null ? 0 : this.getName().hashCode());
      return 31 * hashCode + (this.getValue() == null ? 0 : this.getValue().hashCode());
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getName() != null) {
         sb.append("Name: ").append(this.getName()).append(",");
      }

      if (this.getValue() != null) {
         sb.append("Value: ").append(this.getValue()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   public MetadataEntry clone() {
      try {
         return (MetadataEntry)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
