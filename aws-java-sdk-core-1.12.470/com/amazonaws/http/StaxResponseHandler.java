package com.amazonaws.http;

import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.internal.SdkFilterInputStream;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.transform.VoidStaxUnmarshaller;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.XmlUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.io.EmptyInputStream;

public class StaxResponseHandler<T> implements HttpResponseHandler<AmazonWebServiceResponse<T>> {
   private Unmarshaller<T, StaxUnmarshallerContext> responseUnmarshaller;
   private final boolean needsConnectionLeftOpen;
   private final boolean isPayloadXML;
   private static final Log log = LogFactory.getLog("com.amazonaws.request");

   public StaxResponseHandler(Unmarshaller<T, StaxUnmarshallerContext> responseUnmarshaller, boolean needsConnectionLeftOpen, boolean isPayloadXML) {
      this.responseUnmarshaller = responseUnmarshaller;
      if (this.responseUnmarshaller == null) {
         this.responseUnmarshaller = new VoidStaxUnmarshaller<>();
      }

      this.needsConnectionLeftOpen = needsConnectionLeftOpen;
      this.isPayloadXML = isPayloadXML;
   }

   public StaxResponseHandler(Unmarshaller<T, StaxUnmarshallerContext> responseUnmarshaller) {
      this(responseUnmarshaller, false, true);
   }

   public AmazonWebServiceResponse<T> handle(HttpResponse response) throws Exception {
      log.trace("Parsing service response XML");
      InputStream content = response.getContent();
      if (content == null || !this.shouldParsePayloadAsXml()) {
         content = new ByteArrayInputStream("<eof/>".getBytes(StringUtils.UTF8));
      } else if (content instanceof SdkFilterInputStream && ((SdkFilterInputStream)content).getDelegateStream() instanceof EmptyInputStream) {
         content = new ByteArrayInputStream("<eof/>".getBytes(StringUtils.UTF8));
      }

      XMLEventReader eventReader;
      try {
         eventReader = XmlUtils.getXmlInputFactory().createXMLEventReader(content);
      } catch (XMLStreamException var19) {
         throw this.handleXmlStreamException(var19);
      }

      AmazonWebServiceResponse var9;
      try {
         AmazonWebServiceResponse<T> awsResponse = new AmazonWebServiceResponse<>();
         StaxUnmarshallerContext unmarshallerContext = new StaxUnmarshallerContext(eventReader, response.getHeaders(), response);
         unmarshallerContext.registerMetadataExpression("ResponseMetadata/RequestId", 2, "AWS_REQUEST_ID");
         unmarshallerContext.registerMetadataExpression("requestId", 2, "AWS_REQUEST_ID");
         this.registerAdditionalMetadataExpressions(unmarshallerContext);
         T result = this.responseUnmarshaller.unmarshall(unmarshallerContext);
         awsResponse.setResult(result);
         Map<String, String> metadata = unmarshallerContext.getMetadata();
         Map<String, String> responseHeaders = response.getHeaders();
         if (responseHeaders != null) {
            if (responseHeaders.get("x-amzn-RequestId") != null) {
               metadata.put("AWS_REQUEST_ID", responseHeaders.get("x-amzn-RequestId"));
            }

            if (responseHeaders.get("x-amz-id-2") != null) {
               metadata.put("AWS_EXTENDED_REQUEST_ID", responseHeaders.get("x-amz-id-2"));
            }
         }

         awsResponse.setResponseMetadata(this.getResponseMetadata(metadata));
         log.trace("Done parsing service response");
         var9 = awsResponse;
      } catch (XMLStreamException var20) {
         throw this.handleXmlStreamException(var20);
      } finally {
         try {
            eventReader.close();
         } catch (XMLStreamException var18) {
            log.warn("Error closing xml parser", var18);
         }
      }

      return var9;
   }

   private Exception handleXmlStreamException(XMLStreamException e) throws Exception {
      return (Exception)(e.getNestedException() instanceof IOException ? new IOException(e) : e);
   }

   protected ResponseMetadata getResponseMetadata(Map<String, String> metadata) {
      return new ResponseMetadata(metadata);
   }

   protected void registerAdditionalMetadataExpressions(StaxUnmarshallerContext unmarshallerContext) {
   }

   @Override
   public boolean needsConnectionLeftOpen() {
      return this.needsConnectionLeftOpen;
   }

   private boolean shouldParsePayloadAsXml() {
      return !this.needsConnectionLeftOpen && this.isPayloadXML;
   }
}
