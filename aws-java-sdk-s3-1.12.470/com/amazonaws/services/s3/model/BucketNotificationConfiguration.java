package com.amazonaws.services.s3.model;

import com.amazonaws.util.json.Jackson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

public class BucketNotificationConfiguration implements Serializable {
   private Map<String, NotificationConfiguration> configurations = null;
   private EventBridgeConfiguration eventBridgeConfiguration;

   public BucketNotificationConfiguration() {
      this.configurations = new HashMap<>();
   }

   public BucketNotificationConfiguration(String name, NotificationConfiguration notificationConfiguration) {
      this.configurations = new HashMap<>();
      this.addConfiguration(name, notificationConfiguration);
   }

   public BucketNotificationConfiguration withNotificationConfiguration(Map<String, NotificationConfiguration> notificationConfiguration) {
      this.configurations.clear();
      this.configurations.putAll(notificationConfiguration);
      return this;
   }

   public BucketNotificationConfiguration addConfiguration(String name, NotificationConfiguration notificationConfiguration) {
      this.configurations.put(name, notificationConfiguration);
      return this;
   }

   public Map<String, NotificationConfiguration> getConfigurations() {
      return this.configurations;
   }

   public void setConfigurations(Map<String, NotificationConfiguration> configurations) {
      this.configurations = configurations;
   }

   public NotificationConfiguration getConfigurationByName(String name) {
      return this.configurations.get(name);
   }

   public NotificationConfiguration removeConfiguration(String name) {
      return this.configurations.remove(name);
   }

   /** @deprecated */
   public BucketNotificationConfiguration(Collection<BucketNotificationConfiguration.TopicConfiguration> topicConfigurations) {
      this.configurations = new HashMap<>();
      if (topicConfigurations != null) {
         for(BucketNotificationConfiguration.TopicConfiguration config : topicConfigurations) {
            this.addConfiguration(UUID.randomUUID().toString(), config);
         }
      }
   }

   /** @deprecated */
   public BucketNotificationConfiguration withTopicConfigurations(BucketNotificationConfiguration.TopicConfiguration... topicConfigurations) {
      this.setTopicConfigurations(Arrays.asList(topicConfigurations));
      return this;
   }

   /** @deprecated */
   public void setTopicConfigurations(Collection<BucketNotificationConfiguration.TopicConfiguration> topicConfigurations) {
      this.configurations.clear();
      if (topicConfigurations != null) {
         for(BucketNotificationConfiguration.TopicConfiguration topicConfiguration : topicConfigurations) {
            this.addConfiguration(UUID.randomUUID().toString(), topicConfiguration);
         }
      }
   }

   /** @deprecated */
   public List<BucketNotificationConfiguration.TopicConfiguration> getTopicConfigurations() {
      List<BucketNotificationConfiguration.TopicConfiguration> topicConfigs = new ArrayList<>();

      for(Entry<String, NotificationConfiguration> entry : this.configurations.entrySet()) {
         if (entry.getValue() instanceof BucketNotificationConfiguration.TopicConfiguration) {
            topicConfigs.add((BucketNotificationConfiguration.TopicConfiguration)entry.getValue());
         }
      }

      return topicConfigs;
   }

   public EventBridgeConfiguration getEventBridgeConfiguration() {
      return this.eventBridgeConfiguration;
   }

   public void setEventBridgeConfiguration(EventBridgeConfiguration eventBridgeConfiguration) {
      this.eventBridgeConfiguration = eventBridgeConfiguration;
   }

   public BucketNotificationConfiguration withEventBridgeConfiguration(EventBridgeConfiguration eventBridgeConfiguration) {
      this.eventBridgeConfiguration = eventBridgeConfiguration;
      return this;
   }

   @Override
   public String toString() {
      return Jackson.toJsonString(this.getConfigurations());
   }

   @Deprecated
   public static class TopicConfiguration extends com.amazonaws.services.s3.model.TopicConfiguration {
      public TopicConfiguration(String topic, String event) {
         super(topic, event);
      }

      public String getTopic() {
         return this.getTopicARN();
      }

      /** @deprecated */
      public String getEvent() {
         Set<String> events = this.getEvents();
         String[] eventArray = events.toArray(new String[events.size()]);
         return eventArray[0];
      }

      @Override
      public String toString() {
         return Jackson.toJsonString(this);
      }
   }
}
