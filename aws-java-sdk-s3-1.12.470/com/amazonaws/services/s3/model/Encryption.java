package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class Encryption implements Serializable, Cloneable {
   private String encryptionType;
   private String kmsKeyId;
   private String kmsContext;

   public String getEncryptionType() {
      return this.encryptionType;
   }

   public void setEncryptionType(String encryptionType) {
      this.encryptionType = encryptionType;
   }

   public Encryption withEncryptionType(String encryptionType) {
      this.setEncryptionType(encryptionType);
      return this;
   }

   public Encryption withEncryptionType(SSEAlgorithm encryptionType) {
      this.setEncryptionType(encryptionType == null ? null : encryptionType.toString());
      return this;
   }

   public String getKmsKeyId() {
      return this.kmsKeyId;
   }

   public void setKmsKeyId(String kmsKeyId) {
      this.kmsKeyId = kmsKeyId;
   }

   public Encryption withKmsKeyId(String kmsKeyId) {
      this.setKmsKeyId(kmsKeyId);
      return this;
   }

   public String getKmsContext() {
      return this.kmsContext;
   }

   public void setKmsContext(String kmsContext) {
      this.kmsContext = kmsContext;
   }

   public Encryption withKmsContext(String kmsContext) {
      this.setKmsContext(kmsContext);
      return this;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof Encryption) {
         Encryption other = (Encryption)obj;
         if (other.getKmsContext() == null ^ this.getKmsContext() == null) {
            return false;
         } else if (other.getKmsContext() != null && !other.getKmsContext().equals(this.getKmsContext())) {
            return false;
         } else if (other.getKmsKeyId() == null ^ this.getKmsKeyId() == null) {
            return false;
         } else if (other.getKmsKeyId() != null && !other.getKmsKeyId().equals(this.getKmsKeyId())) {
            return false;
         } else if (other.getEncryptionType() == null ^ this.getEncryptionType() == null) {
            return false;
         } else {
            return other.getEncryptionType() == null || other.getEncryptionType().equals(this.getEncryptionType());
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getKmsKeyId() == null ? 0 : this.getKmsKeyId().hashCode());
      hashCode = 31 * hashCode + (this.getKmsContext() == null ? 0 : this.getKmsContext().hashCode());
      return 31 * hashCode + (this.getEncryptionType() == null ? 0 : this.getEncryptionType().hashCode());
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getKmsContext() != null) {
         sb.append("KmsContext: ").append(this.getKmsContext()).append(",");
      }

      if (this.getKmsKeyId() != null) {
         sb.append("KmsKeyId: ").append(this.getKmsKeyId()).append(",");
      }

      if (this.getEncryptionType() != null) {
         sb.append("EncryptionType: ").append(this.getEncryptionType());
      }

      sb.append("}");
      return sb.toString();
   }

   public Encryption clone() {
      try {
         return (Encryption)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
