package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.GetBucketEncryptionResult;
import com.amazonaws.services.s3.model.ServerSideEncryptionConfiguration;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.util.XmlUtils;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.stream.events.XMLEvent;

public class GetBucketEncryptionStaxUnmarshaller implements Unmarshaller<GetBucketEncryptionResult, InputStream> {
   private static final GetBucketEncryptionStaxUnmarshaller instance = new GetBucketEncryptionStaxUnmarshaller();

   public static GetBucketEncryptionStaxUnmarshaller getInstance() {
      return instance;
   }

   private GetBucketEncryptionStaxUnmarshaller() {
   }

   public GetBucketEncryptionResult unmarshall(InputStream inputStream) throws Exception {
      StaxUnmarshallerContext context = new StaxUnmarshallerContext(XmlUtils.getXmlInputFactory().createXMLEventReader(inputStream));
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      GetBucketEncryptionResult result = new GetBucketEncryptionResult();
      ServerSideEncryptionConfiguration sseConfig = new ServerSideEncryptionConfiguration();
      result.setServerSideEncryptionConfiguration(sseConfig);

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return result;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return result;
            }
         } else if (context.testExpression("Rule", targetDepth)) {
            if (sseConfig.getRules() == null) {
               sseConfig.setRules(new ArrayList<>());
            }

            sseConfig.getRules().add(ServerSideEncryptionRuleStaxUnmarshaller.getInstance().unmarshall(context));
         }
      }
   }
}
