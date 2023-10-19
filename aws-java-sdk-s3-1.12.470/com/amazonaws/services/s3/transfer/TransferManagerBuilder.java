package com.amazonaws.services.s3.transfer;

import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.client.builder.ExecutorFactory;
import com.amazonaws.internal.SdkFunction;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.internal.TransferManagerUtils;
import java.util.concurrent.ExecutorService;

@NotThreadSafe
public final class TransferManagerBuilder {
   private static final SdkFunction<TransferManagerParams, TransferManager> DEFAULT_TRANSFER_MANAGER_FACTORY = new SdkFunction<TransferManagerParams, TransferManager>(
      
   ) {
      public TransferManager apply(TransferManagerParams params) {
         return new TransferManager(params);
      }
   };
   private final SdkFunction<TransferManagerParams, TransferManager> transferManagerFactory;
   private AmazonS3 s3Client;
   private ExecutorFactory executorFactory;
   private Boolean shutDownThreadPools;
   private Long minimumUploadPartSize;
   private Long multipartUploadThreshold;
   private Long multipartCopyThreshold;
   private Long multipartCopyPartSize;
   private Boolean disableParallelDownloads;
   private Boolean alwaysCalculateMultipartMd5;

   public static TransferManagerBuilder standard() {
      return new TransferManagerBuilder();
   }

   public static TransferManager defaultTransferManager() {
      return standard().build();
   }

   private TransferManagerBuilder() {
      this(DEFAULT_TRANSFER_MANAGER_FACTORY);
   }

   @SdkTestInternalApi
   TransferManagerBuilder(SdkFunction<TransferManagerParams, TransferManager> transferManagerFactory) {
      this.transferManagerFactory = transferManagerFactory;
   }

   public final AmazonS3 getS3Client() {
      return this.s3Client;
   }

   public final void setS3Client(AmazonS3 s3Client) {
      this.s3Client = s3Client;
   }

   public final TransferManagerBuilder withS3Client(AmazonS3 s3Client) {
      this.setS3Client(s3Client);
      return this;
   }

   private AmazonS3 resolveS3Client() {
      return this.s3Client == null ? AmazonS3ClientBuilder.defaultClient() : this.s3Client;
   }

   public final ExecutorFactory getExecutorFactory() {
      return this.executorFactory;
   }

   public final void setExecutorFactory(ExecutorFactory executorFactory) {
      this.executorFactory = executorFactory;
   }

   public final TransferManagerBuilder withExecutorFactory(ExecutorFactory executorFactory) {
      this.setExecutorFactory(executorFactory);
      return this;
   }

   private ExecutorService resolveExecutorService() {
      return (ExecutorService)(this.executorFactory == null ? TransferManagerUtils.createDefaultExecutorService() : this.executorFactory.newExecutor());
   }

   public final Boolean isShutDownThreadPools() {
      return this.shutDownThreadPools;
   }

   public final void setShutDownThreadPools(Boolean shutDownThreadPools) {
      this.shutDownThreadPools = shutDownThreadPools;
   }

   public final TransferManagerBuilder withShutDownThreadPools(Boolean shutDownThreadPools) {
      this.setShutDownThreadPools(shutDownThreadPools);
      return this;
   }

   private Boolean resolveShutDownThreadPools() {
      return this.shutDownThreadPools == null ? Boolean.TRUE : this.shutDownThreadPools;
   }

   public final Long getMinimumUploadPartSize() {
      return this.minimumUploadPartSize;
   }

   public final void setMinimumUploadPartSize(Long minimumUploadPartSize) {
      this.minimumUploadPartSize = minimumUploadPartSize;
   }

   public final TransferManagerBuilder withMinimumUploadPartSize(Long minimumUploadPartSize) {
      this.setMinimumUploadPartSize(minimumUploadPartSize);
      return this;
   }

   public final Long getMultipartUploadThreshold() {
      return this.multipartUploadThreshold;
   }

