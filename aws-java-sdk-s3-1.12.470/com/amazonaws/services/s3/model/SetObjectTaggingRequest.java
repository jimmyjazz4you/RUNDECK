package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetObjectTaggingRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private String versionId;
   private ObjectTagging tagging;
   private String expectedBucketOwner;
   private boolean isRequesterPays;

   public SetObjectTaggingRequest(String bucketName, String key, ObjectTagging tagging) {
      this(bucketName, key, null, tagging);
   }

   public SetObjectTaggingRequest(String bucketName, String key, String versionId, ObjectTagging tagging) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = versionId;
      this.tagging = tagging;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetObjectTaggingRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetObjectTaggingRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public SetObjectTaggingRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public SetObjectTaggingRequest withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }

   public ObjectTagging getTagging() {
      return this.tagging;
   }

   public void setTagging(ObjectTagging tagging) {
      this.tagging = tagging;
   }

   public SetObjectTaggingRequest withTagging(ObjectTagging tagging) {
      this.setTagging(tagging);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public SetObjectTaggingRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
