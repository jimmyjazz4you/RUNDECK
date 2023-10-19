package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.ServerSideEncryptionByDefault;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller;
import javax.xml.stream.events.XMLEvent;

class ServerSideEncryptionByDefaultStaxUnmarshaller implements Unmarshaller<ServerSideEncryptionByDefault, StaxUnmarshallerContext> {
   private static final ServerSideEncryptionByDefaultStaxUnmarshaller instance = new ServerSideEncryptionByDefaultStaxUnmarshaller();

   public static ServerSideEncryptionByDefaultStaxUnmarshaller getInstance() {
      return instance;
   }

   private ServerSideEncryptionByDefaultStaxUnmarshaller() {
   }

   public ServerSideEncryptionByDefault unmarshall(StaxUnmarshallerContext context) throws Exception {
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      ServerSideEncryptionByDefault sseByDefault = new ServerSideEncryptionByDefault();

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return sseByDefault;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return sseByDefault;
            }
         } else {
            if (context.testExpression("SSEAlgorithm", targetDepth)) {
               sseByDefault.setSSEAlgorithm(StringStaxUnmarshaller.getInstance().unmarshall(context));
            }

            if (context.testExpression("KMSMasterKeyID", targetDepth)) {
               sseByDefault.setKMSMasterKeyID(StringStaxUnmarshaller.getInstance().unmarshall(context));
            }
         }
      }
   }
}
