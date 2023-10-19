package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.QueueConfiguration;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller;

class QueueConfigurationStaxUnmarshaller extends NotificationConfigurationStaxUnmarshaller<QueueConfiguration> {
   private static final QueueConfigurationStaxUnmarshaller instance = new QueueConfigurationStaxUnmarshaller();

   public static QueueConfigurationStaxUnmarshaller getInstance() {
      return instance;
   }

   private QueueConfigurationStaxUnmarshaller() {
   }

   protected boolean handleXmlEvent(QueueConfiguration queueConfig, StaxUnmarshallerContext context, int targetDepth) throws Exception {
      if (context.testExpression("Queue", targetDepth)) {
         queueConfig.setQueueARN(StringStaxUnmarshaller.getInstance().unmarshall(context));
         return true;
      } else {
         return false;
      }
   }

   protected QueueConfiguration createConfiguration() {
      return new QueueConfiguration();
   }
}
