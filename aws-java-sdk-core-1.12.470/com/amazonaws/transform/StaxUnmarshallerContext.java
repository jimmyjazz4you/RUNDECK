package com.amazonaws.transform;

import com.amazonaws.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

public class StaxUnmarshallerContext {
   private XMLEvent currentEvent;
   private final XMLEventReader eventReader;
   public final Stack<String> stack = new Stack<>();
   private String stackString = "";
   private Map<String, String> metadata = new HashMap<>();
   private List<StaxUnmarshallerContext.MetadataExpression> metadataExpressions = new ArrayList<>();
   private Iterator<?> attributeIterator;
   private final Map<String, String> headers;
   private final HttpResponse httpResponse;
   private String currentHeader;

   public void setCurrentHeader(String currentHeader) {
      this.currentHeader = currentHeader;
   }

   public boolean isInsideResponseHeader() {
      return this.currentEvent == null;
   }

   public StaxUnmarshallerContext(XMLEventReader eventReader) {
      this(eventReader, null, null);
   }

   public StaxUnmarshallerContext(XMLEventReader eventReader, Map<String, String> headers) {
      this(eventReader, headers, null);
   }

   public StaxUnmarshallerContext(XMLEventReader eventReader, Map<String, String> headers, HttpResponse httpResponse) {
      this.eventReader = eventReader;
      this.headers = headers;
      this.httpResponse = httpResponse;
   }

   public String getHeader(String header) {
      return this.headers == null ? null : this.headers.get(header);
   }

   public HttpResponse getHttpResponse() {
      return this.httpResponse;
   }

   public String readText() throws XMLStreamException {
      if (this.isInsideResponseHeader()) {
         return this.getHeader(this.currentHeader);
      } else if (this.currentEvent.isAttribute()) {
         Attribute attribute = (Attribute)this.currentEvent;
         return attribute.getValue();
      } else {
         StringBuilder sb = new StringBuilder();

         while(true) {
            XMLEvent event = this.eventReader.peek();
            if (event.getEventType() != 4) {
               if (event.getEventType() == 2) {
                  return sb.toString();
               } else {
                  throw new RuntimeException("Encountered unexpected event: " + event.toString());
               }
            }

            this.eventReader.nextEvent();
            sb.append(event.asCharacters().getData());
         }
      }
   }

   public int getCurrentDepth() {
      return this.stack.size();
   }

   public boolean testExpression(String expression) {
      return expression.equals(".") ? true : this.stackString.endsWith(expression);
   }

   public boolean testExpression(String expression, int startingStackDepth) {
      if (expression.equals(".")) {
         return true;
      } else {
         int index = -1;

         while((index = expression.indexOf("/", index + 1)) > -1) {
            if (expression.charAt(index + 1) != '@') {
               ++startingStackDepth;
            }
         }

         return startingStackDepth == this.getCurrentDepth() && this.stackString.endsWith("/" + expression);
      }
   }

   public boolean isStartOfDocument() throws XMLStreamException {
      return this.eventReader.peek().isStartDocument();
   }

   public XMLEvent nextEvent() throws XMLStreamException {
      if (this.attributeIterator != null && this.attributeIterator.hasNext()) {
         this.currentEvent = (XMLEvent)this.attributeIterator.next();
      } else {
         this.currentEvent = this.eventReader.nextEvent();
      }

      if (this.currentEvent.isStartElement()) {
         this.attributeIterator = this.currentEvent.asStartElement().getAttributes();
      }

      this.updateContext(this.currentEvent);
      if (this.eventReader.hasNext()) {
         XMLEvent nextEvent = this.eventReader.peek();
         if (nextEvent != null && nextEvent.isCharacters()) {
            for(StaxUnmarshallerContext.MetadataExpression metadataExpression : this.metadataExpressions) {
               if (this.testExpression(metadataExpression.expression, metadataExpression.targetDepth)) {
                  this.metadata.put(metadataExpression.key, nextEvent.asCharacters().getData());
               }
            }
         }
      }

      return this.currentEvent;
   }

   public Map<String, String> getMetadata() {
      return this.metadata;
   }

   public void registerMetadataExpression(String expression, int targetDepth, String storageKey) {
      this.metadataExpressions.add(new StaxUnmarshallerContext.MetadataExpression(expression, targetDepth, storageKey));
   }

   private void updateContext(XMLEvent event) {
      if (event != null) {
         if (event.isEndElement()) {
            this.stack.pop();
            this.stackString = "";

            for(String s : this.stack) {
               this.stackString = this.stackString + "/" + s;
            }
         } else if (event.isStartElement()) {
            this.stack.push(event.asStartElement().getName().getLocalPart());
            this.stackString = this.stackString + "/" + event.asStartElement().getName().getLocalPart();
         } else if (event.isAttribute()) {
            Attribute attribute = (Attribute)event;
            this.stackString = "";

            for(String s : this.stack) {
               this.stackString = this.stackString + "/" + s;
            }

            this.stackString = this.stackString + "/@" + attribute.getName().getLocalPart();
         }
      }
   }

   private static class MetadataExpression {
      public String expression;
      public int targetDepth;
      public String key;

      public MetadataExpression(String expression, int targetDepth, String key) {
         this.expression = expression;
         this.targetDepth = targetDepth;
         this.key = key;
      }
   }
}
