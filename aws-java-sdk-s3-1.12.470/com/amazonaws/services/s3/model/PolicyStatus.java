package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class PolicyStatus implements Serializable, Cloneable {
   private Boolean isPublic;

   public Boolean getIsPublic() {
      return this.isPublic;
   }

   public void setIsPublic(Boolean isPublic) {
      this.isPublic = isPublic;
   }

   public PolicyStatus withIsPublic(Boolean isPublic) {
      this.setIsPublic(isPublic);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         PolicyStatus that = (PolicyStatus)o;
         return this.isPublic != null ? this.isPublic.equals(that.isPublic) : that.isPublic == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.isPublic != null ? this.isPublic.hashCode() : 0;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getIsPublic() != null) {
         sb.append("IsPublic: ").append(this.getIsPublic()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   public PolicyStatus clone() {
      try {
         return (PolicyStatus)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
