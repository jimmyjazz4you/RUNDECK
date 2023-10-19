package com.amazonaws.services.s3.internal.crypto;

class AesCbc extends ContentCryptoScheme {
   @Override
   public String getKeyGeneratorAlgorithm() {
      return "AES";
   }

   @Override
   public String getCipherAlgorithm() {
      return "AES/CBC/PKCS5Padding";
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
      return 16;
   }

   @Override
   long getMaxPlaintextSize() {
      return 4503599627370496L;
   }
}
