package com.amazonaws.services.s3.internal.eventstreaming;

import com.amazonaws.util.Base64;
import com.amazonaws.util.ValidationUtils;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import org.joda.time.DateTime;

public abstract class HeaderValue {
   public static HeaderValue fromBoolean(boolean value) {
      return new HeaderValue.BooleanValue(value);
   }

   public static HeaderValue fromByte(byte value) {
      return new HeaderValue.ByteValue(value);
   }

   public static HeaderValue fromShort(short value) {
      return new HeaderValue.ShortValue(value);
   }

   public static HeaderValue fromInteger(int value) {
      return new HeaderValue.IntegerValue(value);
   }

   public static HeaderValue fromLong(long value) {
      return new HeaderValue.LongValue(value);
   }

   public static HeaderValue fromByteArray(byte[] bytes) {
      return new HeaderValue.ByteArrayValue(bytes);
   }

   public static HeaderValue fromByteBuffer(ByteBuffer buf) {
      buf = buf.duplicate();
      byte[] bytes = new byte[buf.remaining()];
      buf.get(bytes);
      return fromByteArray(bytes);
   }

   public static HeaderValue fromString(String string) {
      return new HeaderValue.StringValue(string);
   }

   public static HeaderValue fromTimestamp(DateTime value) {
      return new HeaderValue.TimestampValue(value);
   }

   public static HeaderValue fromDate(Date value) {
      return new HeaderValue.TimestampValue(new DateTime(value));
   }

   public static HeaderValue fromUuid(UUID value) {
      return new HeaderValue.UuidValue(value);
   }

   protected HeaderValue() {
   }

   public abstract HeaderType getType();

   public boolean getBoolean() {
      throw new IllegalStateException();
   }

   public byte getByte() {
      throw new IllegalStateException("Expected byte, but type was " + this.getType().name());
   }

   public short getShort() {
      throw new IllegalStateException("Expected short, but type was " + this.getType().name());
   }

   public int getInteger() {
      throw new IllegalStateException("Expected integer, but type was " + this.getType().name());
   }

   public long getLong() {
      throw new IllegalStateException("Expected long, but type was " + this.getType().name());
   }

   public byte[] getByteArray() {
      throw new IllegalStateException();
   }

   public final ByteBuffer getByteBuffer() {
      return ByteBuffer.wrap(this.getByteArray());
   }

   public String getString() {
      throw new IllegalStateException();
   }

   public DateTime getTimestamp() {
      throw new IllegalStateException("Expected timestamp, but type was " + this.getType().name());
   }

   public Date getDate() {
      return this.getTimestamp().toDate();
   }

   public UUID getUuid() {
      throw new IllegalStateException("Expected UUID, but type was " + this.getType().name());
   }

   void encode(DataOutputStream dos) throws IOException {
      dos.writeByte(this.getType().headerTypeId);
      this.encodeValue(dos);
   }

   abstract void encodeValue(DataOutputStream var1) throws IOException;

   static HeaderValue decode(ByteBuffer buf) {
      HeaderType type = HeaderType.fromTypeId(buf.get());
      switch(type) {
         case TRUE:
            return new HeaderValue.BooleanValue(true);
         case FALSE:
            return new HeaderValue.BooleanValue(false);
         case BYTE:
            return new HeaderValue.ByteValue(buf.get());
         case SHORT:
            return new HeaderValue.ShortValue(buf.getShort());
         case INTEGER:
            return fromInteger(buf.getInt());
         case LONG:
            return new HeaderValue.LongValue(buf.getLong());
         case BYTE_ARRAY:
            return fromByteArray(Utils.readBytes(buf));
         case STRING:
            return fromString(Utils.readString(buf));
         case TIMESTAMP:
            return HeaderValue.TimestampValue.decode(buf);
         case UUID:
            return HeaderValue.UuidValue.decode(buf);
         default:
            throw new IllegalStateException();
      }
   }

   private static final class BooleanValue extends HeaderValue {
      private final boolean value;

      private BooleanValue(boolean value) {
         this.value = value;
      }

      @Override
      public HeaderType getType() {
         return this.value ? HeaderType.TRUE : HeaderType.FALSE;
      }

      @Override
      public boolean getBoolean() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) {
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.BooleanValue that = (HeaderValue.BooleanValue)o;
            return this.value == that.value;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.value ? 1 : 0;
      }

