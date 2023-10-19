package com.amazonaws.internal.http;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.protocol.json.JsonContent;
import com.amazonaws.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import software.amazon.ion.IonReader;
import software.amazon.ion.IonSystem;
import software.amazon.ion.IonType;

@SdkInternalApi
public class IonErrorCodeParser implements ErrorCodeParser {
   private static final Log log = LogFactory.getLog(IonErrorCodeParser.class);
   private static final String TYPE_PREFIX = "aws-type:";
   private static final String X_AMZN_REQUEST_ID_HEADER = "x-amzn-RequestId";
   private final IonSystem ionSystem;

   public IonErrorCodeParser(IonSystem ionSystem) {
      this.ionSystem = ionSystem;
   }

   @Override
   public String parseErrorCode(HttpResponse response, JsonContent jsonContents) {
      IonReader reader = this.ionSystem.newReader(jsonContents.getRawContent());

      String var15;
      try {
         IonType type = reader.next();
         if (type != IonType.STRUCT) {
            throw new SdkClientException(String.format("Can only get error codes from structs (saw %s), request id %s", type, getRequestId(response)));
         }

         boolean errorCodeSeen = false;
         String errorCode = null;
         String[] annotations = reader.getTypeAnnotations();

         for(String annotation : annotations) {
            if (annotation.startsWith("aws-type:")) {
               if (errorCodeSeen) {
                  throw new SdkClientException(String.format("Multiple error code annotations found for request id %s", getRequestId(response)));
               }

               errorCodeSeen = true;
               errorCode = annotation.substring("aws-type:".length());
            }
         }

         var15 = errorCode;
      } finally {
         IOUtils.closeQuietly(reader, log);
      }

      return var15;
   }

   private static String getRequestId(HttpResponse response) {
      return response.getHeaders().get("x-amzn-RequestId");
   }
}
