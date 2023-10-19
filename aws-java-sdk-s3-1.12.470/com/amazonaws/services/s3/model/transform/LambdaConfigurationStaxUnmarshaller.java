package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.CloudFunctionConfiguration;
import com.amazonaws.services.s3.model.Filter;
import com.amazonaws.services.s3.model.LambdaConfiguration;
import com.amazonaws.services.s3.model.NotificationConfiguration;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javax.xml.stream.events.XMLEvent;

class LambdaConfigurationStaxUnmarshaller implements Unmarshaller<Entry<String, NotificationConfiguration>, StaxUnmarshallerContext> {
   private static final LambdaConfigurationStaxUnmarshaller instance = new LambdaConfigurationStaxUnmarshaller();

   public static LambdaConfigurationStaxUnmarshaller getInstance() {
      return instance;
   }

   private LambdaConfigurationStaxUnmarshaller() {
   }

   public Entry<String, NotificationConfiguration> unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      String id = null;
      List<String> events = new ArrayList<>();
      Filter filter = null;
      String functionArn = null;
      String invocationRole = null;

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return this.createLambdaConfig(id, events, functionArn, invocationRole, filter);
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return this.createLambdaConfig(id, events, functionArn, invocationRole, filter);
            }
         } else if (context.testExpression("Id", targetDepth)) {
            id = StringStaxUnmarshaller.getInstance().unmarshall(context);
         } else if (context.testExpression("Event", targetDepth)) {
            events.add(StringStaxUnmarshaller.getInstance().unmarshall(context));
         } else if (context.testExpression("Filter", targetDepth)) {
            filter = FilterStaxUnmarshaller.getInstance().unmarshall(context);
         } else if (context.testExpression("CloudFunction", targetDepth)) {
            functionArn = StringStaxUnmarshaller.getInstance().unmarshall(context);
         } else if (context.testExpression("InvocationRole", targetDepth)) {
            invocationRole = StringStaxUnmarshaller.getInstance().unmarshall(context);
         }
      }
   }

   private Entry<String, NotificationConfiguration> createLambdaConfig(String id, List<String> events, String functionArn, String invocationRole, Filter filter) {
      NotificationConfiguration config;
      if (invocationRole == null) {
         config = new LambdaConfiguration(functionArn, events.toArray(new String[0]));
      } else {
         config = new CloudFunctionConfiguration(invocationRole, functionArn, events.toArray(new String[0]));
      }

      return new SimpleEntry<>(id, config.withFilter(filter));
   }
}
