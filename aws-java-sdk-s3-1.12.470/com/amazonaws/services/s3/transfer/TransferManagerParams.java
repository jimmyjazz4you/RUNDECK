package com.amazonaws.services.s3.transfer;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.AmazonS3;
import java.util.concurrent.ExecutorService;

@SdkInternalApi
class TransferManagerParams {
   private AmazonS3 s3Client;
   private ExecutorService executorService;
   private Boolean shutDownThreadPools;
   private TransferManagerConfiguration configuration;

   public AmazonS3 getS3Client() {
      return this.s3Client;
   }

   public TransferManagerParams withS3Client(AmazonS3 s3Client) {
      this.s3Client = s3Client;
      return this;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public TransferManagerParams withExecutorService(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
   }

   public Boolean getShutDownThreadPools() {
      return this.shutDownThreadPools;
   }

   public TransferManagerParams withShutDownThreadPools(Boolean shutDownThreadPools) {
      this.shutDownThreadPools = shutDownThreadPools;
      return this;
   }

   public TransferManagerConfiguration getConfiguration() {
      return this.configuration;
   }

   public TransferManagerParams withTransferManagerConfiguration(TransferManagerConfiguration configuration) {
      this.configuration = configuration;
      return this;
   }
}
