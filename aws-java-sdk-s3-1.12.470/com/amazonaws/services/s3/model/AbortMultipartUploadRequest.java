package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class AbortMultipartUploadRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private String uploadId;
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   public AbortMultipartUploadRequest(String bucketName, String key, String uploadId) {
      this.bucketName = bucketName;
      this.key = key;
      this.uploadId = uploadId;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public AbortMultipartUploadRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public void setBucketName(String value) {
      this.bucketName = value;
   }

   public AbortMultipartUploadRequest withBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public AbortMultipartUploadRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public String getUploadId() {
      return this.uploadId;
   }

   public void setUploadId(String uploadId) {
      this.uploadId = uploadId;
   }

   public AbortMultipartUploadRequest withUploadId(String uploadId) {
      this.uploadId = uploadId;
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public AbortMultipartUploadRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
