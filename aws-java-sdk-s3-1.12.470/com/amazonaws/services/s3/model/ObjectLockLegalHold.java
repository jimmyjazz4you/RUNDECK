package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ObjectLockLegalHold implements Serializable {
   private String status;

   public String getStatus() {
      return this.status;
   }

   public ObjectLockLegalHold withStatus(String status) {
      this.status = status;
      return this;
   }

   public ObjectLockLegalHold withStatus(ObjectLockLegalHoldStatus status) {
      this.status = status.toString();
      return this;
   }

   public void setStatus(String status) {
      this.withStatus(status);
   }

   public void setStatus(ObjectLockLegalHoldStatus status) {
      this.setStatus(status.toString());
   }
}
