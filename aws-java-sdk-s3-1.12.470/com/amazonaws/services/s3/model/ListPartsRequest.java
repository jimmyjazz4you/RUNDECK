package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class ListPartsRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private String uploadId;
   private Integer maxParts;
   private Integer partNumberMarker;
   private String encodingType;
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   public ListPartsRequest(String bucketName, String key, String uploadId) {
      this.bucketName = bucketName;
      this.key = key;
      this.uploadId = uploadId;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public ListPartsRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public ListPartsRequest withBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public ListPartsRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public String getUploadId() {
      return this.uploadId;
   }

   public void setUploadId(String uploadId) {
      this.uploadId = uploadId;
   }

   public ListPartsRequest withUploadId(String uploadId) {
      this.uploadId = uploadId;
      return this;
   }

   public Integer getMaxParts() {
      return this.maxParts;
   }

   public void setMaxParts(int maxParts) {
      this.maxParts = maxParts;
   }

   public ListPartsRequest withMaxParts(int maxParts) {
      this.maxParts = maxParts;
      return this;
   }

   public Integer getPartNumberMarker() {
      return this.partNumberMarker;
   }

   public void setPartNumberMarker(Integer partNumberMarker) {
      this.partNumberMarker = partNumberMarker;
   }

   public ListPartsRequest withPartNumberMarker(Integer partNumberMarker) {
      this.partNumberMarker = partNumberMarker;
      return this;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public ListPartsRequest withEncodingType(String encodingType) {
      this.setEncodingType(encodingType);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public ListPartsRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
