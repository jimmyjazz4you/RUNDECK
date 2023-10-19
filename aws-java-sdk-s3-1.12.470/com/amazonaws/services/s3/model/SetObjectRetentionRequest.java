package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetObjectRetentionRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucket;
   private String key;
   private ObjectLockRetention retention;
   private boolean isRequesterPays;
   private String versionId;
   private boolean bypassGovernanceRetention;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetObjectRetentionRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetObjectRetentionRequest withBucketName(String bucket) {
      this.bucket = bucket;
      return this;
   }

   public void setBucketName(String bucket) {
      this.withBucketName(bucket);
   }

   public String getKey() {
      return this.key;
   }

   public SetObjectRetentionRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public void setKey(String key) {
      this.withKey(key);
   }

   public ObjectLockRetention getRetention() {
      return this.retention;
   }

   public SetObjectRetentionRequest withRetention(ObjectLockRetention retention) {
      this.retention = retention;
      return this;
   }

   public void setRetention(ObjectLockRetention retention) {
      this.withRetention(retention);
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public SetObjectRetentionRequest withRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
      return this;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.withRequesterPays(isRequesterPays);
   }

   public String getVersionId() {
      return this.versionId;
   }

   public SetObjectRetentionRequest withVersionId(String versionId) {
      this.versionId = versionId;
      return this;
   }

   public void setVersionId(String versionId) {
      this.withVersionId(versionId);
   }

   public boolean getBypassGovernanceRetention() {
      return this.bypassGovernanceRetention;
   }

   public SetObjectRetentionRequest withBypassGovernanceRetention(boolean bypassGovernanceRetention) {
      this.bypassGovernanceRetention = bypassGovernanceRetention;
      return this;
   }

   public void setBypassGovernanceRetention(boolean bypassGovernanceRetention) {
      this.withBypassGovernanceRetention(bypassGovernanceRetention);
   }
}
