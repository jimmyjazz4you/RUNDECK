package com.amazonaws.transform;

import com.amazonaws.util.XpathUtils;
import java.nio.ByteBuffer;
import java.util.Date;
import org.w3c.dom.Node;

public class SimpleTypeUnmarshallers {
   public static class BooleanUnmarshaller implements Unmarshaller<Boolean, Node> {
      private static SimpleTypeUnmarshallers.BooleanUnmarshaller instance;

      public Boolean unmarshall(Node in) throws Exception {
         return XpathUtils.asBoolean(".", in);
      }

      public static SimpleTypeUnmarshallers.BooleanUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.BooleanUnmarshaller();
         }

         return instance;
      }
   }

   public static class ByteBufferUnmarshaller implements Unmarshaller<ByteBuffer, Node> {
      private static SimpleTypeUnmarshallers.ByteBufferUnmarshaller instance;

      public ByteBuffer unmarshall(Node in) throws Exception {
         return XpathUtils.asByteBuffer(".", in);
      }

      public static SimpleTypeUnmarshallers.ByteBufferUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.ByteBufferUnmarshaller();
         }

         return instance;
      }
   }

   public static class ByteUnmarshaller implements Unmarshaller<Byte, Node> {
      private static SimpleTypeUnmarshallers.ByteUnmarshaller instance;

      public Byte unmarshall(Node in) throws Exception {
         return XpathUtils.asByte(".", in);
      }

      public static SimpleTypeUnmarshallers.ByteUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.ByteUnmarshaller();
         }

         return instance;
      }
   }

   public static class DateUnmarshaller implements Unmarshaller<Date, Node> {
      private static SimpleTypeUnmarshallers.DateUnmarshaller instance;

      public Date unmarshall(Node in) throws Exception {
         return XpathUtils.asDate(".", in);
      }

      public static SimpleTypeUnmarshallers.DateUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.DateUnmarshaller();
         }

         return instance;
      }
   }

   public static class DoubleUnmarshaller implements Unmarshaller<Double, Node> {
      private static SimpleTypeUnmarshallers.DoubleUnmarshaller instance;

      public Double unmarshall(Node in) throws Exception {
         return XpathUtils.asDouble(".", in);
      }

      public static SimpleTypeUnmarshallers.DoubleUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.DoubleUnmarshaller();
         }

         return instance;
      }
   }

   public static class FloatUnmarshaller implements Unmarshaller<Float, Node> {
      private static SimpleTypeUnmarshallers.FloatUnmarshaller instance;

      public Float unmarshall(Node in) throws Exception {
         return XpathUtils.asFloat(".", in);
      }

      public static SimpleTypeUnmarshallers.FloatUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.FloatUnmarshaller();
         }

         return instance;
      }
   }

   public static class IntegerUnmarshaller implements Unmarshaller<Integer, Node> {
      private static SimpleTypeUnmarshallers.IntegerUnmarshaller instance;

      public Integer unmarshall(Node in) throws Exception {
         return XpathUtils.asInteger(".", in);
      }

      public static SimpleTypeUnmarshallers.IntegerUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.IntegerUnmarshaller();
         }

         return instance;
      }
   }

   public static class LongUnmarshaller implements Unmarshaller<Long, Node> {
      private static SimpleTypeUnmarshallers.LongUnmarshaller instance;

      public Long unmarshall(Node in) throws Exception {
         return XpathUtils.asLong(".", in);
      }

      public static SimpleTypeUnmarshallers.LongUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.LongUnmarshaller();
         }

         return instance;
      }
   }

   public static class StringUnmarshaller implements Unmarshaller<String, Node> {
      private static SimpleTypeUnmarshallers.StringUnmarshaller instance;

      public String unmarshall(Node in) throws Exception {
         return XpathUtils.asString(".", in);
      }

      public static SimpleTypeUnmarshallers.StringUnmarshaller getInstance() {
         if (instance == null) {
            instance = new SimpleTypeUnmarshallers.StringUnmarshaller();
         }

         return instance;
      }
   }
}
