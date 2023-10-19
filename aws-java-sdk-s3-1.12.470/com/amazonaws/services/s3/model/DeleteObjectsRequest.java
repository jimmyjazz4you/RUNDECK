package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeleteObjectsRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private boolean quiet;
   private MultiFactorAuthentication mfa;
   private final List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
   private boolean isRequesterPays;
   private boolean bypassGovernanceRetention;
   private String expectedBucketOwner;

   public DeleteObjectsRequest(String bucketName) {
      this.setBucketName(bucketName);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public DeleteObjectsRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public DeleteObjectsRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public MultiFactorAuthentication getMfa() {
      return this.mfa;
   }

   public void setMfa(MultiFactorAuthentication mfa) {
      this.mfa = mfa;
   }

   public DeleteObjectsRequest withMfa(MultiFactorAuthentication mfa) {
      this.setMfa(mfa);
      return this;
   }

   public void setQuiet(boolean quiet) {
      this.quiet = quiet;
   }

   public boolean getQuiet() {
      return this.quiet;
   }

   public DeleteObjectsRequest withQuiet(boolean quiet) {
      this.setQuiet(quiet);
      return this;
   }

   public void setKeys(List<DeleteObjectsRequest.KeyVersion> keys) {
      this.keys.clear();
      this.keys.addAll(keys);
   }

   public DeleteObjectsRequest withKeys(List<DeleteObjectsRequest.KeyVersion> keys) {
      this.setKeys(keys);
      return this;
   }

   public List<DeleteObjectsRequest.KeyVersion> getKeys() {
      return this.keys;
   }

   public DeleteObjectsRequest withKeys(String... keys) {
      List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>(keys.length);

      for(String key : keys) {
         keyVersions.add(new DeleteObjectsRequest.KeyVersion(key));
      }

      this.setKeys(keyVersions);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public DeleteObjectsRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }

   public boolean getBypassGovernanceRetention() {
      return this.bypassGovernanceRetention;
   }

   public DeleteObjectsRequest withBypassGovernanceRetention(boolean bypassGovernanceRetention) {
      this.bypassGovernanceRetention = bypassGovernanceRetention;
      return this;
   }

   public void setBypassGovernanceRetention(boolean bypassGovernanceRetention) {
      this.withBypassGovernanceRetention(bypassGovernanceRetention);
   }

   public static class KeyVersion implements Serializable {
      private final String key;
      private final String version;

      public KeyVersion(String key) {
         this(key, null);
      }

      public KeyVersion(String key, String version) {
         this.key = key;
         this.version = version;
      }

      public String getKey() {
         return this.key;
      }

      public String getVersion() {
         return this.version;
      }
   }
}
