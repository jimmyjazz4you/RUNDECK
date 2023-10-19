package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class EncryptionConfiguration implements Serializable, Cloneable {
   private String replicaKmsKeyID;

   public String getReplicaKmsKeyID() {
      return this.replicaKmsKeyID;
   }

   public void setReplicaKmsKeyID(String replicaKmsKeyID) {
      this.replicaKmsKeyID = replicaKmsKeyID;
   }

   public EncryptionConfiguration withReplicaKmsKeyID(String replicaKmsKeyID) {
      this.setReplicaKmsKeyID(replicaKmsKeyID);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getReplicaKmsKeyID() != null) {
         sb.append("ReplicaKmsKeyID: ").append(this.getReplicaKmsKeyID()).append(",");
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
      } else if (!(obj instanceof EncryptionConfiguration)) {
         return false;
      } else {
         EncryptionConfiguration other = (EncryptionConfiguration)obj;
         if (other.getReplicaKmsKeyID() == null ^ this.getReplicaKmsKeyID() == null) {
            return false;
         } else {
            return other.getReplicaKmsKeyID() == null || other.getReplicaKmsKeyID().equals(this.getReplicaKmsKeyID());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getReplicaKmsKeyID() == null ? 0 : this.getReplicaKmsKeyID().hashCode());
   }

   public EncryptionConfiguration clone() {
      try {
         return (EncryptionConfiguration)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
