package com.amazonaws.services.s3.transfer.model;

public class CopyResult {
   private String sourceBucketName;
   private String sourceKey;
   private String destinationBucketName;
   private String destinationKey;
   private String eTag;
   private String versionId;

   public String getSourceBucketName() {
      return this.sourceBucketName;
   }

   public void setSourceBucketName(String sourceBucketName) {
      this.sourceBucketName = sourceBucketName;
   }

   public String getSourceKey() {
      return this.sourceKey;
   }

   public void setSourceKey(String sourceKey) {
      this.sourceKey = sourceKey;
   }

   public String getDestinationBucketName() {
      return this.destinationBucketName;
   }

   public void setDestinationBucketName(String destinationBucketName) {
      this.destinationBucketName = destinationBucketName;
   }

   public String getDestinationKey() {
      return this.destinationKey;
   }

   public void setDestinationKey(String destinationKey) {
      this.destinationKey = destinationKey;
   }

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String etag) {
      this.eTag = etag;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }
}
