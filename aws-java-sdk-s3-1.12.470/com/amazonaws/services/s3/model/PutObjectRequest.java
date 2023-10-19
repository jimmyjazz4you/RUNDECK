package com.amazonaws.services.s3.model;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

public class PutObjectRequest extends AbstractPutObjectRequest implements Serializable, ExpectedBucketOwnerRequest {
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   public PutObjectRequest(String bucketName, String key, File file) {
      super(bucketName, key, file);
   }

   public PutObjectRequest(String bucketName, String key, String redirectLocation) {
      super(bucketName, key, redirectLocation);
   }

   public PutObjectRequest(String bucketName, String key, InputStream input, ObjectMetadata metadata) {
      super(bucketName, key, input, metadata);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public PutObjectRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public PutObjectRequest clone() {
      PutObjectRequest request = (PutObjectRequest)super.clone();
      return this.copyPutObjectBaseTo(request);
   }

   public PutObjectRequest withBucketName(String bucketName) {
      return super.withBucketName(bucketName);
   }

   public PutObjectRequest withKey(String key) {
      return super.withKey(key);
   }

   public PutObjectRequest withStorageClass(String storageClass) {
      return super.withStorageClass(storageClass);
   }

   public PutObjectRequest withStorageClass(StorageClass storageClass) {
      return super.withStorageClass(storageClass);
   }

   public PutObjectRequest withFile(File file) {
      return super.withFile(file);
   }

   public PutObjectRequest withMetadata(ObjectMetadata metadata) {
      return super.withMetadata(metadata);
   }

   public PutObjectRequest withCannedAcl(CannedAccessControlList cannedAcl) {
      return super.withCannedAcl(cannedAcl);
   }

   public PutObjectRequest withAccessControlList(AccessControlList accessControlList) {
      return super.withAccessControlList(accessControlList);
   }

   public PutObjectRequest withInputStream(InputStream inputStream) {
      return super.withInputStream(inputStream);
   }

   public PutObjectRequest withRedirectLocation(String redirectLocation) {
      return super.withRedirectLocation(redirectLocation);
   }

   public PutObjectRequest withSSECustomerKey(SSECustomerKey sseKey) {
      return super.withSSECustomerKey(sseKey);
   }

   @Override
   public PutObjectRequest withTagging(ObjectTagging tagSet) {
      super.setTagging(tagSet);
      return this;
   }

   @Deprecated
   public PutObjectRequest withProgressListener(ProgressListener progressListener) {
      return super.withProgressListener(progressListener);
   }

   public PutObjectRequest withSSEAwsKeyManagementParams(SSEAwsKeyManagementParams sseAwsKeyManagementParams) {
      return super.withSSEAwsKeyManagementParams(sseAwsKeyManagementParams);
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public PutObjectRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
