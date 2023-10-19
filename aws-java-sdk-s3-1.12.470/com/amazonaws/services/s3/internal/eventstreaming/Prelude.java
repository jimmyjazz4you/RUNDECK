package com.amazonaws.services.s3.internal.eventstreaming;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

final class Prelude {
   static final int LENGTH = 8;
   static final int LENGTH_WITH_CRC = 12;
   private final int totalLength;
   private final long headersLength;

   private Prelude(int totalLength, long headersLength) {
      this.totalLength = totalLength;
      this.headersLength = headersLength;
   }

   static Prelude decode(ByteBuffer buf) {
      buf = buf.duplicate();
      long computedPreludeCrc = computePreludeCrc(buf);
      long totalLength = intToUnsignedLong(buf.getInt());
      long headersLength = intToUnsignedLong(buf.getInt());
      long wirePreludeCrc = intToUnsignedLong(buf.getInt());
      if (computedPreludeCrc != wirePreludeCrc) {
         throw new IllegalArgumentException(String.format("Prelude checksum failure: expected 0x%x, computed 0x%x", wirePreludeCrc, computedPreludeCrc));
      } else if (headersLength >= 0L && headersLength <= 131072L) {
         long payloadLength = totalLength - headersLength - 16L;
         if (payloadLength >= 0L && payloadLength <= 16777216L) {
            return new Prelude(toIntExact(totalLength), headersLength);
         } else {
            throw new IllegalArgumentException("Illegal payload size: " + payloadLength);
         }
      } else {
         throw new IllegalArgumentException("Illegal headers_length value: " + headersLength);
      }
   }

   private static long intToUnsignedLong(int i) {
      return (long)i & 4294967295L;
   }

   private static int toIntExact(long value) {
      if ((long)((int)value) != value) {
         throw new ArithmeticException("integer overflow");
      } else {
         return (int)value;
      }
   }

   private static long computePreludeCrc(ByteBuffer buf) {
      byte[] prelude = new byte[8];
      buf.duplicate().get(prelude);
      Checksum crc = new CRC32();
      crc.update(prelude, 0, prelude.length);
      return crc.getValue();
   }

   int getTotalLength() {
      return this.totalLength;
   }

   long getHeadersLength() {
      return this.headersLength;
   }
}
