package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ServerSideEncryptionByDefault implements Serializable, Cloneable {
   private String sseAlgorithm;
   private String kmsMasterKeyID;

   public String getSSEAlgorithm() {
      return this.sseAlgorithm;
   }

   public void setSSEAlgorithm(String sseAlgorithm) {
      this.sseAlgorithm = sseAlgorithm;
   }

   public ServerSideEncryptionByDefault withSSEAlgorithm(String sseAlgorithm) {
      this.setSSEAlgorithm(sseAlgorithm);
      return this;
   }

   public ServerSideEncryptionByDefault withSSEAlgorithm(SSEAlgorithm sseAlgorithm) {
      this.setSSEAlgorithm(sseAlgorithm == null ? null : sseAlgorithm.toString());
      return this;
   }

   public String getKMSMasterKeyID() {
      return this.kmsMasterKeyID;
   }

   public void setKMSMasterKeyID(String kmsMasterKeyID) {
      this.kmsMasterKeyID = kmsMasterKeyID;
   }

   public ServerSideEncryptionByDefault withKMSMasterKeyID(String kmsMasterKeyID) {
      this.setKMSMasterKeyID(kmsMasterKeyID);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getSSEAlgorithm() != null) {
         sb.append("SSEAlgorithm: ").append(this.getSSEAlgorithm()).append(",");
      }

      if (this.getKMSMasterKeyID() != null) {
         sb.append("KMSMasterKeyID: ").append(this.getKMSMasterKeyID()).append(",");
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
      } else if (!(obj instanceof ServerSideEncryptionByDefault)) {
         return false;
      } else {
         ServerSideEncryptionByDefault other = (ServerSideEncryptionByDefault)obj;
         if (other.getSSEAlgorithm() == null ^ this.getSSEAlgorithm() == null) {
            return false;
         } else if (other.getSSEAlgorithm() != null && !other.getSSEAlgorithm().equals(this.getSSEAlgorithm())) {
            return false;
         } else if (other.getKMSMasterKeyID() == null ^ this.getKMSMasterKeyID() == null) {
            return false;
         } else {
            return other.getKMSMasterKeyID() == null || other.getKMSMasterKeyID().equals(this.getKMSMasterKeyID());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getSSEAlgorithm() == null ? 0 : this.getSSEAlgorithm().hashCode());
      return 31 * hashCode + (this.getKMSMasterKeyID() == null ? 0 : this.getKMSMasterKeyID().hashCode());
   }

   public ServerSideEncryptionByDefault clone() {
      try {
         return (ServerSideEncryptionByDefault)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
