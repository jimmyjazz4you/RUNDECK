package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ReplicationTimeValue implements Serializable {
   private Integer minutes;

   public Integer getMinutes() {
      return this.minutes;
   }

   public ReplicationTimeValue withMinutes(Integer minutes) {
      this.minutes = minutes;
      return this;
   }

   public void setMinutes(Integer minutes) {
      this.withMinutes(minutes);
   }
}
