package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ReplicationTime implements Serializable {
   private String status;
   private ReplicationTimeValue time;

   public String getStatus() {
      return this.status;
   }

   public ReplicationTime withStatus(ReplicationTimeStatus status) {
      this.status = status.toString();
      return this;
   }

   public ReplicationTime withStatus(String status) {
      this.status = status;
      return this;
   }

   public void setStatus(ReplicationTimeStatus status) {
      this.withStatus(status);
   }

   public void setStatus(String status) {
      this.withStatus(status);
   }

   public ReplicationTimeValue getTime() {
      return this.time;
   }

   public ReplicationTime withTime(ReplicationTimeValue time) {
      this.time = time;
      return this;
   }

   public void setTime(ReplicationTimeValue time) {
      this.withTime(time);
   }
}