   public final void setMultipartUploadThreshold(Long multipartUploadThreshold) {
      this.multipartUploadThreshold = multipartUploadThreshold;
   }

   public final TransferManagerBuilder withMultipartUploadThreshold(Long multipartUploadThreshold) {
      this.setMultipartUploadThreshold(multipartUploadThreshold);
      return this;
   }

   public final Long getMultipartCopyThreshold() {
      return this.multipartCopyThreshold;
   }

   public final void setMultipartCopyThreshold(Long multipartCopyThreshold) {
      this.multipartCopyThreshold = multipartCopyThreshold;
   }

   public final TransferManagerBuilder withMultipartCopyThreshold(Long multipartCopyThreshold) {
      this.setMultipartCopyThreshold(multipartCopyThreshold);
      return this;
   }

   public final Long getMultipartCopyPartSize() {
      return this.multipartCopyPartSize;
   }

   public final void setMultipartCopyPartSize(Long multipartCopyPartSize) {
      this.multipartCopyPartSize = multipartCopyPartSize;
   }

   public final TransferManagerBuilder withMultipartCopyPartSize(Long multipartCopyPartSize) {
      this.setMultipartCopyPartSize(multipartCopyPartSize);
      return this;
   }

   public Boolean isDisableParallelDownloads() {
      return this.disableParallelDownloads;
   }

   public void setDisableParallelDownloads(Boolean disableParallelDownloads) {
      this.disableParallelDownloads = disableParallelDownloads;
   }

   public TransferManagerBuilder withDisableParallelDownloads(Boolean disableParallelDownloads) {
      this.setDisableParallelDownloads(disableParallelDownloads);
      return this;
   }

   public TransferManagerBuilder disableParallelDownloads() {
      return this.withDisableParallelDownloads(Boolean.TRUE);
   }

   public boolean getAlwaysCalculateMultipartMd5() {
      return this.alwaysCalculateMultipartMd5;
   }

   public void setAlwaysCalculateMultipartMd5(boolean alwaysCalculateMultipartMd5) {
      this.alwaysCalculateMultipartMd5 = alwaysCalculateMultipartMd5;
   }

   public TransferManagerBuilder withAlwaysCalculateMultipartMd5(boolean alwaysCalculateMultipartMd5) {
      this.setAlwaysCalculateMultipartMd5(alwaysCalculateMultipartMd5);
      return this;
   }

   private TransferManagerConfiguration resolveConfiguration() {
      TransferManagerConfiguration configuration = new TransferManagerConfiguration();
      if (this.minimumUploadPartSize != null) {
         configuration.setMinimumUploadPartSize(this.minimumUploadPartSize);
      }

      if (this.multipartCopyPartSize != null) {
         configuration.setMultipartCopyPartSize(this.multipartCopyPartSize);
      }

      if (this.multipartCopyThreshold != null) {
         configuration.setMultipartCopyThreshold(this.multipartCopyThreshold);
      }

      if (this.multipartUploadThreshold != null) {
         configuration.setMultipartUploadThreshold(this.multipartUploadThreshold);
      }

      if (this.disableParallelDownloads != null) {
         configuration.setDisableParallelDownloads(this.disableParallelDownloads);
      }

      if (this.alwaysCalculateMultipartMd5 != null) {
         configuration.setAlwaysCalculateMultipartMd5(this.alwaysCalculateMultipartMd5);
      }

      return configuration;
   }

   TransferManagerParams getParams() {
      return new TransferManagerParams()
         .withS3Client(this.resolveS3Client())
         .withExecutorService(this.resolveExecutorService())
         .withShutDownThreadPools(this.resolveShutDownThreadPools())
         .withTransferManagerConfiguration(this.resolveConfiguration());
   }

   public final TransferManager build() {
      return (TransferManager)this.transferManagerFactory.apply(this.getParams());
   }
}
