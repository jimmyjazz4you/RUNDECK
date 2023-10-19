package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetObjectTaggingRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private String versionId;
   private String expectedBucketOwner;
   private boolean isRequesterPays;

   public GetObjectTaggingRequest(String bucketName, String key, String versionId) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = versionId;
   }

   public GetObjectTaggingRequest(String bucketName, String key) {
      this(bucketName, key, null);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetObjectTaggingRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public GetObjectTaggingRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public GetObjectTaggingRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public GetObjectTaggingRequest withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public GetObjectTaggingRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
