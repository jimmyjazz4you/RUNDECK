package com.amazonaws.transform;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.XpathUtils;
import java.lang.reflect.Constructor;
import javax.xml.xpath.XPath;
import org.w3c.dom.Node;

@SdkProtectedApi
public class LegacyErrorUnmarshaller implements Unmarshaller<AmazonServiceException, Node> {
   private final Class<? extends AmazonServiceException> exceptionClass;

   public LegacyErrorUnmarshaller() {
      this(AmazonServiceException.class);
   }

   public LegacyErrorUnmarshaller(Class<? extends AmazonServiceException> exceptionClass) {
      this.exceptionClass = exceptionClass;
   }

   public AmazonServiceException unmarshall(Node in) throws Exception {
      XPath xpath = XpathUtils.xpath();
      String errorCode = this.parseErrorCode(in, xpath);
      String message = XpathUtils.asString("Response/Errors/Error/Message", in, xpath);
      String requestId = XpathUtils.asString("Response/RequestID", in, xpath);
      String errorType = XpathUtils.asString("Response/Errors/Error/Type", in, xpath);
      Constructor<? extends AmazonServiceException> constructor = this.exceptionClass.getConstructor(String.class);
      AmazonServiceException ase = constructor.newInstance(message);
      ase.setErrorCode(errorCode);
      ase.setRequestId(requestId);
      if (errorType == null) {
         ase.setErrorType(AmazonServiceException.ErrorType.Unknown);
      } else if (errorType.equalsIgnoreCase("server")) {
         ase.setErrorType(AmazonServiceException.ErrorType.Service);
      } else if (errorType.equalsIgnoreCase("client")) {
         ase.setErrorType(AmazonServiceException.ErrorType.Client);
      }

      return ase;
   }

   public String parseErrorCode(Node in) throws Exception {
      return XpathUtils.asString("Response/Errors/Error/Code", in);
   }

   public String parseErrorCode(Node in, XPath xpath) throws Exception {
      return XpathUtils.asString("Response/Errors/Error/Code", in, xpath);
   }

   public String getErrorPropertyPath(String property) {
      return "Response/Errors/Error/" + property;
   }
}
