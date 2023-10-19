package com.amazonaws.http;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.XpathUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

@SdkProtectedApi
public class DefaultErrorResponseHandler implements HttpResponseHandler<AmazonServiceException> {
   private static final Log log = LogFactory.getLog(DefaultErrorResponseHandler.class);
   private Map<String, Unmarshaller<AmazonServiceException, Node>> unmarshallerMap;
   private Unmarshaller<AmazonServiceException, Node> defaultUnmarshaller;
   private List<Unmarshaller<AmazonServiceException, Node>> unmarshallerList;

   public DefaultErrorResponseHandler(
      Map<String, Unmarshaller<AmazonServiceException, Node>> unmarshallerMap, Unmarshaller<AmazonServiceException, Node> defaultUnmarshaller
   ) {
      this.unmarshallerMap = unmarshallerMap;
      this.defaultUnmarshaller = defaultUnmarshaller;
   }

   public DefaultErrorResponseHandler(List<Unmarshaller<AmazonServiceException, Node>> unmarshallerList) {
      this.unmarshallerList = unmarshallerList;
   }

   public AmazonServiceException handle(HttpResponse errorResponse) throws Exception {
      AmazonServiceException ase = this.createAse(errorResponse);
      if (ase == null) {
         throw new SdkClientException("Unable to unmarshall error response from service");
      } else {
         ase.setHttpHeaders(errorResponse.getHeaders());
         if (StringUtils.isNullOrEmpty(ase.getErrorCode())) {
            ase.setErrorCode(errorResponse.getStatusCode() + " " + errorResponse.getStatusText());
         }

         return ase;
      }
   }

   private AmazonServiceException createAse(HttpResponse errorResponse) throws Exception {
      Document document = this.documentFromContent(errorResponse.getContent(), this.idString(errorResponse));
      return this.unmarshallerMap != null ? this.exceptionFromMappedUnmarshallers(errorResponse, document) : this.getExceptionFromList(errorResponse, document);
   }

   private AmazonServiceException exceptionFromMappedUnmarshallers(HttpResponse errorResponse, Document document) throws Exception {
      Unmarshaller<AmazonServiceException, Node> mappedUnmarshaller = null;
      String errorCode = this.parseErrorCodeFromResponse(document);
      if (errorCode != null) {
         mappedUnmarshaller = this.unmarshallerMap.get(errorCode);
      }

      Unmarshaller<AmazonServiceException, Node> unmarshaller = mappedUnmarshaller != null ? mappedUnmarshaller : this.defaultUnmarshaller;
      return unmarshaller != null ? this.getAmazonServiceException(errorResponse, document, unmarshaller) : null;
   }

   private String parseErrorCodeFromResponse(Document document) throws XPathExpressionException {
      String errorCode = XpathUtils.asString("Response/Errors/Error/Code", document);
      if (errorCode == null) {
         errorCode = XpathUtils.asString("ErrorResponse/Error/Code", document);
      }

      return errorCode;
   }

   private AmazonServiceException getExceptionFromList(HttpResponse errorResponse, Document document) throws Exception {
      for(Unmarshaller<AmazonServiceException, Node> unmarshaller : this.unmarshallerList) {
         AmazonServiceException exception = this.getAmazonServiceException(errorResponse, document, unmarshaller);
         if (exception != null) {
            return exception;
         }
      }

      return null;
   }

   private AmazonServiceException getAmazonServiceException(
      HttpResponse errorResponse, Document document, Unmarshaller<AmazonServiceException, Node> unmarshaller
   ) throws Exception {
      AmazonServiceException ase = unmarshaller.unmarshall(document);
      if (ase != null) {
         ase.setStatusCode(errorResponse.getStatusCode());
         return ase;
      } else {
         return null;
      }
   }

   private Document documentFromContent(InputStream content, String idString) throws ParserConfigurationException, SAXException, IOException {
      try {
         return this.parseXml(this.contentToString(content, idString), idString);
      } catch (Exception var4) {
         return XpathUtils.documentFrom("<empty/>");
      }
   }

   private String contentToString(InputStream content, String idString) throws Exception {
      try {
         return IOUtils.toString(content);
      } catch (Exception var4) {
         log.debug(String.format("Unable to read input stream to string (%s)", idString), var4);
         throw var4;
      }
   }

   private Document parseXml(String xml, String idString) throws Exception {
      try {
         return XpathUtils.documentFrom(xml);
      } catch (Exception var4) {
         log.debug(String.format("Unable to parse HTTP response (%s) content to XML document '%s' ", idString, xml), var4);
         throw var4;
      }
   }

   private String idString(HttpResponse errorResponse) {
      StringBuilder idString = new StringBuilder();

      try {
         if (errorResponse.getRequest().getHeaders().containsKey("amz-sdk-invocation-id")) {
            idString.append("Invocation Id:").append(errorResponse.getRequest().getHeaders().get("amz-sdk-invocation-id"));
         }

         if (errorResponse.getHeaders().containsKey("x-amzn-RequestId")) {
            if (idString.length() > 0) {
               idString.append(", ");
            }

            idString.append("Request Id:").append(errorResponse.getHeaders().get("x-amzn-RequestId"));
         }
      } catch (NullPointerException var4) {
         log.debug("Error getting Request or Invocation ID from response", var4);
      }

      return idString.length() > 0 ? idString.toString() : "Unknown";
   }

   @Override
   public boolean needsConnectionLeftOpen() {
      return false;
   }
}
