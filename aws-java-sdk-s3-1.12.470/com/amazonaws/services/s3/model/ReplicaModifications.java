package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ReplicaModifications implements Serializable, Cloneable {
   private String status;

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public ReplicaModifications withStatus(String status) {
      this.setStatus(status);
      return this;
   }

   public ReplicaModifications withStatus(ReplicaModificationsStatus status) {
      this.setStatus(status == null ? null : status.toString());
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getStatus() != null) {
         sb.append("Status: ").append(this.getStatus()).append(",");
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
      } else if (!(obj instanceof ReplicaModifications)) {
         return false;
      } else {
         ReplicaModifications other = (ReplicaModifications)obj;
         if (other.getStatus() == null ^ this.getStatus() == null) {
            return false;
         } else {
            return other.getStatus() == null || other.getStatus().equals(this.getStatus());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getStatus() == null ? 0 : this.getStatus().hashCode());
   }

   public ReplicaModifications clone() {
      try {
         return (ReplicaModifications)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
