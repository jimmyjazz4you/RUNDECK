package com.amazonaws.services.s3.model.inventory;

import java.io.Serializable;

public class ServerSideEncryptionKMS implements InventoryEncryption, Serializable, Cloneable {
   private String keyId;

   public String getKeyId() {
      return this.keyId;
   }

   public void setKeyId(String keyId) {
      this.keyId = keyId;
   }

   public ServerSideEncryptionKMS withKeyId(String keyId) {
      this.setKeyId(keyId);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getKeyId() != null) {
         sb.append("KeyId: ").append(this.getKeyId()).append(",");
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
      } else if (!(obj instanceof ServerSideEncryptionKMS)) {
         return false;
      } else {
         ServerSideEncryptionKMS other = (ServerSideEncryptionKMS)obj;
         if (other.getKeyId() == null ^ this.getKeyId() == null) {
            return false;
         } else {
            return other.getKeyId() == null || other.getKeyId().equals(this.getKeyId());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getKeyId() == null ? 0 : this.getKeyId().hashCode());
   }

   public ServerSideEncryptionKMS clone() {
      try {
         return (ServerSideEncryptionKMS)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
