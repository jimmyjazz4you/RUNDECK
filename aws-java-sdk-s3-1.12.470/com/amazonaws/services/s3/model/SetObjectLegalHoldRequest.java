package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetObjectLegalHoldRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucket;
   private String key;
   private ObjectLockLegalHold legalHold;
   private boolean isRequesterPays;
   private String versionId;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetObjectLegalHoldRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetObjectLegalHoldRequest withBucketName(String bucket) {
      this.bucket = bucket;
      return this;
   }

   public void setBucketName(String bucket) {
      this.withBucketName(bucket);
   }

   public String getKey() {
      return this.key;
   }

   public SetObjectLegalHoldRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public void setKey(String key) {
      this.withKey(key);
   }

   public ObjectLockLegalHold getLegalHold() {
      return this.legalHold;
   }

   public SetObjectLegalHoldRequest withLegalHold(ObjectLockLegalHold legalHold) {
      this.legalHold = legalHold;
      return this;
   }

   public void setLegalHold(ObjectLockLegalHold legalHold) {
      this.withLegalHold(legalHold);
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public SetObjectLegalHoldRequest withRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
      return this;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public SetObjectLegalHoldRequest withVersionId(String versionId) {
      this.versionId = versionId;
      return this;
   }

   public void setVersionId(String versionId) {
      this.withVersionId(versionId);
   }
}
