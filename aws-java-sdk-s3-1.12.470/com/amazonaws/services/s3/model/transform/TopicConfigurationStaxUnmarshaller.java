package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.TopicConfiguration;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller;

class TopicConfigurationStaxUnmarshaller extends NotificationConfigurationStaxUnmarshaller<TopicConfiguration> {
   private static final TopicConfigurationStaxUnmarshaller instance = new TopicConfigurationStaxUnmarshaller();

   public static TopicConfigurationStaxUnmarshaller getInstance() {
      return instance;
   }

   private TopicConfigurationStaxUnmarshaller() {
   }

   protected boolean handleXmlEvent(TopicConfiguration topicConfig, StaxUnmarshallerContext context, int targetDepth) throws Exception {
      if (context.testExpression("Topic", targetDepth)) {
         topicConfig.setTopicARN(StringStaxUnmarshaller.getInstance().unmarshall(context));
         return true;
      } else {
         return false;
      }
   }

   protected TopicConfiguration createConfiguration() {
      return new TopicConfiguration();
   }
}
