package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetObjectAclRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private final String bucketName;
   private final String key;
   private final String versionId;
   private final AccessControlList acl;
   private final CannedAccessControlList cannedAcl;
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   public SetObjectAclRequest(String bucketName, String key, AccessControlList acl) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = null;
      this.acl = acl;
      this.cannedAcl = null;
   }

   public SetObjectAclRequest(String bucketName, String key, CannedAccessControlList acl) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = null;
      this.acl = null;
      this.cannedAcl = acl;
   }

   public SetObjectAclRequest(String bucketName, String key, String versionId, AccessControlList acl) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = versionId;
      this.acl = acl;
      this.cannedAcl = null;
   }

   public SetObjectAclRequest(String bucketName, String key, String versionId, CannedAccessControlList acl) {
      this.bucketName = bucketName;
      this.key = key;
      this.versionId = versionId;
      this.acl = null;
      this.cannedAcl = acl;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetObjectAclRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public String getKey() {
      return this.key;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public AccessControlList getAcl() {
      return this.acl;
   }

   public CannedAccessControlList getCannedAcl() {
      return this.cannedAcl;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public SetObjectAclRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
