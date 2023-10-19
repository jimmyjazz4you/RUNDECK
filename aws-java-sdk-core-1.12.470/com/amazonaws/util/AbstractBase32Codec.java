package com.amazonaws.util;

abstract class AbstractBase32Codec implements Codec {
   private static final int MASK_2BITS = 3;
   private static final int MASK_3BITS = 7;
   private static final int MASK_4BITS = 15;
   private static final int MASK_5BITS = 31;
   private static final byte PAD = 61;
   private final byte[] alphabets;

   protected AbstractBase32Codec(byte[] alphabets) {
      this.alphabets = alphabets;
   }

   @Override
   public final byte[] encode(byte[] src) {
      int num5bytes = src.length / 5;
      int remainder = src.length % 5;
      if (remainder == 0) {
         byte[] dest = new byte[num5bytes * 8];
         int s = 0;

         for(int d = 0; s < src.length; d += 8) {
            this.encode5bytes(src, s, dest, d);
            s += 5;
         }

         return dest;
      } else {
         byte[] dest = new byte[(num5bytes + 1) * 8];
         int s = 0;

         int d;
         for(d = 0; s < src.length - remainder; d += 8) {
            this.encode5bytes(src, s, dest, d);
            s += 5;
         }

         switch(remainder) {
            case 1:
               this.encode1byte(src, s, dest, d);
               break;
            case 2:
               this.encode2bytes(src, s, dest, d);
               break;
            case 3:
               this.encode3bytes(src, s, dest, d);
               break;
            case 4:
               this.encode4bytes(src, s, dest, d);
               break;
            default:
               throw new IllegalStateException();
         }

         return dest;
      }
   }

   private final void encode5bytes(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s++]) >>> 3 & 31];
      byte var17;
      dest[d++] = this.alphabets[(p & 7) << 2 | (var17 = src[s++]) >>> 6 & 3];
      dest[d++] = this.alphabets[var17 >>> 1 & 31];
      dest[d++] = this.alphabets[(var17 & 1) << 4 | (p = src[s++]) >>> 4 & 15];
      byte var19;
      dest[d++] = this.alphabets[(p & 15) << 1 | (var19 = src[s++]) >>> 7 & 1];
      dest[d++] = this.alphabets[var19 >>> 2 & 31];
      dest[d++] = this.alphabets[(var19 & 3) << 3 | (p = src[s]) >>> 5 & 7];
      dest[d] = this.alphabets[p & 31];
   }

   private final void encode4bytes(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s++]) >>> 3 & 31];
      byte var16;
      dest[d++] = this.alphabets[(p & 7) << 2 | (var16 = src[s++]) >>> 6 & 3];
      dest[d++] = this.alphabets[var16 >>> 1 & 31];
      dest[d++] = this.alphabets[(var16 & 1) << 4 | (p = src[s++]) >>> 4 & 15];
      byte var18;
      dest[d++] = this.alphabets[(p & 15) << 1 | (var18 = src[s]) >>> 7 & 1];
      dest[d++] = this.alphabets[var18 >>> 2 & 31];
      dest[d++] = this.alphabets[(var18 & 3) << 3];
      dest[d] = 61;
   }

   private final void encode3bytes(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s++]) >>> 3 & 31];
      byte var14;
      dest[d++] = this.alphabets[(p & 7) << 2 | (var14 = src[s++]) >>> 6 & 3];
      dest[d++] = this.alphabets[var14 >>> 1 & 31];
      dest[d++] = this.alphabets[(var14 & 1) << 4 | (p = src[s]) >>> 4 & 15];
      dest[d++] = this.alphabets[(p & 15) << 1];

      for(int i = 0; i < 3; ++i) {
         dest[d++] = 61;
      }
   }

   private final void encode2bytes(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s++]) >>> 3 & 31];
      byte var12;
      dest[d++] = this.alphabets[(p & 7) << 2 | (var12 = src[s]) >>> 6 & 3];
      dest[d++] = this.alphabets[var12 >>> 1 & 31];
      dest[d++] = this.alphabets[(var12 & 1) << 4];

      for(int i = 0; i < 4; ++i) {
         dest[d++] = 61;
      }
   }

   private final void encode1byte(byte[] src, int s, byte[] dest, int d) {
      byte p;
      dest[d++] = this.alphabets[(p = src[s]) >>> 3 & 31];
      dest[d++] = this.alphabets[(p & 7) << 2];

      for(int i = 0; i < 6; ++i) {
         dest[d++] = 61;
      }
   }

   private final void decode5bytes(byte[] src, int s, byte[] dest, int d) {
      int p = 0;
      dest[d++] = (byte)(this.pos(src[s++]) << 3 | (p = this.pos(src[s++])) >>> 2 & 7);
      int var18;
      dest[d++] = (byte)((p & 3) << 6 | this.pos(src[s++]) << 1 | (var18 = this.pos(src[s++])) >>> 4 & 1);
      dest[d++] = (byte)((var18 & 15) << 4 | (p = this.pos(src[s++])) >>> 1 & 15);
      int var20;
      dest[d++] = (byte)((p & 1) << 7 | this.pos(src[s++]) << 2 | (var20 = this.pos(src[s++])) >>> 3 & 3);
      dest[d] = (byte)((var20 & 7) << 5 | this.pos(src[s]));
   }

   private final void decode1to4bytes(int n, byte[] src, int s, byte[] dest, int d) {
      int p = 0;
      dest[d++] = (byte)(this.pos(src[s++]) << 3 | (p = this.pos(src[s++])) >>> 2 & 7);
      if (n == 1) {
         CodecUtils.sanityCheckLastPos(p, 3);
      } else {
         int var17;
         dest[d++] = (byte)((p & 3) << 6 | this.pos(src[s++]) << 1 | (var17 = this.pos(src[s++])) >>> 4 & 1);
         if (n == 2) {
            CodecUtils.sanityCheckLastPos(var17, 15);
         } else {
            dest[d++] = (byte)((var17 & 15) << 4 | (p = this.pos(src[s++])) >>> 1 & 15);
            if (n == 3) {
               CodecUtils.sanityCheckLastPos(p, 1);
            } else {
               int var19;
               dest[d] = (byte)((p & 1) << 7 | this.pos(src[s++]) << 2 | (var19 = this.pos(src[s])) >>> 3 & 3);
               CodecUtils.sanityCheckLastPos(var19, 7);
            }
         }
      }
   }

   @Override
   public final byte[] decode(byte[] src, int length) {
      if (length % 8 != 0) {
         throw new IllegalArgumentException("Input is expected to be encoded in multiple of 8 bytes but found: " + length);
      } else {
         int pads = 0;

         for(int last = length - 1; pads < 6 && last > -1 && src[last] == 61; ++pads) {
            --last;
         }

         int fq;
         switch(pads) {
            case 0:
               fq = 5;
               break;
            case 1:
               fq = 4;
               break;
            case 2:
            case 5:
            default:
               throw new IllegalArgumentException("Invalid number of paddings " + pads);
            case 3:
               fq = 3;
               break;
            case 4:
               fq = 2;
               break;
            case 6:
               fq = 1;
         }

         byte[] dest = new byte[length / 8 * 5 - (5 - fq)];
         int s = 0;

         int d;
         for(d = 0; d < dest.length - fq % 5; d += 5) {
            this.decode5bytes(src, s, dest, d);
            s += 8;
         }

         if (fq < 5) {
            this.decode1to4bytes(fq, src, s, dest, d);
         }

         return dest;
      }
   }

   protected abstract int pos(byte var1);
}
