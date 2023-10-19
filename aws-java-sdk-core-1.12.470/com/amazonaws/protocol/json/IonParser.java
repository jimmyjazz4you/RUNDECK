package com.amazonaws.protocol.json;

import com.amazonaws.SdkClientException;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import software.amazon.ion.IonReader;
import software.amazon.ion.IonType;

final class IonParser extends JsonParser {
   private final IonReader reader;
   private final boolean shouldCloseReader;
   private IonParser.State state = IonParser.State.BEFORE_VALUE;
   private JsonToken currentToken;
   private JsonToken lastClearedToken;
   private boolean shouldSkipContainer;
   private boolean closed;

   public IonParser(IonReader reader, boolean shouldCloseReader) {
      super(Feature.collectDefaults());
      this.reader = reader;
      this.shouldCloseReader = shouldCloseReader;
   }

   public ObjectCodec getCodec() {
      throw new UnsupportedOperationException();
   }

   public void setCodec(ObjectCodec c) {
      throw new UnsupportedOperationException();
   }

   public Version version() {
      throw new UnsupportedOperationException();
   }

   public void close() throws IOException {
      if (this.shouldCloseReader) {
         this.reader.close();
      } else if (Feature.AUTO_CLOSE_SOURCE.enabledIn(this._features)) {
         this.reader.close();
      }

      this.closed = true;
   }

   public JsonToken nextToken() throws IOException, JsonParseException {
      this.currentToken = this.doNextToken();
      return this.currentToken;
   }

   private JsonToken doNextToken() {
      while(true) {
         switch(this.state) {
            case BEFORE_VALUE:
               IonType currentType = this.reader.next();
               if (currentType == null) {
                  boolean topLevel = this.reader.getDepth() == 0;
                  if (topLevel) {
                     this.state = IonParser.State.EOF;
                     break;
                  }

                  this.state = IonParser.State.END_OF_CONTAINER;
                  return this.reader.isInStruct() ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
               }

               if (this.reader.isInStruct()) {
                  this.state = IonParser.State.FIELD_NAME;
                  return JsonToken.FIELD_NAME;
               }

               this.state = IonParser.State.VALUE;
               return this.getJsonToken();
            case END_OF_CONTAINER:
               this.reader.stepOut();
               this.state = IonParser.State.BEFORE_VALUE;
               break;
            case EOF:
               return null;
            case FIELD_NAME:
               this.state = IonParser.State.VALUE;
               return this.getJsonToken();
            case VALUE:
               this.state = IonParser.State.BEFORE_VALUE;
               if (IonType.isContainer(this.reader.getType()) && !this.reader.isNullValue() && !this.shouldSkipContainer) {
                  this.reader.stepIn();
               }

               this.shouldSkipContainer = false;
         }
      }
   }

   public JsonToken nextValue() throws IOException, JsonParseException {
      JsonToken token = this.nextToken();
      return token == JsonToken.FIELD_NAME ? this.nextToken() : token;
   }

