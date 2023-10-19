package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.ServerSideEncryptionRule;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.BooleanStaxUnmarshaller;
import javax.xml.stream.events.XMLEvent;

class ServerSideEncryptionRuleStaxUnmarshaller implements Unmarshaller<ServerSideEncryptionRule, StaxUnmarshallerContext> {
   private static final ServerSideEncryptionRuleStaxUnmarshaller instance = new ServerSideEncryptionRuleStaxUnmarshaller();

   public static ServerSideEncryptionRuleStaxUnmarshaller getInstance() {
      return instance;
   }

   private ServerSideEncryptionRuleStaxUnmarshaller() {
   }

   public ServerSideEncryptionRule unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      ServerSideEncryptionRule rule = new ServerSideEncryptionRule();

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return rule;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return rule;
            }
         } else if (context.testExpression("ApplyServerSideEncryptionByDefault", targetDepth)) {
            rule.setApplyServerSideEncryptionByDefault(ServerSideEncryptionByDefaultStaxUnmarshaller.getInstance().unmarshall(context));
         } else if (context.testExpression("BucketKeyEnabled", targetDepth)) {
            rule.setBucketKeyEnabled(BooleanStaxUnmarshaller.getInstance().unmarshall(context));
         }
      }
   }
}
