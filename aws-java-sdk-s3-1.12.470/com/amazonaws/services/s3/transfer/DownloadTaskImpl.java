package com.amazonaws.services.s3.transfer;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.internal.SkipMd5CheckStrategy;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.internal.DownloadImpl;

final class DownloadTaskImpl implements ServiceUtils.RetryableS3DownloadTask {
   private final AmazonS3 s3;
   private final DownloadImpl download;
   private final GetObjectRequest getObjectRequest;
   private final SkipMd5CheckStrategy skipMd5CheckStrategy = SkipMd5CheckStrategy.INSTANCE;

   DownloadTaskImpl(AmazonS3 s3, DownloadImpl download, GetObjectRequest getObjectRequest) {
      this.s3 = s3;
      this.download = download;
      this.getObjectRequest = getObjectRequest;
   }

   @Override
   public S3Object getS3ObjectStream() {
      S3Object s3Object = this.s3.getObject(this.getObjectRequest);
      this.download.setS3Object(s3Object);
      return s3Object;
   }

   @Override
   public boolean needIntegrityCheck() {
      return !(this.s3 instanceof AmazonS3Encryption)
         && !(this.s3 instanceof AmazonS3EncryptionV2)
         && !this.skipMd5CheckStrategy.skipClientSideValidationPerRequest(this.getObjectRequest);
   }
}