   public JsonParser skipChildren() throws IOException, JsonParseException {
      IonType currentType = this.reader.getType();
      if (IonType.isContainer(currentType)) {
         this.shouldSkipContainer = true;
         this.currentToken = currentType == IonType.STRUCT ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
      }

      return this;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public JsonToken getCurrentToken() {
      return this.currentToken;
   }

   public int getCurrentTokenId() {
      return this.currentToken == null ? 0 : this.currentToken.id();
   }

   public boolean hasCurrentToken() {
      return this.currentToken != null;
   }

   public boolean hasTokenId(int id) {
      return this.getCurrentTokenId() == id;
   }

   public boolean hasToken(JsonToken t) {
      return this.currentToken == t;
   }

   public String getCurrentName() throws IOException {
      return this.reader.getFieldName();
   }

   public JsonStreamContext getParsingContext() {
      throw new UnsupportedOperationException();
   }

   public JsonLocation getTokenLocation() {
      throw new UnsupportedOperationException();
   }

   public JsonLocation getCurrentLocation() {
      throw new UnsupportedOperationException();
   }

   public void clearCurrentToken() {
      this.lastClearedToken = this.currentToken;
      this.currentToken = null;
   }

   public JsonToken getLastClearedToken() {
      return this.lastClearedToken;
   }

   public void overrideCurrentName(String name) {
      throw new UnsupportedOperationException();
   }

   public String getText() throws IOException {
      if (this.state == IonParser.State.FIELD_NAME) {
         return this.reader.getFieldName();
      } else if (IonType.isText(this.reader.getType())) {
         return this.reader.stringValue();
      } else if (this.currentToken == null) {
         return null;
      } else {
         return this.currentToken.isNumeric() ? this.getNumberValue().toString() : this.currentToken.asString();
      }
   }

   public char[] getTextCharacters() throws IOException {
      throw new UnsupportedOperationException();
   }

   public int getTextLength() throws IOException {
      throw new UnsupportedOperationException();
   }

   public int getTextOffset() throws IOException {
      throw new UnsupportedOperationException();
   }

   public boolean hasTextCharacters() {
      return false;
   }

   public Number getNumberValue() throws IOException {
      NumberType numberType = this.getNumberType();
      if (numberType == null) {
         throw new SdkClientException(String.format("Unable to get number value for non-numeric token %s", this.reader.getType()));
      } else {
         switch(numberType) {
            case BIG_DECIMAL:
               return this.reader.bigDecimalValue();
            case BIG_INTEGER:
               return this.reader.bigIntegerValue();
            case DOUBLE:
               return this.reader.doubleValue();
            default:
               throw new SdkClientException(String.format("Unable to get number value for number type %s", numberType));
         }
      }
   }

   public NumberType getNumberType() throws IOException {
      switch(this.reader.getType()) {
         case DECIMAL:
            return NumberType.BIG_DECIMAL;
         case FLOAT:
            return NumberType.DOUBLE;
         case INT:
            return NumberType.BIG_INTEGER;
         default:
            return null;
      }
   }

   public int getIntValue() throws IOException {
      return this.reader.intValue();
   }

   public long getLongValue() throws IOException {
      return this.reader.longValue();
   }

   public BigInteger getBigIntegerValue() throws IOException {
      return this.reader.bigIntegerValue();
   }

   public float getFloatValue() throws IOException {
      return (float)this.reader.doubleValue();
   }

   public double getDoubleValue() throws IOException {
      return this.reader.doubleValue();
   }

   public BigDecimal getDecimalValue() throws IOException {
      return this.reader.decimalValue();
   }

   public Object getEmbeddedObject() throws IOException {
      if (this.currentToken != JsonToken.VALUE_EMBEDDED_OBJECT) {
         return null;
      } else {
         IonType currentType = this.reader.getType();
         switch(currentType) {
            case BLOB:
            case CLOB:
               return ByteBuffer.wrap(this.reader.newBytes());
            case TIMESTAMP:
               return this.reader.timestampValue().dateValue();
            default:
               throw new SdkClientException(String.format("Cannot return embedded object for Ion type %s", currentType));
         }
      }
   }

   public byte[] getBinaryValue(Base64Variant bv) throws IOException {
      throw new UnsupportedOperationException();
   }

   public String getValueAsString(String defaultValue) throws IOException {
      return this.currentToken == JsonToken.VALUE_STRING
            || this.currentToken != null && this.currentToken != JsonToken.VALUE_NULL && this.currentToken.isScalarValue()
         ? this.getText()
         : defaultValue;
   }

   private JsonToken getJsonToken() {
      if (this.reader.isNullValue()) {
         return JsonToken.VALUE_NULL;
      } else {
         IonType currentType = this.reader.getType();
         switch(currentType) {
            case DECIMAL:
               return JsonToken.VALUE_NUMBER_FLOAT;
            case FLOAT:
               return JsonToken.VALUE_NUMBER_FLOAT;
            case INT:
               return JsonToken.VALUE_NUMBER_INT;
            case BLOB:
            case CLOB:
               return JsonToken.VALUE_EMBEDDED_OBJECT;
            case TIMESTAMP:
               return JsonToken.VALUE_EMBEDDED_OBJECT;
            case BOOL:
               return this.reader.booleanValue() ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
            case LIST:
               return JsonToken.START_ARRAY;
            case SEXP:
               return JsonToken.START_ARRAY;
            case STRING:
               return JsonToken.VALUE_STRING;
            case STRUCT:
               return JsonToken.START_OBJECT;
            case SYMBOL:
               return JsonToken.VALUE_STRING;
            default:
               throw new SdkClientException(String.format("Unhandled Ion type %s", currentType));
         }
      }
   }

   private static enum State {
      BEFORE_VALUE,
      END_OF_CONTAINER,
      EOF,
      FIELD_NAME,
      VALUE;
   }
}
