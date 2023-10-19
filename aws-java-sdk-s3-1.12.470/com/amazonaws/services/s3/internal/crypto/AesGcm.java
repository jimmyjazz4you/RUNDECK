package com.amazonaws.services.s3.internal.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

class AesGcm extends ContentCryptoScheme {
   @Override
   public String getKeyGeneratorAlgorithm() {
      return "AES";
   }

   @Override
   public String getCipherAlgorithm() {
      return "AES/GCM/NoPadding";
   }

   @Override
   public int getKeyLengthInBits() {
      return 256;
   }

   @Override
   public int getBlockSizeInBytes() {
      return 16;
   }

   @Override
   public int getIVLengthInBytes() {
      return 12;
   }

   @Override
   long getMaxPlaintextSize() {
      return 68719476704L;
   }

   @Override
   public int getTagLengthInBits() {
      return 128;
   }

   @Override
   public String getPreferredCipherProvider() {
      return "BC";
   }

   @Override
   CipherLite createAuxillaryCipher(SecretKey cek, byte[] ivOrig, int cipherMode, Provider securityProvider, long startingBytePos) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
      byte[] iv = AES_CTR.adjustIV(ivOrig, startingBytePos);
      return AES_CTR.createCipherLite(cek, iv, cipherMode, securityProvider, false);
   }

   @Override
   protected CipherLite newCipherLite(Cipher cipher, SecretKey cek, int cipherMode) {
      return new GCMCipherLite(cipher, cek, cipherMode);
   }
}
