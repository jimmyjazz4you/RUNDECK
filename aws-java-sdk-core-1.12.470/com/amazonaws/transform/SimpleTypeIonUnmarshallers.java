package com.amazonaws.transform;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Date;

public class SimpleTypeIonUnmarshallers {
   public static class BigDecimalIonUnmarshaller implements Unmarshaller<BigDecimal, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.BigDecimalIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.BigDecimalIonUnmarshaller();

      public BigDecimal unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getDecimalValue();
      }

      public static SimpleTypeIonUnmarshallers.BigDecimalIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BigIntegerIonUnmarshaller implements Unmarshaller<BigInteger, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.BigIntegerIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.BigIntegerIonUnmarshaller();

      public BigInteger unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getBigIntegerValue();
      }

      public static SimpleTypeIonUnmarshallers.BigIntegerIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BooleanIonUnmarshaller implements Unmarshaller<Boolean, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.BooleanIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.BooleanIonUnmarshaller();

      public Boolean unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getBooleanValue();
      }

      public static SimpleTypeIonUnmarshallers.BooleanIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteBufferIonUnmarshaller implements Unmarshaller<ByteBuffer, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.ByteBufferIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.ByteBufferIonUnmarshaller();

      public ByteBuffer unmarshall(JsonUnmarshallerContext context) throws Exception {
         return (ByteBuffer)context.getJsonParser().getEmbeddedObject();
      }

      public static SimpleTypeIonUnmarshallers.ByteBufferIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteIonUnmarshaller implements Unmarshaller<Byte, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.ByteIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.ByteIonUnmarshaller();

      public Byte unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getByteValue();
      }

      public static SimpleTypeIonUnmarshallers.ByteIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DateIonUnmarshaller implements Unmarshaller<Date, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.DateIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.DateIonUnmarshaller();

      public Date unmarshall(JsonUnmarshallerContext context) throws Exception {
         return (Date)context.getJsonParser().getEmbeddedObject();
      }

      public static SimpleTypeIonUnmarshallers.DateIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DoubleIonUnmarshaller implements Unmarshaller<Double, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.DoubleIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.DoubleIonUnmarshaller();

      public Double unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getDoubleValue();
      }

      public static SimpleTypeIonUnmarshallers.DoubleIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class FloatIonUnmarshaller implements Unmarshaller<Float, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.FloatIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.FloatIonUnmarshaller();

      public Float unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getFloatValue();
      }

      public static SimpleTypeIonUnmarshallers.FloatIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class IntegerIonUnmarshaller implements Unmarshaller<Integer, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.IntegerIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.IntegerIonUnmarshaller();

      public Integer unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getIntValue();
      }

      public static SimpleTypeIonUnmarshallers.IntegerIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class LongIonUnmarshaller implements Unmarshaller<Long, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.LongIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.LongIonUnmarshaller();

      public Long unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getLongValue();
      }

      public static SimpleTypeIonUnmarshallers.LongIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ShortIonUnmarshaller implements Unmarshaller<Short, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.ShortIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.ShortIonUnmarshaller();

      public Short unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.getJsonParser().getShortValue();
      }

      public static SimpleTypeIonUnmarshallers.ShortIonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class StringIonUnmarshaller implements Unmarshaller<String, JsonUnmarshallerContext> {
      private static final SimpleTypeIonUnmarshallers.StringIonUnmarshaller instance = new SimpleTypeIonUnmarshallers.StringIonUnmarshaller();

      public String unmarshall(JsonUnmarshallerContext context) throws Exception {
         return context.readText();
      }

      public static SimpleTypeIonUnmarshallers.StringIonUnmarshaller getInstance() {
         return instance;
      }
   }
}
