package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.model.GetPublicAccessBlockResult;
import com.amazonaws.services.s3.model.PublicAccessBlockConfiguration;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.SimpleTypeStaxUnmarshallers.BooleanStaxUnmarshaller;
import com.amazonaws.util.XmlUtils;
import java.io.InputStream;
import javax.xml.stream.events.XMLEvent;

public class GetPublicAccessBlockStaxUnmarshaller implements Unmarshaller<GetPublicAccessBlockResult, InputStream> {
   private static final GetPublicAccessBlockStaxUnmarshaller instance = new GetPublicAccessBlockStaxUnmarshaller();

   public static GetPublicAccessBlockStaxUnmarshaller getInstance() {
      return instance;
   }

   private GetPublicAccessBlockStaxUnmarshaller() {
   }

   public GetPublicAccessBlockResult unmarshall(InputStream inputStream) throws Exception {
      StaxUnmarshallerContext context = new StaxUnmarshallerContext(XmlUtils.getXmlInputFactory().createXMLEventReader(inputStream));
      int originalDepth = context.getCurrentDepth();
      int targetDepth = originalDepth + 1;
      if (context.isStartOfDocument()) {
         ++targetDepth;
      }

      GetPublicAccessBlockResult result = new GetPublicAccessBlockResult();
      PublicAccessBlockConfiguration configuration = new PublicAccessBlockConfiguration();
      result.setPublicAccessBlockConfiguration(configuration);

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
            if (context.testExpression("BlockPublicAcls", targetDepth)) {
               configuration.setBlockPublicAcls(BooleanStaxUnmarshaller.getInstance().unmarshall(context));
            }

            if (context.testExpression("IgnorePublicAcls", targetDepth)) {
               configuration.setIgnorePublicAcls(BooleanStaxUnmarshaller.getInstance().unmarshall(context));
            }

            if (context.testExpression("BlockPublicPolicy", targetDepth)) {
               configuration.setBlockPublicPolicy(BooleanStaxUnmarshaller.getInstance().unmarshall(context));
            }

            if (context.testExpression("RestrictPublicBuckets", targetDepth)) {
               configuration.setRestrictPublicBuckets(BooleanStaxUnmarshaller.getInstance().unmarshall(context));
            }
         }
      }
   }
}
