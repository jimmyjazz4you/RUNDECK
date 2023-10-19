package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.EnumSet;

public class QueueConfiguration extends NotificationConfiguration implements Serializable {
   private String queueARN;

   public QueueConfiguration() {
   }

   public QueueConfiguration(String queueARN, EnumSet<S3Event> events) {
      super(events);
      this.queueARN = queueARN;
   }

   public QueueConfiguration(String queueARN, String... events) {
      super(events);
      this.queueARN = queueARN;
   }

   public String getQueueARN() {
      return this.queueARN;
   }

   public void setQueueARN(String queueARN) {
      this.queueARN = queueARN;
   }

   public QueueConfiguration withQueueARN(String queueARN) {
      this.setQueueARN(queueARN);
      return this;
   }
}
