package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ServerSideEncryptionRule implements Serializable, Cloneable {
   private ServerSideEncryptionByDefault applyServerSideEncryptionByDefault;
   private Boolean bucketKeyEnabled;

   public ServerSideEncryptionByDefault getApplyServerSideEncryptionByDefault() {
      return this.applyServerSideEncryptionByDefault;
   }

   public void setApplyServerSideEncryptionByDefault(ServerSideEncryptionByDefault applyServerSideEncryptionByDefault) {
      this.applyServerSideEncryptionByDefault = applyServerSideEncryptionByDefault;
   }

   public ServerSideEncryptionRule withApplyServerSideEncryptionByDefault(ServerSideEncryptionByDefault applyServerSideEncryptionByDefault) {
      this.setApplyServerSideEncryptionByDefault(applyServerSideEncryptionByDefault);
      return this;
   }

   public Boolean getBucketKeyEnabled() {
      return this.bucketKeyEnabled;
   }

   public void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.bucketKeyEnabled = bucketKeyEnabled;
   }

   public ServerSideEncryptionRule withBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.setBucketKeyEnabled(bucketKeyEnabled);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getApplyServerSideEncryptionByDefault() != null) {
         sb.append("ApplyServerSideEncryptionByDefault: ").append(this.getApplyServerSideEncryptionByDefault()).append(",");
         sb.append("BucketKeyEnabled: ").append(this.getBucketKeyEnabled()).append(",");
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
      } else if (!(obj instanceof ServerSideEncryptionRule)) {
         return false;
      } else {
         ServerSideEncryptionRule other = (ServerSideEncryptionRule)obj;
         if (other.getApplyServerSideEncryptionByDefault() == null ^ this.getApplyServerSideEncryptionByDefault() == null) {
            return false;
         } else if (other.getApplyServerSideEncryptionByDefault() != null
            && !other.getApplyServerSideEncryptionByDefault().equals(this.getApplyServerSideEncryptionByDefault())) {
            return false;
         } else if (other.getBucketKeyEnabled() == null ^ this.getBucketKeyEnabled() == null) {
            return false;
         } else {
            return other.getBucketKeyEnabled() == null || other.getBucketKeyEnabled() == this.getBucketKeyEnabled();
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getApplyServerSideEncryptionByDefault() == null ? 0 : this.getApplyServerSideEncryptionByDefault().hashCode());
      return 31 * hashCode + (this.getBucketKeyEnabled() == null ? 0 : (this.getBucketKeyEnabled() ? 1 : 2));
   }

   public ServerSideEncryptionRule clone() {
      try {
         return (ServerSideEncryptionRule)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
