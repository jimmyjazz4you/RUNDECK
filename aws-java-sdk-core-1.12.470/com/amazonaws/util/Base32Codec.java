package com.amazonaws.util;

class Base32Codec extends AbstractBase32Codec {
   private static final int OFFSET_OF_2 = 24;

   private static byte[] alphabets() {
      return CodecUtils.toBytesDirect("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567");
   }

   Base32Codec() {
      super(alphabets());
   }

   @Override
   protected int pos(byte in) {
      int pos = Base32Codec.LazyHolder.DECODED[in];
      if (pos > -1) {
         return pos;
      } else {
         throw new IllegalArgumentException("Invalid base 32 character: '" + (char)in + "'");
      }
   }

   private static class LazyHolder {
      private static final byte[] DECODED = decodeTable();

      private static byte[] decodeTable() {
         byte[] dest = new byte[123];

         for(int i = 0; i <= 122; ++i) {
            if (i >= 65 && i <= 90) {
               dest[i] = (byte)(i - 65);
            } else if (i >= 50 && i <= 55) {
               dest[i] = (byte)(i - 24);
            } else if (i >= 97 && i <= 122) {
               dest[i] = (byte)(i - 97);
            } else {
               dest[i] = -1;
            }
         }

         return dest;
      }
   }
}
