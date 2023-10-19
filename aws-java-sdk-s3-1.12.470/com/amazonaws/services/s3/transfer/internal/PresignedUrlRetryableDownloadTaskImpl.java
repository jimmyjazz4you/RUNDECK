package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.internal.SkipMd5CheckStrategy;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.S3Object;

@SdkInternalApi
public class PresignedUrlRetryableDownloadTaskImpl implements ServiceUtils.RetryableS3DownloadTask {
   private final AmazonS3 s3;
   private final PresignedUrlDownloadImpl download;
   private final PresignedUrlDownloadRequest request;
   private final SkipMd5CheckStrategy skipMd5CheckStrategy = SkipMd5CheckStrategy.INSTANCE;

   public PresignedUrlRetryableDownloadTaskImpl(AmazonS3 s3, PresignedUrlDownloadImpl download, PresignedUrlDownloadRequest request) {
      this.s3 = s3;
      this.download = download;
      this.request = request;
   }

   @Override
   public S3Object getS3ObjectStream() {
      S3Object s3Object = this.s3.download(this.request).getS3Object();
      this.download.setS3Object(s3Object);
      return s3Object;
   }

   @Override
   public boolean needIntegrityCheck() {
      return !(this.s3 instanceof AmazonS3Encryption)
         && !(this.s3 instanceof AmazonS3EncryptionV2)
         && !this.skipMd5CheckStrategy.skipClientSideValidationPerRequest(this.request);
   }
}
