package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetObjectMetadataRequest extends AmazonWebServiceRequest implements SSECustomerKeyProvider, Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private String versionId;
   private boolean isRequesterPays;
   private SSECustomerKey sseCustomerKey;
   private Integer partNumber;
   private String expectedBucketOwner;

   public GetObjectMetadataRequest(String bucketName, String key) {
      this.setBucketName(bucketName);
      this.setKey(key);
   }

   public GetObjectMetadataRequest(String bucketName, String key, String versionId) {
      this(bucketName, key);
      this.setVersionId(versionId);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetObjectMetadataRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public GetObjectMetadataRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public GetObjectMetadataRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public GetObjectMetadataRequest withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public GetObjectMetadataRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }

   @Override
   public SSECustomerKey getSSECustomerKey() {
      return this.sseCustomerKey;
   }

   public void setSSECustomerKey(SSECustomerKey sseKey) {
      this.sseCustomerKey = sseKey;
   }

   public GetObjectMetadataRequest withSSECustomerKey(SSECustomerKey sseKey) {
      this.setSSECustomerKey(sseKey);
      return this;
   }

   public Integer getPartNumber() {
      return this.partNumber;
   }

   public void setPartNumber(Integer partNumber) {
      this.partNumber = partNumber;
   }

   public GetObjectMetadataRequest withPartNumber(Integer partNumber) {
      this.setPartNumber(partNumber);
      return this;
   }
}
