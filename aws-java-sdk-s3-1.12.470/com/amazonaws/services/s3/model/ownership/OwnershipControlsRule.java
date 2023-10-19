package com.amazonaws.services.s3.model.ownership;

import java.io.Serializable;

public class OwnershipControlsRule implements Serializable {
   private String objectOwnership;

   public String getOwnership() {
      return this.objectOwnership;
   }

   public void setOwnership(String objectOwnership) {
      this.objectOwnership = objectOwnership;
   }

   public void setOwnership(ObjectOwnership objectOwnership) {
      this.objectOwnership = objectOwnership.toString();
   }

   public OwnershipControlsRule withOwnership(String objectOwnership) {
      this.setOwnership(objectOwnership);
      return this;
   }

   public OwnershipControlsRule withOwnership(ObjectOwnership objectOwnership) {
      this.setOwnership(objectOwnership);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         OwnershipControlsRule that = (OwnershipControlsRule)o;
         return this.objectOwnership != null ? this.objectOwnership.equals(that.objectOwnership) : that.objectOwnership == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.objectOwnership != null ? this.objectOwnership.hashCode() : 0;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getOwnership() != null) {
         sb.append("Ownership: ").append(this.getOwnership()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }
}
