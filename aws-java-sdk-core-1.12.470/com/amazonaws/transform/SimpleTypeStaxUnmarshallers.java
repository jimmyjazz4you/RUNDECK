package com.amazonaws.transform;

import com.amazonaws.SdkClientException;
import com.amazonaws.util.Base64;
import com.amazonaws.util.DateUtils;
import com.amazonaws.util.TimestampFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleTypeStaxUnmarshallers {
   private static Log log = LogFactory.getLog(SimpleTypeStaxUnmarshallers.class);

   public static class BigDecimalStaxUnmarshaller implements Unmarshaller<BigDecimal, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.BigDecimalStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.BigDecimalStaxUnmarshaller();

      public BigDecimal unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String s = unmarshallerContext.readText();
         return s == null ? null : new BigDecimal(s);
      }

      public static SimpleTypeStaxUnmarshallers.BigDecimalStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BigIntegerStaxUnmarshaller implements Unmarshaller<BigInteger, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.BigIntegerStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.BigIntegerStaxUnmarshaller();

      public BigInteger unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String s = unmarshallerContext.readText();
         return s == null ? null : new BigInteger(s);
      }

      public static SimpleTypeStaxUnmarshallers.BigIntegerStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class BooleanStaxUnmarshaller implements Unmarshaller<Boolean, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.BooleanStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.BooleanStaxUnmarshaller();

      public Boolean unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String booleanString = unmarshallerContext.readText();
         return booleanString == null ? null : Boolean.parseBoolean(booleanString);
      }

      public static SimpleTypeStaxUnmarshallers.BooleanStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteBufferStaxUnmarshaller implements Unmarshaller<ByteBuffer, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.ByteBufferStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.ByteBufferStaxUnmarshaller();

      public ByteBuffer unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String base64EncodedString = unmarshallerContext.readText();
         byte[] decodedBytes = Base64.decode(base64EncodedString);
         return ByteBuffer.wrap(decodedBytes);
      }

      public static SimpleTypeStaxUnmarshallers.ByteBufferStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ByteStaxUnmarshaller implements Unmarshaller<Byte, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.ByteStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.ByteStaxUnmarshaller();

      public Byte unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String byteString = unmarshallerContext.readText();
         return byteString == null ? null : Byte.valueOf(byteString);
      }

      public static SimpleTypeStaxUnmarshallers.ByteStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class CharacterJsonUnmarshaller implements Unmarshaller<Character, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.CharacterJsonUnmarshaller instance = new SimpleTypeStaxUnmarshallers.CharacterJsonUnmarshaller();

      public Character unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
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

      public static SimpleTypeStaxUnmarshallers.CharacterJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DateStaxUnmarshaller implements Unmarshaller<Date, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.DateStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.DateStaxUnmarshaller();

      public Date unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String dateString = unmarshallerContext.readText();
         if (dateString == null) {
            return null;
         } else {
            try {
               return DateUtils.parseISO8601Date(dateString);
            } catch (Exception var4) {
               SimpleTypeStaxUnmarshallers.log.warn("Unable to parse date '" + dateString + "':  " + var4.getMessage(), var4);
               return null;
            }
         }
      }

      public static SimpleTypeStaxUnmarshallers.DateStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class DateStaxUnmarshallerFactory implements Unmarshaller<Date, StaxUnmarshallerContext> {
      private final String dateFormatType;

      private DateStaxUnmarshallerFactory(String dateFormatType) {
         this.dateFormatType = dateFormatType;
      }

      public Date unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String dateString = unmarshallerContext.readText();
         if (dateString == null) {
            return null;
         } else {
            try {
               if (TimestampFormat.RFC_822.getFormat().equals(this.dateFormatType)) {
                  return DateUtils.parseRFC822Date(dateString);
               } else {
                  return TimestampFormat.UNIX_TIMESTAMP.getFormat().equals(this.dateFormatType)
                     ? DateUtils.parseServiceSpecificDate(dateString)
                     : DateUtils.parseISO8601Date(dateString);
               }
            } catch (Exception var4) {
               SimpleTypeStaxUnmarshallers.log.warn("Unable to parse date '" + dateString + "':  " + var4.getMessage(), var4);
               return null;
            }
         }
      }

      public static SimpleTypeStaxUnmarshallers.DateStaxUnmarshallerFactory getInstance(String dateFormatType) {
         return new SimpleTypeStaxUnmarshallers.DateStaxUnmarshallerFactory(dateFormatType);
      }
   }

   public static class DoubleStaxUnmarshaller implements Unmarshaller<Double, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.DoubleStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.DoubleStaxUnmarshaller();

      public Double unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String doubleString = unmarshallerContext.readText();
         return doubleString == null ? null : Double.parseDouble(doubleString);
      }

      public static SimpleTypeStaxUnmarshallers.DoubleStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class FloatStaxUnmarshaller implements Unmarshaller<Float, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.FloatStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.FloatStaxUnmarshaller();

      public Float unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String floatString = unmarshallerContext.readText();
         return floatString == null ? null : Float.valueOf(floatString);
      }

      public static SimpleTypeStaxUnmarshallers.FloatStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class IntegerStaxUnmarshaller implements Unmarshaller<Integer, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.IntegerStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.IntegerStaxUnmarshaller();

      public Integer unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String intString = unmarshallerContext.readText();
         return intString == null ? null : Integer.parseInt(intString);
      }

      public static SimpleTypeStaxUnmarshallers.IntegerStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class LongStaxUnmarshaller implements Unmarshaller<Long, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.LongStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.LongStaxUnmarshaller();

      public Long unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String longString = unmarshallerContext.readText();
         return longString == null ? null : Long.parseLong(longString);
      }

      public static SimpleTypeStaxUnmarshallers.LongStaxUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class ShortJsonUnmarshaller implements Unmarshaller<Short, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.ShortJsonUnmarshaller instance = new SimpleTypeStaxUnmarshallers.ShortJsonUnmarshaller();

      public Short unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         String shortString = unmarshallerContext.readText();
         return shortString == null ? null : Short.valueOf(shortString);
      }

      public static SimpleTypeStaxUnmarshallers.ShortJsonUnmarshaller getInstance() {
         return instance;
      }
   }

   public static class StringStaxUnmarshaller implements Unmarshaller<String, StaxUnmarshallerContext> {
      private static final SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller instance = new SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller();

      public String unmarshall(StaxUnmarshallerContext unmarshallerContext) throws Exception {
         return unmarshallerContext.readText();
      }

      public static SimpleTypeStaxUnmarshallers.StringStaxUnmarshaller getInstance() {
         return instance;
      }
   }
}
