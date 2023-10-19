package com.amazonaws.services.s3.model.transform;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.internal.eventstreaming.HeaderType;
import com.amazonaws.services.s3.internal.eventstreaming.HeaderValue;
import com.amazonaws.services.s3.internal.eventstreaming.Message;
import com.amazonaws.services.s3.model.SelectObjectContentEvent;
import com.amazonaws.services.s3.model.SelectObjectContentEventException;
import com.amazonaws.transform.StaxUnmarshallerContext;
import com.amazonaws.util.XmlUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

@SdkInternalApi
public abstract class SelectObjectContentEventUnmarshaller {
   public static SelectObjectContentEvent unmarshalMessage(Message message) {
      String messageType = getStringHeader(message, ":message-type");
      if ("error".equals(messageType)) {
         throw unmarshalErrorMessage(message);
      } else if ("event".equals(messageType)) {
         return unmarshalEventMessage(message);
      } else {
         throw new SelectObjectContentEventException("Service returned unknown message type: " + messageType);
      }
   }

   private static SelectObjectContentEventException unmarshalErrorMessage(Message message) {
      String errorCode = getStringHeader(message, ":error-code");
      String errorMessage = getStringHeader(message, ":error-message");
      SelectObjectContentEventException exception = new SelectObjectContentEventException("S3 returned an error: " + errorMessage + " (" + errorCode + ")");
      exception.setErrorCode(errorCode);
      exception.setErrorMessage(errorMessage);
      return exception;
   }

   private static SelectObjectContentEvent unmarshalEventMessage(Message message) {
      String eventType = getStringHeader(message, ":event-type");

      try {
         return forEventType(eventType).unmarshal(message);
      } catch (Exception var3) {
         throw new SelectObjectContentEventException("Failed to read response event of type " + eventType, var3);
      }
   }

   public static SelectObjectContentEventUnmarshaller forEventType(String eventType) {
      if ("Records".equals(eventType)) {
         return new SelectObjectContentEventUnmarshaller.RecordsEventUnmarshaller();
      } else if ("Stats".equals(eventType)) {
         return new SelectObjectContentEventUnmarshaller.StatsEventUnmarshaller();
      } else if ("Progress".equals(eventType)) {
         return new SelectObjectContentEventUnmarshaller.ProgressEventUnmarshaller();
      } else if ("Cont".equals(eventType)) {
         return new SelectObjectContentEventUnmarshaller.ContinuationEventUnmarshaller();
      } else {
         return (SelectObjectContentEventUnmarshaller)("End".equals(eventType)
            ? new SelectObjectContentEventUnmarshaller.EndEventUnmarshaller()
            : new SelectObjectContentEventUnmarshaller.UnknownEventUnmarshaller());
      }
   }

   private static String getStringHeader(Message message, String headerName) {
      HeaderValue header = message.getHeaders().get(headerName);
      if (header == null) {
         throw new SelectObjectContentEventException("Unexpected lack of '" + headerName + "' header from service.");
      } else if (header.getType() != HeaderType.STRING) {
         throw new SelectObjectContentEventException("Unexpected non-string '" + headerName + "' header: " + header.getType());
      } else {
         return header.getString();
      }
   }

   public abstract SelectObjectContentEvent unmarshal(Message var1) throws Exception;

   private static StaxUnmarshallerContext payloadUnmarshaller(Message message) throws XMLStreamException {
      InputStream payloadStream = new ByteArrayInputStream(message.getPayload());
      XMLEventReader xmlEventReader = XmlUtils.getXmlInputFactory().createXMLEventReader(payloadStream);
      return new StaxUnmarshallerContext(xmlEventReader);
   }

   public static class ContinuationEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
      public SelectObjectContentEvent.ContinuationEvent unmarshal(Message message) {
         return new SelectObjectContentEvent.ContinuationEvent();
      }
   }

   public static class EndEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
      public SelectObjectContentEvent.EndEvent unmarshal(Message message) {
         return new SelectObjectContentEvent.EndEvent();
      }
   }

   public static class ProgressEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
      public SelectObjectContentEvent.ProgressEvent unmarshal(Message message) throws Exception {
         StaxUnmarshallerContext context = SelectObjectContentEventUnmarshaller.payloadUnmarshaller(message);
         return new SelectObjectContentEvent.ProgressEvent().withDetails(ProgressStaxUnmarshaller.getInstance().unmarshall(context));
      }
   }

   public static class RecordsEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
      public SelectObjectContentEvent.RecordsEvent unmarshal(Message message) {
         return new SelectObjectContentEvent.RecordsEvent().withPayload(ByteBuffer.wrap(message.getPayload()));
      }
   }

   public static class StatsEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
      public SelectObjectContentEvent.StatsEvent unmarshal(Message message) throws Exception {
         StaxUnmarshallerContext context = SelectObjectContentEventUnmarshaller.payloadUnmarshaller(message);
         return new SelectObjectContentEvent.StatsEvent().withDetails(StatsStaxUnmarshaller.getInstance().unmarshall(context));
      }
   }

   public static class UnknownEventUnmarshaller extends SelectObjectContentEventUnmarshaller {
      @Override
      public SelectObjectContentEvent unmarshal(Message message) {
         return new SelectObjectContentEvent();
      }
   }
}
