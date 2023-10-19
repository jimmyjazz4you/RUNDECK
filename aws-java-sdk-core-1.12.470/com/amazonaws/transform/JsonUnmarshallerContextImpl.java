package com.amazonaws.transform;

import com.amazonaws.http.HttpResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class JsonUnmarshallerContextImpl extends JsonUnmarshallerContext {
   private JsonToken currentToken;
   private JsonToken nextToken;
   private final JsonParser jsonParser;
   private String currentHeader;
   private final Stack<JsonUnmarshallerContextImpl.JsonFieldTokenPair> stack = new Stack<>();
   private String currentField;
   private String lastParsedParentElement;
   private Map<String, String> metadata = new HashMap<>();
   private final HttpResponse httpResponse;
   private final Map<Class<?>, Unmarshaller<?, JsonUnmarshallerContext>> unmarshallerMap;
   private final Map<JsonUnmarshallerContext.UnmarshallerType, Unmarshaller<?, JsonUnmarshallerContext>> customUnmarshallerMap;

   public JsonUnmarshallerContextImpl(JsonParser jsonParser, Map<Class<?>, Unmarshaller<?, JsonUnmarshallerContext>> mapper, HttpResponse httpResponse) {
      this(jsonParser, mapper, Collections.emptyMap(), httpResponse);
   }

   public JsonUnmarshallerContextImpl(
      JsonParser jsonParser,
      Map<Class<?>, Unmarshaller<?, JsonUnmarshallerContext>> mapper,
      Map<JsonUnmarshallerContext.UnmarshallerType, Unmarshaller<?, JsonUnmarshallerContext>> customUnmarshallerMap,
      HttpResponse httpResponse
   ) {
      this.jsonParser = jsonParser;
      this.unmarshallerMap = mapper;
      this.customUnmarshallerMap = customUnmarshallerMap;
      this.httpResponse = httpResponse;
   }

   @Override
   public String getHeader(String header) {
      return this.httpResponse == null ? null : this.httpResponse.getHeaders().get(header);
   }

   @Override
   public HttpResponse getHttpResponse() {
      return this.httpResponse;
   }

   @Override
   public int getCurrentDepth() {
      int depth = this.stack.size();
      if (this.currentField != null) {
         ++depth;
      }

      return depth;
   }

   @Override
   public String readText() throws IOException {
      return this.isInsideResponseHeader() ? this.getHeader(this.currentHeader) : this.readCurrentJsonTokenValue();
   }

   private String readCurrentJsonTokenValue() throws IOException {
      switch(this.currentToken) {
         case VALUE_STRING:
            return this.jsonParser.getText();
         case VALUE_FALSE:
            return "false";
         case VALUE_TRUE:
            return "true";
         case VALUE_NULL:
            return null;
         case VALUE_NUMBER_FLOAT:
         case VALUE_NUMBER_INT:
            return this.jsonParser.getNumberValue().toString();
         case FIELD_NAME:
            return this.jsonParser.getText();
         default:
            throw new RuntimeException("We expected a VALUE token but got: " + this.currentToken);
      }
   }

   @Override
   public boolean isInsideResponseHeader() {
      return this.currentToken == null && this.nextToken == null;
   }

   @Override
   public boolean isStartOfDocument() {
      return this.jsonParser == null || this.jsonParser.getCurrentToken() == null;
   }

   @Override
   public boolean testExpression(String expression) {
      if (expression.equals(".")) {
         return true;
      } else if (this.currentField != null) {
         return this.currentField.equals(expression);
      } else {
         return !this.stack.isEmpty() && this.stack.peek().getField().equals(expression);
      }
   }

   @Override
   public String getCurrentParentElement() {
      String parentElement;
      if (this.currentField != null) {
         parentElement = this.currentField;
      } else if (!this.stack.isEmpty()) {
         parentElement = this.stack.peek().getField();
      } else {
         parentElement = "";
      }

      return parentElement;
   }

   @Override
   public boolean testExpression(String expression, int stackDepth) {
      if (expression.equals(".")) {
         return true;
      } else {
         return this.testExpression(expression) && stackDepth == this.getCurrentDepth();
      }
   }

   @Override
   public JsonToken nextToken() throws IOException {
      JsonToken token = this.nextToken != null ? this.nextToken : this.jsonParser.nextToken();
      this.currentToken = token;
      this.nextToken = null;
      this.updateContext();
      return token;
   }

   @Override
   public JsonToken peek() throws IOException {
      if (this.nextToken != null) {
         return this.nextToken;
      } else {
         this.nextToken = this.jsonParser.nextToken();
         return this.nextToken;
      }
   }

   @Override
   public JsonParser getJsonParser() {
      return this.jsonParser;
   }

   @Override
   public Map<String, String> getMetadata() {
      return this.metadata;
   }

   @Override
   public void setCurrentHeader(String currentHeader) {
      this.currentHeader = currentHeader;
   }

   @Override
   public <T> Unmarshaller<T, JsonUnmarshallerContext> getUnmarshaller(Class<T> type) {
      return (Unmarshaller<T, JsonUnmarshallerContext>)this.unmarshallerMap.get(type);
   }

   @Override
   public <T> Unmarshaller<T, JsonUnmarshallerContext> getUnmarshaller(Class<T> type, JsonUnmarshallerContext.UnmarshallerType unmarshallerType) {
      return (Unmarshaller<T, JsonUnmarshallerContext>)this.customUnmarshallerMap.get(unmarshallerType);
   }

   @Override
   public JsonToken getCurrentToken() {
      return this.currentToken;
   }

   private void updateContext() throws IOException {
      this.lastParsedParentElement = null;
      if (this.currentToken != null) {
         if (this.currentToken != JsonToken.START_OBJECT && this.currentToken != JsonToken.START_ARRAY) {
            if (this.currentToken != JsonToken.END_OBJECT && this.currentToken != JsonToken.END_ARRAY) {
               if (this.currentToken == JsonToken.FIELD_NAME) {
                  String t = this.jsonParser.getText();
                  this.currentField = t;
               }
            } else {
               if (!this.stack.isEmpty()) {
                  boolean squareBracketsMatch = this.currentToken == JsonToken.END_ARRAY && this.stack.peek().getToken() == JsonToken.START_ARRAY;
                  boolean curlyBracketsMatch = this.currentToken == JsonToken.END_OBJECT && this.stack.peek().getToken() == JsonToken.START_OBJECT;
                  if (squareBracketsMatch || curlyBracketsMatch) {
                     this.lastParsedParentElement = this.stack.pop().getField();
                  }
               }

               this.currentField = null;
            }
         } else if (this.currentField != null) {
            this.stack.push(new JsonUnmarshallerContextImpl.JsonFieldTokenPair(this.currentField, this.currentToken));
            this.currentField = null;
         } else if (this.currentToken == JsonToken.START_ARRAY) {
            this.stack.push(new JsonUnmarshallerContextImpl.JsonFieldTokenPair("ARRAY", this.currentToken));
         }
      }
   }

   @Override
   public String toString() {
      StringBuilder stackString = new StringBuilder();

      for(JsonUnmarshallerContextImpl.JsonFieldTokenPair jsonFieldTokenPair : this.stack) {
         stackString.append("/").append(jsonFieldTokenPair.getField());
      }

      if (this.currentField != null) {
         stackString.append("/").append(this.currentField);
      }

      return stackString.length() == 0 ? "/" : stackString.toString();
   }

   @Override
   public String getLastParsedParentElement() {
      return this.lastParsedParentElement;
   }

   private static class JsonFieldTokenPair {
      private final String field;
      private final JsonToken jsonToken;

      public JsonFieldTokenPair(String fieldString, JsonToken token) {
         this.field = fieldString;
         this.jsonToken = token;
      }

      public String getField() {
         return this.field;
      }

      public JsonToken getToken() {
         return this.jsonToken;
      }

      @Override
      public String toString() {
         return this.field + ": " + this.jsonToken.asString();
      }
   }
}
