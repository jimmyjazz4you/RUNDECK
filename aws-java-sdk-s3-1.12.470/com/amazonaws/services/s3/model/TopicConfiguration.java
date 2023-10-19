package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.EnumSet;

public class TopicConfiguration extends NotificationConfiguration implements Serializable {
   private String topicARN;

   public TopicConfiguration() {
   }

   public TopicConfiguration(String topicARN, EnumSet<S3Event> events) {
      super(events);
      this.topicARN = topicARN;
   }

   public TopicConfiguration(String topicARN, String... events) {
      super(events);
      this.topicARN = topicARN;
   }

   public String getTopicARN() {
      return this.topicARN;
   }

   public void setTopicARN(String topicARN) {
      this.topicARN = topicARN;
   }

   public TopicConfiguration withTopicARN(String topicARN) {
      this.setTopicARN(topicARN);
      return this;
   }
}
