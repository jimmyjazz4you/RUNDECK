package com.amazonaws.services.s3.internal.crypto;

import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

public final class GCMCipherLite extends CipherLite {
   private static final int TAG_LENGTH = ContentCryptoScheme.AES_GCM.getTagLengthInBits() / 8;
   private final int tagLen;
   private long outputByteCount;
   private boolean invisiblyProcessed;
   private long currentCount;
   private long markedCount;
   private CipherLite aux;
   private byte[] finalBytes;
   private boolean doneFinal;
   private boolean securityViolated;

   GCMCipherLite(Cipher cipher, SecretKey secreteKey, int cipherMode) {
      super(cipher, ContentCryptoScheme.AES_GCM, secreteKey, cipherMode);
      this.tagLen = cipherMode == 1 ? TAG_LENGTH : 0;
      if (cipherMode != 1 && cipherMode != 2) {
         throw new IllegalArgumentException();
      }
   }

   @Override
   public byte[] doFinal() throws IllegalBlockSizeException, BadPaddingException {
      if (this.doneFinal) {
         if (this.securityViolated) {
            throw new SecurityException();
         } else {
            return this.finalBytes == null ? null : (byte[])this.finalBytes.clone();
         }
      } else {
         this.doneFinal = true;
         this.finalBytes = super.doFinal();
         if (this.finalBytes == null) {
            return null;
         } else {
            this.outputByteCount += (long)this.checkMax(this.finalBytes.length - this.tagLen);
            return (byte[])this.finalBytes.clone();
         }
      }
   }

   @Override
   public final byte[] doFinal(byte[] input) throws IllegalBlockSizeException, BadPaddingException {
      return this.doFinal0(input, 0, input.length);
   }

   @Override
   public final byte[] doFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
      return this.doFinal0(input, inputOffset, inputLen);
   }

   private final byte[] doFinal0(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
      if (this.doneFinal) {
         if (this.securityViolated) {
            throw new SecurityException();
         } else if (2 == this.getCipherMode()) {
            return this.finalBytes == null ? null : (byte[])this.finalBytes.clone();
         } else {
            int finalDataLen = this.finalBytes.length - this.tagLen;
            if (inputLen == finalDataLen) {
               return (byte[])this.finalBytes.clone();
            } else if (inputLen < finalDataLen && (long)inputLen + this.currentCount == this.outputByteCount) {
               int from = this.finalBytes.length - this.tagLen - inputLen;
               return Arrays.copyOfRange(this.finalBytes, from, this.finalBytes.length);
            } else {
               throw new IllegalStateException("Inconsistent re-rencryption");
            }
         }
      } else {
         this.doneFinal = true;
         this.finalBytes = super.doFinal(input, inputOffset, inputLen);
         if (this.finalBytes == null) {
            return null;
         } else {
            this.outputByteCount += (long)this.checkMax(this.finalBytes.length - this.tagLen);
            return (byte[])this.finalBytes.clone();
         }
      }
   }

   @Override
   public byte[] update(byte[] input, int inputOffset, int inputLen) {
      byte[] out;
      if (this.aux == null) {
         out = super.update(input, inputOffset, inputLen);
         if (out == null) {
            this.invisiblyProcessed = input.length > 0;
            return null;
         }

         this.outputByteCount += (long)this.checkMax(out.length);
         this.invisiblyProcessed = out.length == 0 && inputLen > 0;
      } else {
         out = this.aux.update(input, inputOffset, inputLen);
         if (out == null) {
            return null;
         }

         this.currentCount += (long)out.length;
         if (this.currentCount == this.outputByteCount) {
            this.aux = null;
         } else if (this.currentCount > this.outputByteCount) {
            if (1 == this.getCipherMode()) {
               throw new IllegalStateException("currentCount=" + this.currentCount + " > outputByteCount=" + this.outputByteCount);
            }

            int finalBytesLen = this.finalBytes == null ? 0 : this.finalBytes.length;
            long diff = this.outputByteCount - (this.currentCount - (long)out.length) - (long)finalBytesLen;
            this.currentCount = this.outputByteCount - (long)finalBytesLen;
            this.aux = null;
            return Arrays.copyOf(out, (int)diff);
         }
      }

      return out;
   }

   private int checkMax(int delta) {
      if (this.outputByteCount + (long)delta > 68719476704L) {
         this.securityViolated = true;
         throw new SecurityException(
            "Number of bytes processed has exceeded the maximum allowed by AES/GCM; [outputByteCount=" + this.outputByteCount + ", delta=" + delta + "]"
         );
      } else {
         return delta;
      }
   }

   @Override
   public long mark() {
      return this.markedCount = this.aux == null ? this.outputByteCount : this.currentCount;
   }

   @Override
   public boolean markSupported() {
      return true;
   }

   @Override
   public void reset() {
      if (this.markedCount < this.outputByteCount || this.invisiblyProcessed) {
         try {
            this.aux = this.createAuxiliary(this.markedCount);
            this.currentCount = this.markedCount;
         } catch (Exception var2) {
            throw var2 instanceof RuntimeException ? (RuntimeException)var2 : new IllegalStateException(var2);
         }
      }
   }

   public byte[] getFinalBytes() {
      return this.finalBytes == null ? null : (byte[])this.finalBytes.clone();
   }

   public byte[] getTag() {
      return this.getCipherMode() == 1 && this.finalBytes != null
         ? Arrays.copyOfRange(this.finalBytes, this.finalBytes.length - this.tagLen, this.finalBytes.length)
         : null;
   }

   public long getOutputByteCount() {
      return this.outputByteCount;
   }

   public long getCurrentCount() {
      return this.currentCount;
   }

   public long getMarkedCount() {
      return this.markedCount;
   }
}
