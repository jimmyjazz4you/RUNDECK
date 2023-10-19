package com.amazonaws.services.s3.internal;

public abstract class SSEResultBase implements ServerSideEncryptionResult {
   private String sseAlgorithm;
   private String sseCustomerAlgorithm;
   private String sseCustomerKeyMD5;
   private Boolean bucketKeyEnabled;

   @Override
   public final String getSSEAlgorithm() {
      return this.sseAlgorithm;
   }

   @Override
   public final void setSSEAlgorithm(String algorithm) {
      this.sseAlgorithm = algorithm;
   }

   @Override
   public final String getSSECustomerAlgorithm() {
      return this.sseCustomerAlgorithm;
   }

   @Override
   public final void setSSECustomerAlgorithm(String algorithm) {
      this.sseCustomerAlgorithm = algorithm;
   }

   @Override
   public final String getSSECustomerKeyMd5() {
      return this.sseCustomerKeyMD5;
   }

   @Override
   public final void setSSECustomerKeyMd5(String md5) {
      this.sseCustomerKeyMD5 = md5;
   }

   @Override
   public final Boolean getBucketKeyEnabled() {
      return this.bucketKeyEnabled;
   }

   @Override
   public final void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.bucketKeyEnabled = bucketKeyEnabled;
   }

   @Deprecated
   public final String getServerSideEncryption() {
      return this.sseAlgorithm;
   }
}
