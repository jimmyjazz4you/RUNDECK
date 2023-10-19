package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

public abstract class AbstractPutObjectRequest
   extends AmazonWebServiceRequest
   implements Cloneable,
   SSECustomerKeyProvider,
   SSEAwsKeyManagementParamsProvider,
   S3DataSource,
   Serializable {
   private String bucketName;
   private String key;
   private File file;
   private transient InputStream inputStream;
   private ObjectMetadata metadata;
   private CannedAccessControlList cannedAcl;
   private AccessControlList accessControlList;
   private String storageClass;
   private String redirectLocation;
   private SSECustomerKey sseCustomerKey;
   private SSEAwsKeyManagementParams sseAwsKeyManagementParams;
   private ObjectTagging tagging;
   private String objectLockMode;
   private Date objectLockRetainUntilDate;
   private String objectLockLegalHoldStatus;
   private Boolean bucketKeyEnabled;

   public AbstractPutObjectRequest(String bucketName, String key, File file) {
      this.bucketName = bucketName;
      this.key = key;
      this.file = file;
   }

   public AbstractPutObjectRequest(String bucketName, String key, String redirectLocation) {
      this.bucketName = bucketName;
      this.key = key;
      this.redirectLocation = redirectLocation;
   }

   protected AbstractPutObjectRequest(String bucketName, String key, InputStream input, ObjectMetadata metadata) {
      this.bucketName = bucketName;
      this.key = key;
      this.inputStream = input;
      this.metadata = metadata;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public <T extends AbstractPutObjectRequest> T withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return (T)this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public <T extends AbstractPutObjectRequest> T withKey(String key) {
      this.setKey(key);
      return (T)this;
   }

   public String getStorageClass() {
      return this.storageClass;
   }

   public void setStorageClass(String storageClass) {
      this.storageClass = storageClass;
   }

   public <T extends AbstractPutObjectRequest> T withStorageClass(String storageClass) {
      this.setStorageClass(storageClass);
      return (T)this;
   }

   public void setStorageClass(StorageClass storageClass) {
      this.storageClass = storageClass.toString();
   }

   public <T extends AbstractPutObjectRequest> T withStorageClass(StorageClass storageClass) {
      this.setStorageClass(storageClass);
      return (T)this;
   }

   @Override
   public File getFile() {
      return this.file;
   }

   @Override
   public void setFile(File file) {
      this.file = file;
   }

   public <T extends AbstractPutObjectRequest> T withFile(File file) {
      this.setFile(file);
      return (T)this;
   }

   public ObjectMetadata getMetadata() {
      return this.metadata;
   }

   public void setMetadata(ObjectMetadata metadata) {
      this.metadata = metadata;
   }

   public <T extends AbstractPutObjectRequest> T withMetadata(ObjectMetadata metadata) {
      this.setMetadata(metadata);
      return (T)this;
   }

   public CannedAccessControlList getCannedAcl() {
      return this.cannedAcl;
   }

   public void setCannedAcl(CannedAccessControlList cannedAcl) {
      this.cannedAcl = cannedAcl;
   }

   public <T extends AbstractPutObjectRequest> T withCannedAcl(CannedAccessControlList cannedAcl) {
      this.setCannedAcl(cannedAcl);
      return (T)this;
   }

   public AccessControlList getAccessControlList() {
      return this.accessControlList;
   }

   public void setAccessControlList(AccessControlList accessControlList) {
      this.accessControlList = accessControlList;
   }

   public <T extends AbstractPutObjectRequest> T withAccessControlList(AccessControlList accessControlList) {
      this.setAccessControlList(accessControlList);
      return (T)this;
   }

   @Override
   public InputStream getInputStream() {
      return this.inputStream;
   }

   @Override
   public void setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public <T extends AbstractPutObjectRequest> T withInputStream(InputStream inputStream) {
      this.setInputStream(inputStream);
      return (T)this;
   }

   public void setRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
   }

   public String getRedirectLocation() {
      return this.redirectLocation;
   }

   public <T extends AbstractPutObjectRequest> T withRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
      return (T)this;
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

   public <T extends AbstractPutObjectRequest> T withSSECustomerKey(SSECustomerKey sseKey) {
      this.setSSECustomerKey(sseKey);
      return (T)this;
   }

   public ObjectTagging getTagging() {
      return this.tagging;
   }

   public void setTagging(ObjectTagging tagging) {
      this.tagging = tagging;
   }

   public <T extends PutObjectRequest> T withTagging(ObjectTagging tagSet) {
      this.setTagging(tagSet);
      return (T)this;
   }

   public String getObjectLockMode() {
      return this.objectLockMode;
   }

   public <T extends PutObjectRequest> T withObjectLockMode(String objectLockMode) {
      this.objectLockMode = objectLockMode;
      return (T)this;
   }

   public <T extends PutObjectRequest> T withObjectLockMode(ObjectLockMode objectLockMode) {
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

   public <T extends PutObjectRequest> T withObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.objectLockRetainUntilDate = objectLockRetainUntilDate;
      return (T)this;
   }

   public void setObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.withObjectLockRetainUntilDate(objectLockRetainUntilDate);
   }

   public String getObjectLockLegalHoldStatus() {
      return this.objectLockLegalHoldStatus;
   }

   public <T extends PutObjectRequest> T withObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.objectLockLegalHoldStatus = objectLockLegalHoldStatus;
      return (T)this;
   }

   public <T extends PutObjectRequest> T withObjectLockLegalHoldStatus(ObjectLockLegalHoldStatus objectLockLegalHoldStatus) {
      return this.withObjectLockLegalHoldStatus(objectLockLegalHoldStatus.toString());
   }

   public void setObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.withObjectLockLegalHoldStatus(objectLockLegalHoldStatus);
   }

   public void setObjectLockLegalHoldStatus(ObjectLockLegalHoldStatus objectLockLegalHoldStatus) {
      this.setObjectLockLegalHoldStatus(objectLockLegalHoldStatus.toString());
   }

   @Deprecated
   public void setProgressListener(ProgressListener progressListener) {
      this.setGeneralProgressListener(new LegacyS3ProgressListener(progressListener));
   }

   @Deprecated
   public ProgressListener getProgressListener() {
      com.amazonaws.event.ProgressListener generalProgressListener = this.getGeneralProgressListener();
      return generalProgressListener instanceof LegacyS3ProgressListener ? ((LegacyS3ProgressListener)generalProgressListener).unwrap() : null;
   }

   @Deprecated
   public <T extends AbstractPutObjectRequest> T withProgressListener(ProgressListener progressListener) {
      this.setProgressListener(progressListener);
      return (T)this;
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

   public <T extends AbstractPutObjectRequest> T withSSEAwsKeyManagementParams(SSEAwsKeyManagementParams sseAwsKeyManagementParams) {
      this.setSSEAwsKeyManagementParams(sseAwsKeyManagementParams);
      return (T)this;
   }

   public Boolean getBucketKeyEnabled() {
      return this.bucketKeyEnabled;
   }

   public void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.bucketKeyEnabled = bucketKeyEnabled;
   }

   public <T extends AbstractPutObjectRequest> T withBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.setBucketKeyEnabled(bucketKeyEnabled);
      return (T)this;
   }

   public AbstractPutObjectRequest clone() {
      return (AbstractPutObjectRequest)super.clone();
   }

   protected final <T extends AbstractPutObjectRequest> T copyPutObjectBaseTo(T target) {
      this.copyBaseTo(target);
      ObjectMetadata metadata = this.getMetadata();
      return target.<AbstractPutObjectRequest>withAccessControlList(this.getAccessControlList())
         .<AbstractPutObjectRequest>withCannedAcl(this.getCannedAcl())
         .<AbstractPutObjectRequest>withInputStream(this.getInputStream())
         .<AbstractPutObjectRequest>withMetadata(metadata == null ? null : metadata.clone())
         .<AbstractPutObjectRequest>withRedirectLocation(this.getRedirectLocation())
         .<AbstractPutObjectRequest>withStorageClass(this.getStorageClass())
         .<AbstractPutObjectRequest>withSSEAwsKeyManagementParams(this.getSSEAwsKeyManagementParams())
         .withSSECustomerKey(this.getSSECustomerKey());
   }
}
