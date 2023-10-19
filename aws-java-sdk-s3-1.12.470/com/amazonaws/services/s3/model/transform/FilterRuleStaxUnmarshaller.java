package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.FilterRule;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller;
import javax.xml.stream.events.XMLEvent;

class FilterRuleStaxUnmarshaller implements Unmarshaller<FilterRule, StaxUnmarshallerContext> {
   private static final FilterRuleStaxUnmarshaller instance = new FilterRuleStaxUnmarshaller();

   public static FilterRuleStaxUnmarshaller getInstance() {
      return instance;
   }

   private FilterRuleStaxUnmarshaller() {
   }

   public FilterRule unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      FilterRule filter = new FilterRule();

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return filter;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return filter;
            }
         } else if (context.testExpression("Name", targetDepth)) {
            filter.setName(StringStaxUnmarshaller.getInstance().unmarshall(context));
         } else if (context.testExpression("Value", targetDepth)) {
            filter.setValue(StringStaxUnmarshaller.getInstance().unmarshall(context));
         }
      }
   }
}
