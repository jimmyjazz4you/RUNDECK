package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.Filter;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import javax.xml.stream.events.XMLEvent;

class FilterStaxUnmarshaller implements Unmarshaller<Filter, StaxUnmarshallerContext> {
   private static final FilterStaxUnmarshaller instance = new FilterStaxUnmarshaller();

   public static FilterStaxUnmarshaller getInstance() {
      return instance;
   }

   private FilterStaxUnmarshaller() {
   }

   public Filter unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      Filter filter = new Filter();

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return filter;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return filter;
            }
         } else if (context.testExpression("S3Key", targetDepth)) {
            filter.withS3KeyFilter(S3KeyFilterStaxUnmarshaller.getInstance().unmarshall(context));
         }
      }
   }
}
