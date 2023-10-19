package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class DeleteObjectTaggingRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private String versionId;
   private String expectedBucketOwner;

   public DeleteObjectTaggingRequest(String bucketName, String key) {
      this.bucketName = bucketName;
      this.key = key;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public DeleteObjectTaggingRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public DeleteObjectTaggingRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public DeleteObjectTaggingRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public DeleteObjectTaggingRequest withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }
}
