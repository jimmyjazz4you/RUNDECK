package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CopyObjectRequest
   extends AmazonWebServiceRequest
   implements SSEAwsKeyManagementParamsProvider,
   Serializable,
   S3AccelerateUnsupported,
   ExpectedBucketOwnerRequest,
   ExpectedSourceBucketOwnerRequest {
   private String expectedBucketOwner;
   private String expectedSourceBucketOwner;
   private String sourceBucketName;
   private String sourceKey;
   private String sourceVersionId;
   private String destinationBucketName;
   private String destinationKey;
   private String storageClass;
   private ObjectMetadata newObjectMetadata;
   private CannedAccessControlList cannedACL;
   private AccessControlList accessControlList;
   private List<String> matchingETagConstraints = new ArrayList<>();
   private List<String> nonmatchingEtagConstraints = new ArrayList<>();
   private Date unmodifiedSinceConstraint;
   private Date modifiedSinceConstraint;
   private String redirectLocation;
   private SSECustomerKey sourceSSECustomerKey;
   private SSECustomerKey destinationSSECustomerKey;
   private SSEAwsKeyManagementParams sseAwsKeyManagementParams;
   private boolean isRequesterPays;
   private ObjectTagging newObjectTagging;
   private String metadataDirective;
   private String objectLockMode;
   private Date objectLockRetainUntilDate;
   private String objectLockLegalHoldStatus;
   private Boolean bucketKeyEnabled;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public CopyObjectRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   @Override
   public String getExpectedSourceBucketOwner() {
      return this.expectedSourceBucketOwner;
   }

   public CopyObjectRequest withExpectedSourceBucketOwner(String expectedSourceBucketOwner) {
      this.expectedSourceBucketOwner = expectedSourceBucketOwner;
      return this;
   }

   @Override
   public void setExpectedSourceBucketOwner(String expectedSourceBucketOwner) {
      this.withExpectedSourceBucketOwner(expectedSourceBucketOwner);
   }

   public CopyObjectRequest() {
   }

   public CopyObjectRequest(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
      this(sourceBucketName, sourceKey, null, destinationBucketName, destinationKey);
   }

   public CopyObjectRequest(String sourceBucketName, String sourceKey, String sourceVersionId, String destinationBucketName, String destinationKey) {
      this.sourceBucketName = sourceBucketName;
      this.sourceKey = sourceKey;
      this.sourceVersionId = sourceVersionId;
      this.destinationBucketName = destinationBucketName;
      this.destinationKey = destinationKey;
   }

   public String getSourceBucketName() {
      return this.sourceBucketName;
   }

   public void setSourceBucketName(String sourceBucketName) {
      this.sourceBucketName = sourceBucketName;
   }

   public CopyObjectRequest withSourceBucketName(String sourceBucketName) {
      this.setSourceBucketName(sourceBucketName);
      return this;
   }

   public String getSourceKey() {
      return this.sourceKey;
   }

   public void setSourceKey(String sourceKey) {
      this.sourceKey = sourceKey;
   }

   public CopyObjectRequest withSourceKey(String sourceKey) {
      this.setSourceKey(sourceKey);
      return this;
   }

   public String getSourceVersionId() {
      return this.sourceVersionId;
   }

   public void setSourceVersionId(String sourceVersionId) {
      this.sourceVersionId = sourceVersionId;
   }

   public CopyObjectRequest withSourceVersionId(String sourceVersionId) {
      this.setSourceVersionId(sourceVersionId);
      return this;
   }

   public String getDestinationBucketName() {
      return this.destinationBucketName;
   }

   public void setDestinationBucketName(String destinationBucketName) {
      this.destinationBucketName = destinationBucketName;
   }

   public CopyObjectRequest withDestinationBucketName(String destinationBucketName) {
      this.setDestinationBucketName(destinationBucketName);
      return this;
   }

   public String getDestinationKey() {
      return this.destinationKey;
   }

   public void setDestinationKey(String destinationKey) {
      this.destinationKey = destinationKey;
   }

   public CopyObjectRequest withDestinationKey(String destinationKey) {
      this.setDestinationKey(destinationKey);
      return this;
   }

   public String getStorageClass() {
      return this.storageClass;
   }

   public void setStorageClass(String storageClass) {
      this.storageClass = storageClass;
   }

   public CopyObjectRequest withStorageClass(String storageClass) {
      this.setStorageClass(storageClass);
      return this;
   }

   public void setStorageClass(StorageClass storageClass) {
      this.storageClass = storageClass.toString();
   }

   public CopyObjectRequest withStorageClass(StorageClass storageClass) {
      this.setStorageClass(storageClass);
      return this;
   }

   public CannedAccessControlList getCannedAccessControlList() {
      return this.cannedACL;
   }

   public void setCannedAccessControlList(CannedAccessControlList cannedACL) {
      this.cannedACL = cannedACL;
   }

   public CopyObjectRequest withCannedAccessControlList(CannedAccessControlList cannedACL) {
      this.setCannedAccessControlList(cannedACL);
      return this;
   }

   public AccessControlList getAccessControlList() {
      return this.accessControlList;
   }

   public void setAccessControlList(AccessControlList accessControlList) {
      this.accessControlList = accessControlList;
   }

   public CopyObjectRequest withAccessControlList(AccessControlList accessControlList) {
      this.setAccessControlList(accessControlList);
      return this;
   }

   public ObjectMetadata getNewObjectMetadata() {
      return this.newObjectMetadata;
   }

   public void setNewObjectMetadata(ObjectMetadata newObjectMetadata) {
      this.newObjectMetadata = newObjectMetadata;
   }

   public CopyObjectRequest withNewObjectMetadata(ObjectMetadata newObjectMetadata) {
      this.setNewObjectMetadata(newObjectMetadata);
      return this;
   }

   public List<String> getMatchingETagConstraints() {
      return this.matchingETagConstraints;
   }

   public void setMatchingETagConstraints(List<String> eTagList) {
      this.matchingETagConstraints = eTagList;
   }

   public CopyObjectRequest withMatchingETagConstraint(String eTag) {
      this.matchingETagConstraints.add(eTag);
      return this;
   }

   public List<String> getNonmatchingETagConstraints() {
      return this.nonmatchingEtagConstraints;
   }

   public void setNonmatchingETagConstraints(List<String> eTagList) {
      this.nonmatchingEtagConstraints = eTagList;
   }

   public CopyObjectRequest withNonmatchingETagConstraint(String eTag) {
      this.nonmatchingEtagConstraints.add(eTag);
      return this;
   }

   public Date getUnmodifiedSinceConstraint() {
      return this.unmodifiedSinceConstraint;
   }

   public void setUnmodifiedSinceConstraint(Date date) {
      this.unmodifiedSinceConstraint = date;
   }

   public CopyObjectRequest withUnmodifiedSinceConstraint(Date date) {
      this.setUnmodifiedSinceConstraint(date);
      return this;
   }

   public Date getModifiedSinceConstraint() {
      return this.modifiedSinceConstraint;
   }

   public void setModifiedSinceConstraint(Date date) {
      this.modifiedSinceConstraint = date;
   }

   public CopyObjectRequest withModifiedSinceConstraint(Date date) {
      this.setModifiedSinceConstraint(date);
      return this;
   }

   public void setRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
   }

   public String getRedirectLocation() {
      return this.redirectLocation;
   }

   public CopyObjectRequest withRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
      return this;
   }

   public SSECustomerKey getSourceSSECustomerKey() {
      return this.sourceSSECustomerKey;
   }

   public void setSourceSSECustomerKey(SSECustomerKey sseKey) {
      this.sourceSSECustomerKey = sseKey;
   }

   public CopyObjectRequest withSourceSSECustomerKey(SSECustomerKey sseKey) {
      this.setSourceSSECustomerKey(sseKey);
      return this;
   }

   public SSECustomerKey getDestinationSSECustomerKey() {
      return this.destinationSSECustomerKey;
   }

   public void setDestinationSSECustomerKey(SSECustomerKey sseKey) {
      if (sseKey != null && this.sseAwsKeyManagementParams != null) {
         throw new IllegalArgumentException("Either SSECustomerKey or SSEAwsKeyManagementParams must not be set at the same time.");
      } else {
         this.destinationSSECustomerKey = sseKey;
      }
   }

   public CopyObjectRequest withDestinationSSECustomerKey(SSECustomerKey sseKey) {
      this.setDestinationSSECustomerKey(sseKey);
      return this;
   }

   @Override
   public SSEAwsKeyManagementParams getSSEAwsKeyManagementParams() {
      return this.sseAwsKeyManagementParams;
   }

   public void setSSEAwsKeyManagementParams(SSEAwsKeyManagementParams params) {
      if (params != null && this.destinationSSECustomerKey != null) {
         throw new IllegalArgumentException("Either SSECustomerKey or SSEAwsKeyManagementParams must not be set at the same time.");
      } else {
         this.sseAwsKeyManagementParams = params;
      }
   }

   public CopyObjectRequest withSSEAwsKeyManagementParams(SSEAwsKeyManagementParams sseAwsKeyManagementParams) {
      this.setSSEAwsKeyManagementParams(sseAwsKeyManagementParams);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public CopyObjectRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }

   public ObjectTagging getNewObjectTagging() {
      return this.newObjectTagging;
   }

   public void setNewObjectTagging(ObjectTagging newObjectTagging) {
      this.newObjectTagging = newObjectTagging;
   }

   public CopyObjectRequest withNewObjectTagging(ObjectTagging newObjectTagging) {
      this.setNewObjectTagging(newObjectTagging);
      return this;
   }

   public String getMetadataDirective() {
      return this.metadataDirective;
   }

   public void setMetadataDirective(String metadataDirective) {
      this.metadataDirective = metadataDirective;
   }

   public CopyObjectRequest withMetadataDirective(String metadataDirective) {
      this.setMetadataDirective(metadataDirective);
      return this;
   }

   public CopyObjectRequest withMetadataDirective(MetadataDirective metadataDirective) {
      return this.withMetadataDirective(metadataDirective == null ? null : metadataDirective.toString());
   }

   public String getObjectLockMode() {
      return this.objectLockMode;
   }

   public CopyObjectRequest withObjectLockMode(String objectLockMode) {
      this.objectLockMode = objectLockMode;
      return this;
   }

   public CopyObjectRequest withObjectLockMode(ObjectLockMode objectLockMode) {
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

   public CopyObjectRequest withObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.objectLockRetainUntilDate = objectLockRetainUntilDate;
      return this;
   }

   public void setObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.withObjectLockRetainUntilDate(objectLockRetainUntilDate);
   }

   public String getObjectLockLegalHoldStatus() {
      return this.objectLockLegalHoldStatus;
   }

   public CopyObjectRequest withObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.objectLockLegalHoldStatus = objectLockLegalHoldStatus;
      return this;
   }

   public CopyObjectRequest withObjectLockLegalHoldStatus(ObjectLockLegalHoldStatus objectLockLegalHoldStatus) {
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

   public CopyObjectRequest withBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.setBucketKeyEnabled(bucketKeyEnabled);
      return this;
   }
}
