package com.amazonaws.transform;

import com.amazonaws.SdkClientException;
import com.amazonaws.util.Base64;
import com.amazonaws.util.DateUtils;
import com.amazonaws.util.TimestampFormat;
import com.fasterxml.jackson.core.JsonToken;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

public class SimpleTypeJsonUnmarshallers {
   public static class BigDecimalJsonUnmarshaller implements Unmarshaller<BigDecimal, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.BigDecimalJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.BigDecimalJsonUnmarshaller();

      public BigDecimal unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String s = unmarshallerContext.readText();
         return s == null ? null : new BigDecimal(s);
      }

      public static SimpleTypeJsonUnmarshallers.BigDecimalJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BigIntegerJsonUnmarshaller implements Unmarshaller<BigInteger, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.BigIntegerJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.BigIntegerJsonUnmarshaller();

      public BigInteger unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String intString = unmarshallerContext.readText();
         return intString == null ? null : new BigInteger(intString);
      }

      public static SimpleTypeJsonUnmarshallers.BigIntegerJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BooleanJsonUnmarshaller implements Unmarshaller<Boolean, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.BooleanJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.BooleanJsonUnmarshaller();

      public Boolean unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String booleanString = unmarshallerContext.readText();
         return booleanString == null ? null : Boolean.parseBoolean(booleanString);
      }

      public static SimpleTypeJsonUnmarshallers.BooleanJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteBufferJsonUnmarshaller implements Unmarshaller<ByteBuffer, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.ByteBufferJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.ByteBufferJsonUnmarshaller();

      public ByteBuffer unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String base64EncodedString = unmarshallerContext.readText();
         if (base64EncodedString == null) {
            return null;
         } else {
            byte[] decodedBytes = Base64.decode(base64EncodedString);
            return ByteBuffer.wrap(decodedBytes);
         }
      }

      public static SimpleTypeJsonUnmarshallers.ByteBufferJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteJsonUnmarshaller implements Unmarshaller<Byte, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.ByteJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.ByteJsonUnmarshaller();

      public Byte unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String byteString = unmarshallerContext.readText();
         return byteString == null ? null : Byte.valueOf(byteString);
      }

      public static SimpleTypeJsonUnmarshallers.ByteJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class CharacterJsonUnmarshaller implements Unmarshaller<Character, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.CharacterJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.CharacterJsonUnmarshaller();

      public Character unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String charString = unmarshallerContext.readText();
         if (charString == null) {
            return null;
         } else {
            charString = charString.trim();
            if (!charString.isEmpty() && charString.length() <= 1) {
               return charString.charAt(0);
            } else {
               throw new SdkClientException("'" + charString + "' cannot be converted to Character");
            }
         }
      }

      public static SimpleTypeJsonUnmarshallers.CharacterJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DateJsonUnmarshaller implements Unmarshaller<Date, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.DateJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.DateJsonUnmarshaller();

      public Date unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.getCurrentToken() == JsonToken.VALUE_STRING
            ? DateUtils.parseISO8601Date(unmarshallerContext.readText())
            : DateUtils.parseServiceSpecificDate(unmarshallerContext.readText());
      }

      public static SimpleTypeJsonUnmarshallers.DateJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DateJsonUnmarshallerFactory implements Unmarshaller<Date, JsonUnmarshallerContext> {
      private final String dateFormatType;

      private DateJsonUnmarshallerFactory(String dateFormatType) {
         this.dateFormatType = dateFormatType;
      }

      public Date unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String dateString = unmarshallerContext.readText();
         if (dateString == null) {
            return null;
         } else {
            try {
               if (TimestampFormat.RFC_822.getFormat().equals(this.dateFormatType)) {
                  return DateUtils.parseRFC822Date(dateString);
               } else if (TimestampFormat.UNIX_TIMESTAMP.getFormat().equals(this.dateFormatType)) {
                  return DateUtils.parseServiceSpecificDate(dateString);
               } else {
                  return TimestampFormat.UNIX_TIMESTAMP_IN_MILLIS.getFormat().equals(this.dateFormatType)
                     ? DateUtils.parseUnixTimestampInMillis(dateString)
                     : DateUtils.parseISO8601Date(dateString);
               }
            } catch (Exception var4) {
               return SimpleTypeJsonUnmarshallers.DateJsonUnmarshaller.getInstance().unmarshall(unmarshallerContext);
            }
         }
      }

      public static SimpleTypeJsonUnmarshallers.DateJsonUnmarshallerFactory getInstance(String dateFormatType) {
         return new SimpleTypeJsonUnmarshallers.DateJsonUnmarshallerFactory(dateFormatType);
      }
   }

   public static class DoubleJsonUnmarshaller implements Unmarshaller<Double, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.DoubleJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.DoubleJsonUnmarshaller();

      public Double unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String doubleString = unmarshallerContext.readText();
         return doubleString == null ? null : Double.parseDouble(doubleString);
      }

      public static SimpleTypeJsonUnmarshallers.DoubleJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class FloatJsonUnmarshaller implements Unmarshaller<Float, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.FloatJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.FloatJsonUnmarshaller();

      public Float unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String floatString = unmarshallerContext.readText();
         return floatString == null ? null : Float.valueOf(floatString);
      }

      public static SimpleTypeJsonUnmarshallers.FloatJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class IntegerJsonUnmarshaller implements Unmarshaller<Integer, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.IntegerJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.IntegerJsonUnmarshaller();

      public Integer unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String intString = unmarshallerContext.readText();
         return intString == null ? null : Integer.parseInt(intString);
      }

      public static SimpleTypeJsonUnmarshallers.IntegerJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class JsonValueStringUnmarshaller extends SimpleTypeJsonUnmarshallers.StringJsonUnmarshaller {
      private static final SimpleTypeJsonUnmarshallers.JsonValueStringUnmarshaller INSTANCE = new SimpleTypeJsonUnmarshallers.JsonValueStringUnmarshaller();

      @Override
      public String unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String stringValue = super.unmarshall(unmarshallerContext);
         return !unmarshallerContext.isInsideResponseHeader() ? stringValue : new String(Base64.decode(stringValue), Charset.forName("utf-8"));
      }

      public static SimpleTypeJsonUnmarshallers.JsonValueStringUnmarshaller getInstance() {
         return INSTANCE;
      }
   }

   public static class LongJsonUnmarshaller implements Unmarshaller<Long, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.LongJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.LongJsonUnmarshaller();

      public Long unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String longString = unmarshallerContext.readText();
         return longString == null ? null : Long.parseLong(longString);
      }

      public static SimpleTypeJsonUnmarshallers.LongJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ShortJsonUnmarshaller implements Unmarshaller<Short, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.ShortJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.ShortJsonUnmarshaller();

      public Short unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         String shortString = unmarshallerContext.readText();
         return shortString == null ? null : Short.valueOf(shortString);
      }

      public static SimpleTypeJsonUnmarshallers.ShortJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class StringJsonUnmarshaller implements Unmarshaller<String, JsonUnmarshallerContext> {
      private static final SimpleTypeJsonUnmarshallers.StringJsonUnmarshaller instance = new SimpleTypeJsonUnmarshallers.StringJsonUnmarshaller();

      public String unmarshall(JsonUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.readText();
      }

      public static SimpleTypeJsonUnmarshallers.StringJsonUnmarshaller getInstance() {
         return instance;
      }
   }
}
