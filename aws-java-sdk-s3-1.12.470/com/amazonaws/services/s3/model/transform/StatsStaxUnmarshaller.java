package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.Stats;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.LongStaxUnmarshaller;
import javax.xml.stream.events.XMLEvent;

class StatsStaxUnmarshaller implements Unmarshaller<Stats, StaxUnmarshallerContext> {
   private static final StatsStaxUnmarshaller instance = new StatsStaxUnmarshaller();

   public static StatsStaxUnmarshaller getInstance() {
      return instance;
   }

   private StatsStaxUnmarshaller() {
   }

   public Stats unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      Stats result = new Stats();

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return result;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return result;
            }
         } else {
            if (context.testExpression("BytesScanned", targetDepth)) {
               result.setBytesScanned(LongStaxUnmarshaller.getInstance().unmarshall(context));
            }

            if (context.testExpression("BytesReturned", targetDepth)) {
               result.setBytesReturned(LongStaxUnmarshaller.getInstance().unmarshall(context));
            }

            if (context.testExpression("BytesProcessed", targetDepth)) {
               result.setBytesProcessed(LongStaxUnmarshaller.getInstance().unmarshall(context));
            }
         }
      }
   }
}
