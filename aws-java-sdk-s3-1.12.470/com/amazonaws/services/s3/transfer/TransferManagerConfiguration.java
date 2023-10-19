package com.amazonaws.services.s3.transfer;

import com.amazonaws.annotation.SdkTestInternalApi;

public class TransferManagerConfiguration {
   @SdkTestInternalApi
   static final int DEFAULT_MINIMUM_UPLOAD_PART_SIZE = 5242880;
   @SdkTestInternalApi
   static final long DEFAULT_MULTIPART_UPLOAD_THRESHOLD = 16777216L;
   @SdkTestInternalApi
   static final long DEFAULT_MULTIPART_COPY_THRESHOLD = 5368709120L;
   @SdkTestInternalApi
   static final long DEFAULT_MINIMUM_COPY_PART_SIZE = 104857600L;
   private long minimumUploadPartSize = 5242880L;
   private long multipartUploadThreshold = 16777216L;
   private long multipartCopyThreshold = 5368709120L;
   private long multipartCopyPartSize = 104857600L;
   private boolean disableParallelDownloads = false;
   private boolean alwaysCalculateMultipartMd5 = false;

   public long getMinimumUploadPartSize() {
      return this.minimumUploadPartSize;
   }

   public void setMinimumUploadPartSize(long minimumUploadPartSize) {
      this.minimumUploadPartSize = minimumUploadPartSize;
   }

   public long getMultipartUploadThreshold() {
      return this.multipartUploadThreshold;
   }

   public void setMultipartUploadThreshold(long multipartUploadThreshold) {
      this.multipartUploadThreshold = multipartUploadThreshold;
   }

   public long getMultipartCopyPartSize() {
      return this.multipartCopyPartSize;
   }

   public void setMultipartCopyPartSize(long multipartCopyPartSize) {
      this.multipartCopyPartSize = multipartCopyPartSize;
   }

   public long getMultipartCopyThreshold() {
      return this.multipartCopyThreshold;
   }

   public void setMultipartCopyThreshold(long multipartCopyThreshold) {
      this.multipartCopyThreshold = multipartCopyThreshold;
   }

   @Deprecated
   public void setMultipartUploadThreshold(int multipartUploadThreshold) {
      this.setMultipartUploadThreshold((long)multipartUploadThreshold);
   }

   public boolean isDisableParallelDownloads() {
      return this.disableParallelDownloads;
   }

   public void setDisableParallelDownloads(boolean disableParallelDownloads) {
      this.disableParallelDownloads = disableParallelDownloads;
   }

   public boolean isAlwaysCalculateMultipartMd5() {
      return this.alwaysCalculateMultipartMd5;
   }

   public void setAlwaysCalculateMultipartMd5(boolean alwaysCalculateMultipartMd5) {
      this.alwaysCalculateMultipartMd5 = alwaysCalculateMultipartMd5;
   }
}
