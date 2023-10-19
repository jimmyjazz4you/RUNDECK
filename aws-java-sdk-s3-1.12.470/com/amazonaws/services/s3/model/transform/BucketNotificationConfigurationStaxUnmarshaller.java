package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.BucketNotificationConfiguration;
import com.amazonaws.services.s3.model.EventBridgeConfiguration;
import com.amazonaws.services.s3.model.NotificationConfiguration;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.util.XmlUtils;
import java.io.InputStream;
import java.util.Map.Entry;
import javax.xml.stream.events.XMLEvent;

public class BucketNotificationConfigurationStaxUnmarshaller implements Unmarshaller<BucketNotificationConfiguration, InputStream> {
   private static final BucketNotificationConfigurationStaxUnmarshaller instance = new BucketNotificationConfigurationStaxUnmarshaller();

   public static BucketNotificationConfigurationStaxUnmarshaller getInstance() {
      return instance;
   }

   private BucketNotificationConfigurationStaxUnmarshaller() {
   }

   public BucketNotificationConfiguration unmarshall(InputStream inputStream) throws Exception {
      StaxUnmarshallerContext context = new StaxUnmarshallerContext(XmlUtils.getXmlInputFactory().createXMLEventReader(inputStream));
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      BucketNotificationConfiguration config = new BucketNotificationConfiguration();

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return config;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return config;
            }
         } else if (context.testExpression("TopicConfiguration", targetDepth)) {
            Entry<String, NotificationConfiguration> entry = TopicConfigurationStaxUnmarshaller.getInstance().unmarshall(context);
            config.addConfiguration(entry.getKey(), entry.getValue());
         } else if (context.testExpression("QueueConfiguration", targetDepth)) {
            Entry<String, NotificationConfiguration> entry = QueueConfigurationStaxUnmarshaller.getInstance().unmarshall(context);
            config.addConfiguration(entry.getKey(), entry.getValue());
         } else if (context.testExpression("CloudFunctionConfiguration", targetDepth)) {
            Entry<String, NotificationConfiguration> entry = LambdaConfigurationStaxUnmarshaller.getInstance().unmarshall(context);
            config.addConfiguration(entry.getKey(), entry.getValue());
         } else if (context.testExpression("EventBridgeConfiguration", targetDepth)) {
            EventBridgeConfiguration eventBridgeConfig = EventBridgeConfigurationStaxUnmarshaller.getInstance().unmarshall(context);
            config.setEventBridgeConfiguration(eventBridgeConfig);
         }
      }
   }
}
