package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

public class UploadPartRequest extends AmazonWebServiceRequest implements SSECustomerKeyProvider, S3DataSource, Serializable, ExpectedBucketOwnerRequest {
   private static final long serialVersionUID = 1L;
   private ObjectMetadata objectMetadata;
   private String bucketName;
   private String key;
   private String uploadId;
   private int partNumber;
   private long partSize;
   private String md5Digest;
   private transient InputStream inputStream;
   private File file;
   private long fileOffset;
   private boolean isLastPart;
   private SSECustomerKey sseCustomerKey;
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public UploadPartRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   @Override
   public void setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   @Override
   public InputStream getInputStream() {
      return this.inputStream;
   }

   public UploadPartRequest withInputStream(InputStream inputStream) {
      this.setInputStream(inputStream);
      return this;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public UploadPartRequest withBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public UploadPartRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public String getUploadId() {
      return this.uploadId;
   }

   public void setUploadId(String uploadId) {
      this.uploadId = uploadId;
   }

   public UploadPartRequest withUploadId(String uploadId) {
      this.uploadId = uploadId;
      return this;
   }

   public int getPartNumber() {
      return this.partNumber;
   }

   public void setPartNumber(int partNumber) {
      this.partNumber = partNumber;
   }

   public UploadPartRequest withPartNumber(int partNumber) {
      this.partNumber = partNumber;
      return this;
   }

   public long getPartSize() {
      return this.partSize;
   }

   public void setPartSize(long partSize) {
      this.partSize = partSize;
   }

   public UploadPartRequest withPartSize(long partSize) {
      this.partSize = partSize;
      return this;
   }

   public String getMd5Digest() {
      return this.md5Digest;
   }

   public void setMd5Digest(String md5Digest) {
      this.md5Digest = md5Digest;
   }

   public UploadPartRequest withMD5Digest(String md5Digest) {
      this.md5Digest = md5Digest;
      return this;
   }

   @Override
   public File getFile() {
      return this.file;
   }

   @Override
   public void setFile(File file) {
      this.file = file;
   }

   public UploadPartRequest withFile(File file) {
      this.setFile(file);
      return this;
   }

   public long getFileOffset() {
      return this.fileOffset;
   }

   public void setFileOffset(long fileOffset) {
      this.fileOffset = fileOffset;
   }

   public UploadPartRequest withFileOffset(long fileOffset) {
      this.setFileOffset(fileOffset);
      return this;
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
   public UploadPartRequest withProgressListener(ProgressListener progressListener) {
      this.setProgressListener(progressListener);
      return this;
   }

   public boolean isLastPart() {
      return this.isLastPart;
   }

   public void setLastPart(boolean isLastPart) {
      this.isLastPart = isLastPart;
   }

   public UploadPartRequest withLastPart(boolean isLastPart) {
      this.setLastPart(isLastPart);
      return this;
   }

   @Override
   public SSECustomerKey getSSECustomerKey() {
      return this.sseCustomerKey;
   }

   public void setSSECustomerKey(SSECustomerKey sseKey) {
      this.sseCustomerKey = sseKey;
   }

   public UploadPartRequest withSSECustomerKey(SSECustomerKey sseKey) {
      this.setSSECustomerKey(sseKey);
      return this;
   }

   public ObjectMetadata getObjectMetadata() {
      return this.objectMetadata;
   }

   public void setObjectMetadata(ObjectMetadata objectMetadata) {
      this.objectMetadata = objectMetadata;
   }

   public UploadPartRequest withObjectMetadata(ObjectMetadata objectMetadata) {
      this.setObjectMetadata(objectMetadata);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public UploadPartRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
