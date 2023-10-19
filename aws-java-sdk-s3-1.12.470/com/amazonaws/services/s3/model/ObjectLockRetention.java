package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Date;

public class ObjectLockRetention implements Serializable {
   private String mode;
   private Date retainUntilDate;

   public String getMode() {
      return this.mode;
   }

   public ObjectLockRetention withMode(String mode) {
      this.mode = mode;
      return this;
   }

   public ObjectLockRetention withMode(ObjectLockRetentionMode mode) {
      return this.withMode(mode.toString());
   }

   public void setMode(String mode) {
      this.withMode(mode);
   }

   public void setMode(ObjectLockRetentionMode mode) {
      this.setMode(mode.toString());
   }

   public Date getRetainUntilDate() {
      return this.retainUntilDate;
   }

   public ObjectLockRetention withRetainUntilDate(Date retainUntilDate) {
      this.retainUntilDate = retainUntilDate;
      return this;
   }

   public void setRetainUntilDate(Date retainUntilDate) {
      this.withRetainUntilDate(retainUntilDate);
   }
}
