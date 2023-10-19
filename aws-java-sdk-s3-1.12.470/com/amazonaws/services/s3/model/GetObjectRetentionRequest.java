package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetObjectRetentionRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucket;
   private String key;
   private String versionId;
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetObjectRetentionRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.bucket;
   }

   public GetObjectRetentionRequest withBucketName(String bucket) {
      this.bucket = bucket;
      return this;
   }

   public void setBucketName(String bucket) {
      this.withBucketName(bucket);
   }

   public String getKey() {
      return this.key;
   }

   public GetObjectRetentionRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public void setKey(String key) {
      this.withKey(key);
   }

   public String getVersionId() {
      return this.versionId;
   }

   public GetObjectRetentionRequest withVersionId(String versionId) {
      this.versionId = versionId;
      return this;
   }

   public void setVersionId(String versionId) {
      this.withVersionId(versionId);
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public GetObjectRetentionRequest withRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
      return this;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }
}
