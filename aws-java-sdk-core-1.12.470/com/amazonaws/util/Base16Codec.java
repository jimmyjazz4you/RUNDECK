package com.amazonaws.util;

class Base16Codec implements Codec {
   private static final int OFFSET_OF_a = 87;
   private static final int OFFSET_OF_A = 55;
   private static final int MASK_4BITS = 15;
   private final byte[] alphabets;

   Base16Codec() {
      this(true);
   }

   Base16Codec(boolean upperCase) {
      this.alphabets = upperCase ? CodecUtils.toBytesDirect("0123456789ABCDEF") : CodecUtils.toBytesDirect("0123456789abcdef");
   }

   @Override
   public byte[] encode(byte[] src) {
      byte[] dest = new byte[src.length * 2];
      int i = 0;

      for(int j = 0; i < src.length; ++i) {
         byte p;
         dest[j++] = this.alphabets[(p = src[i]) >>> 4 & 15];
         dest[j++] = this.alphabets[p & 15];
      }

      return dest;
   }

   @Override
   public byte[] decode(byte[] src, int length) {
      if (length % 2 != 0) {
         throw new IllegalArgumentException("Input is expected to be encoded in multiple of 2 bytes but found: " + length);
      } else {
         byte[] dest = new byte[length / 2];
         int i = 0;

         for(int j = 0; j < dest.length; ++j) {
            dest[j] = (byte)(this.pos(src[i++]) << 4 | this.pos(src[i++]));
         }

         return dest;
      }
   }

   protected int pos(byte in) {
      int pos = Base16Codec.LazyHolder.DECODED[in];
      if (pos > -1) {
         return pos;
      } else {
         throw new IllegalArgumentException("Invalid base 16 character: '" + (char)in + "'");
      }
   }

   private static class LazyHolder {
      private static final byte[] DECODED = decodeTable();

      private static byte[] decodeTable() {
         byte[] dest = new byte[103];

         for(int i = 0; i <= 102; ++i) {
            if (i >= 48 && i <= 57) {
               dest[i] = (byte)(i - 48);
            } else if (i >= 65 && i <= 70) {
               dest[i] = (byte)(i - 55);
            } else if (i >= 97 && i <= 102) {
               dest[i] = (byte)(i - 87);
            } else {
               dest[i] = -1;
            }
         }

         return dest;
      }
   }
}
