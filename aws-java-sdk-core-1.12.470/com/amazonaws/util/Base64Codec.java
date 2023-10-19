package com.amazonaws.util;

class Base64Codec implements Codec {
   private static final int OFFSET_OF_a = 71;
   private static final int OFFSET_OF_0 = -4;
   private static final int OFFSET_OF_PLUS = -19;
   private static final int OFFSET_OF_SLASH = -16;
   private static final int MASK_2BITS = 3;
   private static final int MASK_4BITS = 15;
   private static final int MASK_6BITS = 63;
   private static final byte PAD = 61;
   private final byte[] alphabets;

   Base64Codec() {
      this.alphabets = CodecUtils.toBytesDirect("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
   }

   protected Base64Codec(byte[] alphabets) {
      this.alphabets = alphabets;
   }

   @Override
   public byte[] encode(byte[] src) {
      int num3bytes = src.length / 3;
      int remainder = src.length % 3;
      if (remainder == 0) {
         byte[] dest = new byte[num3bytes * 4];
         int s = 0;

         for(int d = 0; s < src.length; d += 4) {
            this.encode3bytes(src, s, dest, d);
            s += 3;
         }

         return dest;
      } else {
         byte[] dest = new byte[(num3bytes + 1) * 4];
         int s = 0;

         int d;
         for(d = 0; s < src.length - remainder; d += 4) {
            this.encode3bytes(src, s, dest, d);
            s += 3;
         }

         switch(remainder) {
            case 1:
               this.encode1byte(src, s, dest, d);
               break;
            case 2:
               this.encode2bytes(src, s, dest, d);
               break;
            default:
               throw new IllegalStateException();
         }

         return dest;
      }
   }

   void encode3bytes(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s++]) >>> 2 & 63];
      byte var11;
      dest[d++] = this.alphabets[(p & 3) << 4 | (var11 = src[s++]) >>> 4 & 15];
      dest[d++] = this.alphabets[(var11 & 15) << 2 | (p = src[s]) >>> 6 & 3];
      dest[d] = this.alphabets[p & 63];
   }

   void encode2bytes(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s++]) >>> 2 & 63];
      byte var10;
      dest[d++] = this.alphabets[(p & 3) << 4 | (var10 = src[s]) >>> 4 & 15];
      dest[d++] = this.alphabets[(var10 & 15) << 2];
      dest[d] = 61;
   }

   void encode1byte(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s]) >>> 2 & 63];
      dest[d++] = this.alphabets[(p & 3) << 4];
      dest[d++] = 61;
      dest[d] = 61;
   }

   void decode4bytes(byte[] src, int s, byte[] dest, int d) {
      int p = 0;
      dest[d++] = (byte)(this.pos(src[s++]) << 2 | (p = this.pos(src[s++])) >>> 4 & 3);
      int var12;
      dest[d++] = (byte)((p & 15) << 4 | (var12 = this.pos(src[s++])) >>> 2 & 15);
      dest[d] = (byte)((var12 & 3) << 6 | this.pos(src[s]));
   }

   void decode1to3bytes(int n, byte[] src, int s, byte[] dest, int d) {
      int p = 0;
      dest[d++] = (byte)(this.pos(src[s++]) << 2 | (p = this.pos(src[s++])) >>> 4 & 3);
      if (n == 1) {
         CodecUtils.sanityCheckLastPos(p, 15);
      } else {
         int var13;
         dest[d++] = (byte)((p & 15) << 4 | (var13 = this.pos(src[s++])) >>> 2 & 15);
         if (n == 2) {
            CodecUtils.sanityCheckLastPos(var13, 3);
         } else {
            dest[d] = (byte)((var13 & 3) << 6 | this.pos(src[s]));
         }
      }
   }

   @Override
   public byte[] decode(byte[] src, int length) {
      if (length % 4 != 0) {
         throw new IllegalArgumentException("Input is expected to be encoded in multiple of 4 bytes but found: " + length);
      } else {
         int pads = 0;

         for(int last = length - 1; pads < 2 && last > -1 && src[last] == 61; ++pads) {
            --last;
         }

         int fq;
         switch(pads) {
            case 0:
               fq = 3;
               break;
            case 1:
               fq = 2;
               break;
            case 2:
               fq = 1;
               break;
            default:
               throw new Error("Impossible");
         }

         byte[] dest = new byte[length / 4 * 3 - (3 - fq)];
         int s = 0;

         int d;
         for(d = 0; d < dest.length - fq % 3; d += 3) {
            this.decode4bytes(src, s, dest, d);
            s += 4;
         }

         if (fq < 3) {
            this.decode1to3bytes(fq, src, s, dest, d);
         }

         return dest;
      }
   }

   protected int pos(byte in) {
      int pos = Base64Codec.LazyHolder.DECODED[in];
      if (pos > -1) {
         return pos;
      } else {
         throw new IllegalArgumentException("Invalid base 64 character: '" + (char)in + "'");
      }
   }

   private static class LazyHolder {
      private static final byte[] DECODED = decodeTable();

      private static byte[] decodeTable() {
         byte[] dest = new byte[123];

         for(int i = 0; i <= 122; ++i) {
            if (i >= 65 && i <= 90) {
               dest[i] = (byte)(i - 65);
            } else if (i >= 48 && i <= 57) {
               dest[i] = (byte)(i - -4);
            } else if (i == 43) {
               dest[i] = (byte)(i - -19);
            } else if (i == 47) {
               dest[i] = (byte)(i - -16);
            } else if (i >= 97 && i <= 122) {
               dest[i] = (byte)(i - 71);
            } else {
               dest[i] = -1;
            }
         }

         return dest;
      }
   }
}
