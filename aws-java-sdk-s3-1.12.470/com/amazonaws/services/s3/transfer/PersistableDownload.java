package com.amazonaws.services.s3.transfer;

import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PersistableDownload extends PersistableTransfer {
   static final String TYPE = "download";
   @JsonProperty
   private final String pauseType = "download";
   @JsonProperty
   private final String bucketName;
   @JsonProperty
   private final String key;
   @JsonProperty
   private final String versionId;
   @JsonProperty
   private final long[] range;
   @JsonProperty
   private final ResponseHeaderOverrides responseHeaders;
   @JsonProperty
   private final boolean isRequesterPays;
   @JsonProperty
   private final String file;
   @JsonProperty
   private final Integer lastFullyDownloadedPartNumber;
   @JsonProperty
   private final long lastModifiedTime;
   @JsonProperty
   private final Long lastFullyDownloadedFilePosition;

   public PersistableDownload() {
      this(null, null, null, null, null, false, null, null, 0L, null);
   }

   public PersistableDownload(
      @JsonProperty("bucketName") String bucketName,
      @JsonProperty("key") String key,
      @JsonProperty("versionId") String versionId,
      @JsonProperty("range") long[] range,
      @JsonProperty("responseHeaders") ResponseHeaderOverrides responseHeaders,
      @JsonProperty("isRequesterPays") boolean isRequesterPays,
      @JsonProperty("file") String file,
      @JsonProperty("lastFullyDownloadedPartNumber") Integer lastFullyDownloadedPartNumber,
      @JsonProperty("lastModifiedTime") long lastModifiedTime,
      @JsonProperty("lastFullyDownloadedFilePosition") Long lastFullyDownloadedFilePosition
   ) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = versionId;
      this.range = range == null ? null : (long[])range.clone();
      this.responseHeaders = responseHeaders;
      this.isRequesterPays = isRequesterPays;
      this.file = file;
      this.lastFullyDownloadedPartNumber = lastFullyDownloadedPartNumber;
      this.lastModifiedTime = lastModifiedTime;
      this.lastFullyDownloadedFilePosition = lastFullyDownloadedFilePosition;
   }

   public PersistableDownload(
      String bucketName,
      String key,
      String versionId,
      long[] range,
      ResponseHeaderOverrides responseHeaders,
      boolean requesterPays,
      String absolutePath,
      Integer lastFullyDownloadedPartNumber,
      long time
   ) {
      this(bucketName, key, versionId, range, responseHeaders, requesterPays, absolutePath, lastFullyDownloadedPartNumber, time, 0L);
   }

   String getBucketName() {
      return this.bucketName;
   }

   String getKey() {
      return this.key;
   }

   String getVersionId() {
      return this.versionId;
   }

   long[] getRange() {
      return this.range == null ? null : (long[])this.range.clone();
   }

   ResponseHeaderOverrides getResponseHeaders() {
      return this.responseHeaders;
   }

   boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   String getFile() {
      return this.file;
   }

   String getPauseType() {
      return "download";
   }

   Integer getLastFullyDownloadedPartNumber() {
      return this.lastFullyDownloadedPartNumber;
   }

   Long getlastModifiedTime() {
      return this.lastModifiedTime;
   }

   Long getLastFullyDownloadedFilePosition() {
      return this.lastFullyDownloadedFilePosition;
   }
}
