package com.amazonaws.services.s3.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class PersistableUpload extends PersistableTransfer {
   static final String TYPE = "upload";
   @JsonProperty
   private final String pauseType = "upload";
   @JsonProperty
   private final String bucketName;
   @JsonProperty
   private final String key;
   @JsonProperty
   private final String file;
   @JsonProperty
   private final String multipartUploadId;
   @JsonProperty
   private final long partSize;
   @JsonProperty
   private final long mutlipartUploadThreshold;

   public PersistableUpload() {
      this(null, null, null, null, -1L, -1L);
   }

   public PersistableUpload(
      @JsonProperty("bucketName") String bucketName,
      @JsonProperty("key") String key,
      @JsonProperty("file") String file,
      @JsonProperty("multipartUploadId") String multipartUploadId,
      @JsonProperty("partSize") long partSize,
      @JsonProperty("mutlipartUploadThreshold") long mutlipartUploadThreshold
   ) {
      this.bucketName = bucketName;
      this.key = key;
      this.file = file;
      this.multipartUploadId = multipartUploadId;
      this.partSize = partSize;
      this.mutlipartUploadThreshold = mutlipartUploadThreshold;
   }

   String getBucketName() {
      return this.bucketName;
   }

   String getKey() {
      return this.key;
   }

   String getMultipartUploadId() {
      return this.multipartUploadId;
   }

   long getPartSize() {
      return this.partSize;
   }

   long getMutlipartUploadThreshold() {
      return this.mutlipartUploadThreshold;
   }

   String getFile() {
      return this.file;
   }

   String getPauseType() {
      return "upload";
   }
}
