package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.GetBucketPolicyStatusResult;
import com.amazonaws.services.s3.model.PolicyStatus;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.BooleanStaxUnmarshaller;
import com.amazonaws.util.XmlUtils;
import java.io.InputStream;
import javax.xml.stream.events.XMLEvent;

public class GetBucketPolicyStatusStaxUnmarshaller implements Unmarshaller<GetBucketPolicyStatusResult, InputStream> {
   private static final GetBucketPolicyStatusStaxUnmarshaller instance = new GetBucketPolicyStatusStaxUnmarshaller();

   public static GetBucketPolicyStatusStaxUnmarshaller getInstance() {
      return instance;
   }

   private GetBucketPolicyStatusStaxUnmarshaller() {
   }

   public GetBucketPolicyStatusResult unmarshall(InputStream inputStream) throws Exception {
      StaxUnmarshallerContext context = new StaxUnmarshallerContext(XmlUtils.getXmlInputFactory().createXMLEventReader(inputStream));
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      GetBucketPolicyStatusResult result = new GetBucketPolicyStatusResult();
      PolicyStatus policyStatus = new PolicyStatus();
      result.setPolicyStatus(policyStatus);

      while(true) {
         XMLEvent xmlEvent = context.nextEvent();
         if (xmlEvent.isEndDocument()) {
            return result;
         }

         if (!xmlEvent.isAttribute() && !xmlEvent.isStartElement()) {
            if (xmlEvent.isEndElement() && context.getCurrentDepth() < originalDepth) {
               return result;
            }
         } else if (context.testExpression("IsPublic", targetDepth)) {
            policyStatus.setIsPublic(BooleanStaxUnmarshaller.getInstance().unmarshall(context));
         }
      }
   }
}
