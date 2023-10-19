package com.amazonaws.transform;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.XpathUtils;
import javax.xml.xpath.XPath;
import org.w3c.dom.Node;

@SdkProtectedApi
public class StandardErrorUnmarshaller extends AbstractErrorUnmarshaller<Node> {
   public StandardErrorUnmarshaller() {
   }

   public StandardErrorUnmarshaller(Class<? extends AmazonServiceException> exceptionClass) {
      super(exceptionClass);
   }

   public AmazonServiceException unmarshall(Node in) throws Exception {
      XPath xpath = XpathUtils.xpath();
      String errorCode = this.parseErrorCode(in, xpath);
      String errorType = XpathUtils.asString("ErrorResponse/Error/Type", in, xpath);
      String requestId = XpathUtils.asString("ErrorResponse/RequestId", in, xpath);
      String message = XpathUtils.asString("ErrorResponse/Error/Message", in, xpath);
      AmazonServiceException ase = this.newException(message);
      ase.setErrorCode(errorCode);
      ase.setRequestId(requestId);
      if (errorType == null) {
         ase.setErrorType(AmazonServiceException.ErrorType.Unknown);
      } else if (errorType.equalsIgnoreCase("Receiver")) {
         ase.setErrorType(AmazonServiceException.ErrorType.Service);
      } else if (errorType.equalsIgnoreCase("Sender")) {
         ase.setErrorType(AmazonServiceException.ErrorType.Client);
      }

      return ase;
   }

   public String parseErrorCode(Node in) throws Exception {
      return XpathUtils.asString("ErrorResponse/Error/Code", in);
   }

   public String parseErrorCode(Node in, XPath xpath) throws Exception {
      return XpathUtils.asString("ErrorResponse/Error/Code", in, xpath);
   }

   public String getErrorPropertyPath(String property) {
      return "ErrorResponse/Error/" + property;
   }
}
