package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.TimestampFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Date;

@SdkProtectedApi
public interface StructuredJsonGenerator {
   StructuredJsonGenerator NO_OP = new StructuredJsonGenerator() {
      private final byte[] EMPTY_BYTES = new byte[0];

      @Override
      public StructuredJsonGenerator writeStartArray() {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeEndArray() {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeNull() {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeStartObject() {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeEndObject() {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeFieldName(String fieldName) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(String val) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(boolean bool) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(long val) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(double val) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(float val) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(short val) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(int val) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(ByteBuffer bytes) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(Date date, TimestampFormat timestampFormat) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(BigDecimal value) {
         return this;
      }

      @Override
      public StructuredJsonGenerator writeValue(BigInteger value) {
         return this;
      }

      @Override
      public byte[] getBytes() {
         return this.EMPTY_BYTES;
      }

      @Override
      public String getContentType() {
         return null;
      }
   };

   StructuredJsonGenerator writeStartArray();

   StructuredJsonGenerator writeEndArray();

   StructuredJsonGenerator writeNull();

   StructuredJsonGenerator writeStartObject();

   StructuredJsonGenerator writeEndObject();

   StructuredJsonGenerator writeFieldName(String var1);

   StructuredJsonGenerator writeValue(String var1);

   StructuredJsonGenerator writeValue(boolean var1);

   StructuredJsonGenerator writeValue(long var1);

   StructuredJsonGenerator writeValue(double var1);

   StructuredJsonGenerator writeValue(float var1);

   StructuredJsonGenerator writeValue(short var1);

   StructuredJsonGenerator writeValue(int var1);

   StructuredJsonGenerator writeValue(ByteBuffer var1);

   StructuredJsonGenerator writeValue(Date var1, TimestampFormat var2);

   StructuredJsonGenerator writeValue(BigDecimal var1);

   StructuredJsonGenerator writeValue(BigInteger var1);

   byte[] getBytes();

   @Deprecated
   String getContentType();
}
