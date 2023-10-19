package com.amazonaws.transform;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkProtectedApi;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Date;

@SdkProtectedApi
public class SimpleTypeCborUnmarshallers {
   public static class BigDecimalCborUnmarshaller implements Unmarshaller<BigDecimal, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.BigDecimalCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.BigDecimalCborUnmarshaller();

      public BigDecimal unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         JsonParser parser = unmarshallerContext.getJsonParser();
         Unmarshaller<BigInteger, JsonUnmarshallerContext> bigIntegerUnmarshaller = unmarshallerContext.getUnmarshaller(BigInteger.class);
         JsonToken current = parser.getCurrentToken();
         if (current != JsonToken.START_ARRAY) {
            throw new SdkClientException("Invalid BigDecimal Format.");
         } else {
            parser.nextToken();
            int exponent = parser.getIntValue();
            parser.nextToken();
            BigInteger mantissa = bigIntegerUnmarshaller.unmarshall(unmarshallerContext);
            return new BigDecimal(mantissa, exponent);
         }
      }

      public static SimpleTypeCborUnmarshallers.BigDecimalCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BigIntegerCborUnmarshaller implements Unmarshaller<BigInteger, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.BigIntegerCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.BigIntegerCborUnmarshaller();

      public BigInteger unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         JsonParser parser = unmarshallerContext.getJsonParser();
         JsonToken current = parser.getCurrentToken();
         if (current == JsonToken.VALUE_NUMBER_INT) {
            return parser.getBigIntegerValue();
         } else if (current == JsonToken.VALUE_EMBEDDED_OBJECT) {
            Object embedded = parser.getEmbeddedObject();
            return new BigInteger((byte[])embedded);
         } else {
            throw new SdkClientException("Invalid BigInteger Format.");
         }
      }

      public static SimpleTypeCborUnmarshallers.BigIntegerCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BooleanCborUnmarshaller implements Unmarshaller<Boolean, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.BooleanCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.BooleanCborUnmarshaller();

      public Boolean unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getJsonParser().getBooleanValue();
      }

      public static SimpleTypeCborUnmarshallers.BooleanCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteBufferCborUnmarshaller implements Unmarshaller<ByteBuffer, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.ByteBufferCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.ByteBufferCborUnmarshaller();

      public ByteBuffer unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return ByteBuffer.wrap(unmarshallerContext.getJsonParser().getBinaryValue());
      }

      public static SimpleTypeCborUnmarshallers.ByteBufferCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteCborUnmarshaller implements Unmarshaller<Byte, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.ByteCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.ByteCborUnmarshaller();

      public Byte unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getJsonParser().getByteValue();
      }

      public static SimpleTypeCborUnmarshallers.ByteCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DateCborUnmarshaller implements Unmarshaller<Date, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.DateCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.DateCborUnmarshaller();

      public Date unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return new Date(unmarshallerContext.getJsonParser().getLongValue());
      }

      public static SimpleTypeCborUnmarshallers.DateCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DoubleCborUnmarshaller implements Unmarshaller<Double, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.DoubleCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.DoubleCborUnmarshaller();

      public Double unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getJsonParser().getDoubleValue();
      }

      public static SimpleTypeCborUnmarshallers.DoubleCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class FloatCborUnmarshaller implements Unmarshaller<Float, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.FloatCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.FloatCborUnmarshaller();

      public Float unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getJsonParser().getFloatValue();
      }

      public static SimpleTypeCborUnmarshallers.FloatCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class IntegerCborUnmarshaller implements Unmarshaller<Integer, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.IntegerCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.IntegerCborUnmarshaller();

      public Integer unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getJsonParser().getIntValue();
      }

      public static SimpleTypeCborUnmarshallers.IntegerCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class LongCborUnmarshaller implements Unmarshaller<Long, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.LongCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.LongCborUnmarshaller();

      public Long unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getJsonParser().getLongValue();
      }

      public static SimpleTypeCborUnmarshallers.LongCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ShortCborUnmarshaller implements Unmarshaller<Short, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.ShortCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.ShortCborUnmarshaller();

      public Short unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getJsonParser().getShortValue();
      }

      public static SimpleTypeCborUnmarshallers.ShortCborUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class StringCborUnmarshaller implements Unmarshaller<String, JsonUnmarshallerContext> {
      private static final SimpleTypeCborUnmarshallers.StringCborUnmarshaller instance = new SimpleTypeCborUnmarshallers.StringCborUnmarshaller();

      public String unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.readText();
      }

      public static SimpleTypeCborUnmarshallers.StringCborUnmarshaller getInstance() {
         return instance;
      }
   }
}