      @Override
      public String toString() {
         return String.valueOf(this.value);
      }
   }

   private static final class ByteArrayValue extends HeaderValue {
      private final byte[] value;

      private ByteArrayValue(byte[] value) {
         this.value = (byte[])ValidationUtils.assertNotNull(value, "value");
      }

      @Override
      public HeaderType getType() {
         return HeaderType.BYTE_ARRAY;
      }

      @Override
      public byte[] getByteArray() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) throws IOException {
         Utils.writeBytes(dos, this.value);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.ByteArrayValue that = (HeaderValue.ByteArrayValue)o;
            return Arrays.equals(this.value, that.value);
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return Arrays.hashCode(this.value);
      }

      @Override
      public String toString() {
         return Base64.encodeAsString(this.value);
      }
   }

   private static final class ByteValue extends HeaderValue {
      private final byte value;

      private ByteValue(byte value) {
         this.value = value;
      }

      @Override
      public HeaderType getType() {
         return HeaderType.BYTE;
      }

      @Override
      public byte getByte() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) {
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.ByteValue that = (HeaderValue.ByteValue)o;
            return this.value == that.value;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.value;
      }

      @Override
      public String toString() {
         return String.valueOf(this.value);
      }
   }

   private static final class IntegerValue extends HeaderValue {
      private final int value;

      private IntegerValue(int value) {
         this.value = value;
      }

      @Override
      public HeaderType getType() {
         return HeaderType.INTEGER;
      }

      @Override
      public int getInteger() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) throws IOException {
         dos.writeInt(this.value);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.IntegerValue that = (HeaderValue.IntegerValue)o;
            return this.value == that.value;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.value;
      }

      @Override
      public String toString() {
         return String.valueOf(this.value);
      }
   }

   private static final class LongValue extends HeaderValue {
      private final long value;

      private LongValue(long value) {
         this.value = value;
      }

      @Override
      public HeaderType getType() {
         return HeaderType.LONG;
      }

      @Override
      public long getLong() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) throws IOException {
         dos.writeLong(this.value);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.LongValue longValue = (HeaderValue.LongValue)o;
            return this.value == longValue.value;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return (int)(this.value ^ this.value >>> 32);
      }

      @Override
      public String toString() {
         return String.valueOf(this.value);
      }
   }

   private static final class ShortValue extends HeaderValue {
      private final short value;

      private ShortValue(short value) {
         this.value = value;
      }

      @Override
      public HeaderType getType() {
         return HeaderType.SHORT;
      }

      @Override
      public short getShort() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) {
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.ShortValue that = (HeaderValue.ShortValue)o;
            return this.value == that.value;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.value;
      }

      @Override
      public String toString() {
         return String.valueOf(this.value);
      }
   }

   private static final class StringValue extends HeaderValue {
      private final String value;

      private StringValue(String value) {
         this.value = (String)ValidationUtils.assertNotNull(value, "value");
      }

      @Override
      public HeaderType getType() {
         return HeaderType.STRING;
      }

      @Override
      public String getString() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) throws IOException {
         Utils.writeString(dos, this.value);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.StringValue that = (HeaderValue.StringValue)o;
            return this.value.equals(that.value);
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.value.hashCode();
      }

      @Override
      public String toString() {
         return '"' + this.value + '"';
      }
   }

   private static final class TimestampValue extends HeaderValue {
      private final DateTime value;

      private TimestampValue(DateTime value) {
         this.value = (DateTime)ValidationUtils.assertNotNull(value, "value");
      }

      static HeaderValue.TimestampValue decode(ByteBuffer buf) {
         long epochMillis = buf.getLong();
         return new HeaderValue.TimestampValue(new DateTime(epochMillis));
      }

      @Override
      public HeaderType getType() {
         return HeaderType.TIMESTAMP;
      }

      @Override
      public DateTime getTimestamp() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) throws IOException {
         dos.writeLong(this.value.getMillis());
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.TimestampValue that = (HeaderValue.TimestampValue)o;
            return this.value.equals(that.value);
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.value.hashCode();
      }

      @Override
      public String toString() {
         return this.value.toString();
      }
   }

   private static final class UuidValue extends HeaderValue {
      private final UUID value;

      private UuidValue(UUID value) {
         this.value = (UUID)ValidationUtils.assertNotNull(value, "value");
      }

      static HeaderValue.UuidValue decode(ByteBuffer buf) {
         long msb = buf.getLong();
         long lsb = buf.getLong();
         return new HeaderValue.UuidValue(new UUID(msb, lsb));
      }

      @Override
      public HeaderType getType() {
         return HeaderType.UUID;
      }

      @Override
      public UUID getUuid() {
         return this.value;
      }

      @Override
      void encodeValue(DataOutputStream dos) throws IOException {
         dos.writeLong(this.value.getMostSignificantBits());
         dos.writeLong(this.value.getLeastSignificantBits());
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            HeaderValue.UuidValue uuidValue = (HeaderValue.UuidValue)o;
            return this.value.equals(uuidValue.value);
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return this.value.hashCode();
      }

      @Override
      public String toString() {
         return this.value.toString();
      }
   }
}
