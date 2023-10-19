package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class BucketAccelerateConfiguration implements Serializable {
   private String status;

   public BucketAccelerateConfiguration(String status) {
      this.setStatus(status);
   }

   public BucketAccelerateConfiguration(BucketAccelerateStatus status) {
      this.setStatus(status);
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public void setStatus(BucketAccelerateStatus status) {
      this.setStatus(status.toString());
   }

   public BucketAccelerateConfiguration withStatus(String status) {
      this.setStatus(status);
      return this;
   }

   public BucketAccelerateConfiguration withStatus(BucketAccelerateStatus status) {
      this.setStatus(status);
      return this;
   }

   public boolean isAccelerateEnabled() {
      return BucketAccelerateStatus.Enabled.toString().equals(this.getStatus());
   }
}
