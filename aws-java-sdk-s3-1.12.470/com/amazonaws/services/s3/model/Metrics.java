package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class Metrics implements Serializable {
   private String status;
   private ReplicationTimeValue eventThreshold;

   public String getStatus() {
      return this.status;
   }

   public Metrics withStatus(MetricsStatus status) {
      this.status = status.toString();
      return this;
   }

   public Metrics withStatus(String status) {
      this.status = status;
      return this;
   }

   public void setStatus(MetricsStatus status) {
      this.withStatus(status);
   }

   public void setStatus(String status) {
      this.withStatus(status);
   }

   public ReplicationTimeValue getEventThreshold() {
      return this.eventThreshold;
   }

   public Metrics withEventThreshold(ReplicationTimeValue eventThreshold) {
      this.eventThreshold = eventThreshold;
      return this;
   }

   public void setEventThreshold(ReplicationTimeValue eventThreshold) {
      this.withEventThreshold(eventThreshold);
   }
}
