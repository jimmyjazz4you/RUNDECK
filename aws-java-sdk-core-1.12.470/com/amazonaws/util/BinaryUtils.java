package com.amazonaws.util;

import java.io.ByteArrayInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BinaryUtils {
   public static String toHex(byte[] data) {
      return Base16Lower.encodeAsString(data);
   }

   public static byte[] fromHex(String hexData) {
      return Base16Lower.decode(hexData);
   }

   public static String toBase64(byte[] data) {
      return Base64.encodeAsString(data);
   }

   public static byte[] fromBase64(String b64Data) {
      return b64Data == null ? null : Base64.decode(b64Data);
   }

   public static ByteArrayInputStream toStream(ByteBuffer byteBuffer) {
      return byteBuffer == null ? new ByteArrayInputStream(new byte[0]) : new ByteArrayInputStream(copyBytesFrom(byteBuffer));
   }

   public static byte[] copyAllBytesFrom(ByteBuffer bb) {
      if (bb == null) {
         return null;
      } else if (bb.hasArray()) {
         return Arrays.copyOfRange(bb.array(), bb.arrayOffset(), bb.arrayOffset() + bb.limit());
      } else {
         ByteBuffer copy = bb.asReadOnlyBuffer();
         ((Buffer)copy).rewind();
         byte[] dst = new byte[copy.remaining()];
         copy.get(dst);
         return dst;
      }
   }

   public static byte[] copyBytesFrom(ByteBuffer bb) {
      if (bb == null) {
         return null;
      } else if (bb.hasArray()) {
         return Arrays.copyOfRange(bb.array(), bb.arrayOffset() + bb.position(), bb.arrayOffset() + bb.limit());
      } else {
         byte[] dst = new byte[bb.remaining()];
         bb.asReadOnlyBuffer().get(dst);
         return dst;
      }
   }
}
