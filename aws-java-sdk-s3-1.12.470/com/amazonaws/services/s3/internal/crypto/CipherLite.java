package com.amazonaws.services.s3.internal.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.NullCipher;
import javax.crypto.SecretKey;

public class CipherLite {
   public static final CipherLite Null = new CipherLite() {
      @Override
      public CipherLite createAuxiliary(long startingBytePos) {
         return this;
      }

      @Override
      public CipherLite createInverse() {
         return this;
      }
   };
   private final Cipher cipher;
   private final ContentCryptoScheme scheme;
   private final SecretKey secreteKey;
   private final int cipherMode;

   private CipherLite() {
      this.cipher = new NullCipher();
      this.scheme = null;
      this.secreteKey = null;
      this.cipherMode = -1;
   }

   CipherLite(Cipher cipher, ContentCryptoScheme scheme, SecretKey secreteKey, int cipherMode) {
      this.cipher = cipher;
      this.scheme = scheme;
      this.secreteKey = secreteKey;
      this.cipherMode = cipherMode;
   }

   public CipherLite recreate() {
      return this.scheme.createCipherLite(this.secreteKey, this.cipher.getIV(), this.cipherMode, this.cipher.getProvider(), true);
   }

   public CipherLite createUsingIV(byte[] iv) {
      return this.scheme.createCipherLite(this.secreteKey, iv, this.cipherMode, this.cipher.getProvider(), true);
   }

   public CipherLite createAuxiliary(long startingBytePos) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
      return this.scheme.createAuxillaryCipher(this.secreteKey, this.cipher.getIV(), this.cipherMode, this.cipher.getProvider(), startingBytePos);
   }

   public CipherLite createInverse() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
      int inversedMode;
      if (this.cipherMode == 2) {
         inversedMode = 1;
      } else {
         if (this.cipherMode != 1) {
            throw new UnsupportedOperationException();
         }

         inversedMode = 2;
      }

      return this.scheme.createCipherLite(this.secreteKey, this.cipher.getIV(), inversedMode, this.cipher.getProvider(), true);
   }

   public byte[] doFinal() throws IllegalBlockSizeException, BadPaddingException {
      return this.cipher.doFinal();
   }

   public byte[] doFinal(byte[] input) throws IllegalBlockSizeException, BadPaddingException {
      return this.cipher.doFinal(input);
   }

   public byte[] doFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
      return this.cipher.doFinal(input, inputOffset, inputLen);
   }

   public byte[] update(byte[] input, int inputOffset, int inputLen) {
      return this.cipher.update(input, inputOffset, inputLen);
   }

   public final String getCipherAlgorithm() {
      return this.cipher.getAlgorithm();
   }

   public final Provider getCipherProvider() {
      return this.cipher.getProvider();
   }

   public final String getSecretKeyAlgorithm() {
      return this.secreteKey.getAlgorithm();
   }

   public final Cipher getCipher() {
      return this.cipher;
   }

   public final ContentCryptoScheme getContentCryptoScheme() {
      return this.scheme;
   }

   public final byte[] getIV() {
      return this.cipher.getIV();
   }

   public final int getBlockSize() {
      return this.cipher.getBlockSize();
   }

   final int getCipherMode() {
      return this.cipherMode;
   }

   public boolean markSupported() {
      return false;
   }

   public long mark() {
      return -1L;
   }

   public void reset() {
      throw new IllegalStateException("mark/reset not supported");
   }

   int getOutputSize(int inputLen) {
      return this.cipher.getOutputSize(inputLen);
   }
}
