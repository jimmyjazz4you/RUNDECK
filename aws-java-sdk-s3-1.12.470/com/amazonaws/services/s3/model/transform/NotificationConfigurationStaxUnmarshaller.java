package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.NotificationConfiguration;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javax.xml.stream.events.XMLEvent;

abstract class NotificationConfigurationStaxUnmarshaller<T extends NotificationConfiguration>
   implements Unmarshaller<Entry<String, NotificationConfiguration>, StaxUnmarshallerContext> {
   public Entry<String, NotificationConfiguration> unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      T topicConfig = this.createConfiguration();
      String id = null;

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return new SimpleEntry<>(id, topicConfig);
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return new SimpleEntry<>(id, topicConfig);
            }
         } else if (!this.handleXmlEvent(topicConfig, context, targetDepth)) {
            if (context.testExpression("Id", targetDepth)) {
               id = StringStaxUnmarshaller.getInstance().unmarshall(context);
            } else if (context.testExpression("Event", targetDepth)) {
               topicConfig.addEvent(StringStaxUnmarshaller.getInstance().unmarshall(context));
            } else if (context.testExpression("Filter", targetDepth)) {
               topicConfig.setFilter(FilterStaxUnmarshaller.getInstance().unmarshall(context));
            }
         }
      }
   }

   protected abstract T createConfiguration();

   protected abstract boolean handleXmlEvent(T var1, StaxUnmarshallerContext var2, int var3) throws Exception;
}
