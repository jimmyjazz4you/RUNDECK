package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.S3KeyFilter;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import javax.xml.stream.events.XMLEvent;

class S3KeyFilterStaxUnmarshaller implements Unmarshaller<S3KeyFilter, StaxUnmarshallerContext> {
   private static final S3KeyFilterStaxUnmarshaller instance = new S3KeyFilterStaxUnmarshaller();

   public static S3KeyFilterStaxUnmarshaller getInstance() {
      return instance;
   }

   private S3KeyFilterStaxUnmarshaller() {
   }

   public S3KeyFilter unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      S3KeyFilter filter = new S3KeyFilter();

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return filter;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return filter;
            }
         } else if (context.testExpression("FilterRule", targetDepth)) {
            filter.addFilterRule(FilterRuleStaxUnmarshaller.getInstance().unmarshall(context));
         }
      }
   }
}
