package com.amazonaws.util;

public enum Base16Lower {
   private static final Base16Codec codec = new Base16Codec(false);

   public static String encodeAsString(byte... bytes) {
      if (bytes == null) {
         return null;
      } else {
         return bytes.length == 0 ? "" : CodecUtils.toStringDirect(codec.encode(bytes));
      }
   }

   public static byte[] encode(byte[] bytes) {
      return bytes != null && bytes.length != 0 ? codec.encode(bytes) : bytes;
   }

   public static byte[] decode(String b16) {
      if (b16 == null) {
         return null;
      } else if (b16.length() == 0) {
         return new byte[0];
      } else {
         byte[] buf = new byte[b16.length()];
         int len = CodecUtils.sanitize(b16, buf);
         return codec.decode(buf, len);
      }
   }

   public static byte[] decode(byte[] b16) {
      return b16 != null && b16.length != 0 ? codec.decode(b16, b16.length) : b16;
   }
}
