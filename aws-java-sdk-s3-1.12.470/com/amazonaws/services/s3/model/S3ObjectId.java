package com.amazonaws.services.s3.model;

import com.amazonaws.annotation.Immutable;
import java.io.Serializable;

@Immutable
public class S3ObjectId implements Serializable {
   private final String bucket;
   private final String key;
   private final String versionId;

   public S3ObjectId(String bucket, String key) {
      this(bucket, key, null);
   }

   public S3ObjectId(String bucket, String key, String versionId) {
      if (bucket != null && key != null) {
         this.bucket = bucket;
         this.key = key;
         this.versionId = versionId;
      } else {
         throw new IllegalArgumentException("bucket and key must be specified");
      }
   }

   public S3ObjectId(S3ObjectIdBuilder builder) {
      this.bucket = builder.getBucket();
      this.key = builder.getKey();
      this.versionId = builder.getVersionId();
   }

   public String getBucket() {
      return this.bucket;
   }

   public String getKey() {
      return this.key;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public InstructionFileId instructionFileId() {
      return this.instructionFileId(null);
   }

   public InstructionFileId instructionFileId(String suffix) {
      String ifileKey = this.key + ".";
      ifileKey = ifileKey + (suffix != null && suffix.trim().length() != 0 ? suffix : "instruction");
      return new InstructionFileId(this.bucket, ifileKey, this.versionId);
   }

   @Override
   public String toString() {
      return "bucket: " + this.bucket + ", key: " + this.key + ", versionId: " + this.versionId;
   }
}
