package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class DeleteVersionRequest extends AmazonWebServiceRequest implements Serializable {
   private String bucketName;
   private String key;
   private String versionId;
   private MultiFactorAuthentication mfa;
   private boolean bypassGovernanceRetention;

   public DeleteVersionRequest(String bucketName, String key, String versionId) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = versionId;
   }

   public DeleteVersionRequest(String bucketName, String key, String versionId, MultiFactorAuthentication mfa) {
      this(bucketName, key, versionId);
      this.mfa = mfa;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public DeleteVersionRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public DeleteVersionRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public DeleteVersionRequest withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }

   public MultiFactorAuthentication getMfa() {
      return this.mfa;
   }

   public void setMfa(MultiFactorAuthentication mfa) {
      this.mfa = mfa;
   }

   public DeleteVersionRequest withMfa(MultiFactorAuthentication mfa) {
      this.setMfa(mfa);
      return this;
   }

   public boolean getBypassGovernanceRetention() {
      return this.bypassGovernanceRetention;
   }

   public DeleteVersionRequest withBypassGovernanceRetention(boolean bypassGovernanceRetention) {
      this.bypassGovernanceRetention = bypassGovernanceRetention;
      return this;
   }

   public void setBypassGovernanceRetention(boolean bypassGovernanceRetention) {
      this.withBypassGovernanceRetention(bypassGovernanceRetention);
   }
}
