package com.amazonaws.services.s3.internal.crypto.v1;

final class MultipartUploadCbcContext extends MultipartUploadCryptoContext {
   private byte[] nextIV;

   MultipartUploadCbcContext(String bucketName, String key, ContentCryptoMaterial cekMaterial) {
      super(bucketName, key, cekMaterial);
   }

   public void setNextInitializationVector(byte[] nextIV) {
      this.nextIV = nextIV;
   }

   public byte[] getNextInitializationVector() {
      return this.nextIV;
   }
}
