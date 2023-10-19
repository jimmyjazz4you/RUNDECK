package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.ServerSideEncryptionResult;

abstract class AbstractSSEHandler extends AbstractHandler implements ServerSideEncryptionResult {
   protected abstract ServerSideEncryptionResult sseResult();

   @Override
   public final String getSSEAlgorithm() {
      ServerSideEncryptionResult result = this.sseResult();
      return result == null ? null : result.getSSEAlgorithm();
   }

   @Override
   public final void setSSEAlgorithm(String serverSideEncryption) {
      ServerSideEncryptionResult result = this.sseResult();
      if (result != null) {
         result.setSSEAlgorithm(serverSideEncryption);
      }
   }

   @Override
   public final String getSSECustomerAlgorithm() {
      ServerSideEncryptionResult result = this.sseResult();
      return result == null ? null : result.getSSECustomerAlgorithm();
   }

   @Override
   public final void setSSECustomerAlgorithm(String algorithm) {
      ServerSideEncryptionResult result = this.sseResult();
      if (result != null) {
         result.setSSECustomerAlgorithm(algorithm);
      }
   }

   @Override
   public final String getSSECustomerKeyMd5() {
      ServerSideEncryptionResult result = this.sseResult();
      return result == null ? null : result.getSSECustomerKeyMd5();
   }

   @Override
   public final void setSSECustomerKeyMd5(String md5Digest) {
      ServerSideEncryptionResult result = this.sseResult();
      if (result != null) {
         result.setSSECustomerKeyMd5(md5Digest);
      }
   }

   @Override
   public final Boolean getBucketKeyEnabled() {
      ServerSideEncryptionResult result = this.sseResult();
      return result == null ? false : result.getBucketKeyEnabled();
   }

   @Override
   public final void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
      ServerSideEncryptionResult result = this.sseResult();
      if (result != null) {
         result.setBucketKeyEnabled(bucketKeyEnabled);
      }
   }
}
