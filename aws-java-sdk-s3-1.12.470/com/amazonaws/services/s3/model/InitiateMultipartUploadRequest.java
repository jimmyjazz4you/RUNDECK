package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;
import java.util.Date;

public class InitiateMultipartUploadRequest
   extends AmazonWebServiceRequest
   implements SSECustomerKeyProvider,
   SSEAwsKeyManagementParamsProvider,
   Serializable,
   ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   public ObjectMetadata objectMetadata;
   private CannedAccessControlList cannedACL;
   private AccessControlList accessControlList;
   private StorageClass storageClass;
   private String redirectLocation;
   private SSECustomerKey sseCustomerKey;
   private SSEAwsKeyManagementParams sseAwsKeyManagementParams;
   private boolean isRequesterPays;
   private ObjectTagging tagging;
   private String objectLockMode;
   private Date objectLockRetainUntilDate;
   private String objectLockLegalHoldStatus;
   private String expectedBucketOwner;
   private Boolean bucketKeyEnabled;

   public InitiateMultipartUploadRequest(String bucketName, String key) {
      this.bucketName = bucketName;
      this.key = key;
   }

   public InitiateMultipartUploadRequest(String bucketName, String key, ObjectMetadata objectMetadata) {
      this.bucketName = bucketName;
      this.key = key;
      this.objectMetadata = objectMetadata;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public InitiateMultipartUploadRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public InitiateMultipartUploadRequest withBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public InitiateMultipartUploadRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public CannedAccessControlList getCannedACL() {
      return this.cannedACL;
   }

   public void setCannedACL(CannedAccessControlList cannedACL) {
      this.cannedACL = cannedACL;
   }

   public InitiateMultipartUploadRequest withCannedACL(CannedAccessControlList acl) {
      this.cannedACL = acl;
      return this;
   }

   public AccessControlList getAccessControlList() {
      return this.accessControlList;
   }

   public void setAccessControlList(AccessControlList accessControlList) {
      this.accessControlList = accessControlList;
   }

   public InitiateMultipartUploadRequest withAccessControlList(AccessControlList accessControlList) {
      this.setAccessControlList(accessControlList);
      return this;
   }

   public StorageClass getStorageClass() {
      return this.storageClass;
   }

   public void setStorageClass(StorageClass storageClass) {
      this.storageClass = storageClass;
   }

   public InitiateMultipartUploadRequest withStorageClass(StorageClass storageClass) {
      this.storageClass = storageClass;
      return this;
   }

   public InitiateMultipartUploadRequest withStorageClass(String storageClass) {
      if (storageClass != null) {
         this.storageClass = StorageClass.fromValue(storageClass);
      } else {
         this.storageClass = null;
      }

      return this;
   }

   public ObjectMetadata getObjectMetadata() {
      return this.objectMetadata;
   }

   public void setObjectMetadata(ObjectMetadata objectMetadata) {
      this.objectMetadata = objectMetadata;
   }

   public InitiateMultipartUploadRequest withObjectMetadata(ObjectMetadata objectMetadata) {
      this.setObjectMetadata(objectMetadata);
      return this;
   }

   public void setRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
   }

   public String getRedirectLocation() {
      return this.redirectLocation;
   }

   public InitiateMultipartUploadRequest withRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
      return this;
   }

   @Override
   public SSECustomerKey getSSECustomerKey() {
      return this.sseCustomerKey;
   }

   public void setSSECustomerKey(SSECustomerKey sseKey) {
      if (sseKey != null && this.sseAwsKeyManagementParams != null) {
         throw new IllegalArgumentException("Either SSECustomerKey or SSEAwsKeyManagementParams must not be set at the same time.");
      } else {
         this.sseCustomerKey = sseKey;
      }
   }

   public InitiateMultipartUploadRequest withSSECustomerKey(SSECustomerKey sseKey) {
      this.setSSECustomerKey(sseKey);
      return this;
   }

   @Override
   public SSEAwsKeyManagementParams getSSEAwsKeyManagementParams() {
      return this.sseAwsKeyManagementParams;
   }

   public void setSSEAwsKeyManagementParams(SSEAwsKeyManagementParams params) {
      if (params != null && this.sseCustomerKey != null) {
         throw new IllegalArgumentException("Either SSECustomerKey or SSEAwsKeyManagementParams must not be set at the same time.");
      } else {
         this.sseAwsKeyManagementParams = params;
      }
   }

   public InitiateMultipartUploadRequest withSSEAwsKeyManagementParams(SSEAwsKeyManagementParams sseAwsKeyManagementParams) {
      this.setSSEAwsKeyManagementParams(sseAwsKeyManagementParams);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public InitiateMultipartUploadRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }

   public ObjectTagging getTagging() {
      return this.tagging;
   }

   public void setTagging(ObjectTagging tagging) {
      this.tagging = tagging;
   }

   public InitiateMultipartUploadRequest withTagging(ObjectTagging tagging) {
      this.setTagging(tagging);
      return this;
   }

   public String getObjectLockMode() {
      return this.objectLockMode;
   }

   public InitiateMultipartUploadRequest withObjectLockMode(String objectLockMode) {
      this.objectLockMode = objectLockMode;
      return this;
   }

   public InitiateMultipartUploadRequest withObjectLockMode(ObjectLockMode objectLockMode) {
      return this.withObjectLockMode(objectLockMode.toString());
   }

   public void setObjectLockMode(String objectLockMode) {
      this.withObjectLockMode(objectLockMode);
   }

   public void setObjectLockMode(ObjectLockMode objectLockMode) {
      this.setObjectLockMode(objectLockMode.toString());
   }

   public Date getObjectLockRetainUntilDate() {
      return this.objectLockRetainUntilDate;
   }

   public InitiateMultipartUploadRequest withObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.objectLockRetainUntilDate = objectLockRetainUntilDate;
      return this;
   }

   public void setObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.withObjectLockRetainUntilDate(objectLockRetainUntilDate);
   }

   public String getObjectLockLegalHoldStatus() {
      return this.objectLockLegalHoldStatus;
   }

   public InitiateMultipartUploadRequest withObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.objectLockLegalHoldStatus = objectLockLegalHoldStatus;
      return this;
   }

   public InitiateMultipartUploadRequest withObjectLockLegalHoldStatus(ObjectLockLegalHoldStatus objectLockLegalHoldStatus) {
      return this.withObjectLockLegalHoldStatus(objectLockLegalHoldStatus.toString());
   }

   public void setObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.withObjectLockLegalHoldStatus(objectLockLegalHoldStatus);
   }

   public void setObjectLockLegalHoldStatus(ObjectLockLegalHoldStatus objectLockLegalHoldStatus) {
      this.setObjectLockLegalHoldStatus(objectLockLegalHoldStatus.toString());
   }

   public Boolean getBucketKeyEnabled() {
      return this.bucketKeyEnabled;
   }

   public void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.bucketKeyEnabled = bucketKeyEnabled;
   }

   public InitiateMultipartUploadRequest withBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.setBucketKeyEnabled(bucketKeyEnabled);
      return this;
   }
}
