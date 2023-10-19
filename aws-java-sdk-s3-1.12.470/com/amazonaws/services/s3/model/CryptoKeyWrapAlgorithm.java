package com.amazonaws.services.s3.model;

public enum CryptoKeyWrapAlgorithm {
   AES_GCM_NoPadding("AES/GCM"),
   RSA_OAEP_SHA1("RSA-OAEP-SHA1"),
   KMS("kms+context");

   private final String metadataLabel;

   private CryptoKeyWrapAlgorithm(String metadataLabel) {
      this.metadataLabel = metadataLabel;
   }

   public String algorithmName() {
      return this.metadataLabel;
   }
}
