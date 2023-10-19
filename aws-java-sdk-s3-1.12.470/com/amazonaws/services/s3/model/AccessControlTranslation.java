package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class AccessControlTranslation implements Serializable, Cloneable {
   private String owner;

   public String getOwner() {
      return this.owner;
   }

   public void setOwner(String owner) {
      this.owner = owner;
   }

   public AccessControlTranslation withOwner(String owner) {
      this.setOwner(owner);
      return this;
   }

   public AccessControlTranslation withOwner(OwnerOverride owner) {
      this.setOwner(owner == null ? null : owner.toString());
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getOwner() != null) {
         sb.append("Owner: ").append(this.getOwner()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof AccessControlTranslation)) {
         return false;
      } else {
         AccessControlTranslation other = (AccessControlTranslation)obj;
         if (other.getOwner() == null ^ this.getOwner() == null) {
            return false;
         } else {
            return other.getOwner() == null || other.getOwner().equals(this.getOwner());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getOwner() == null ? 0 : this.getOwner().hashCode());
   }

   public AccessControlTranslation clone() {
      try {
         return (AccessControlTranslation)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
