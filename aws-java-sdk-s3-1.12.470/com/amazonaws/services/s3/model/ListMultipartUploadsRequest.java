package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class ListMultipartUploadsRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String delimiter;
   private String prefix;
   private Integer maxUploads;
   private String keyMarker;
   private String uploadIdMarker;
   private String encodingType;
   private String expectedBucketOwner;

   public ListMultipartUploadsRequest(String bucketName) {
      this.bucketName = bucketName;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public ListMultipartUploadsRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public ListMultipartUploadsRequest withBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
   }

   public Integer getMaxUploads() {
      return this.maxUploads;
   }

   public void setMaxUploads(Integer maxUploads) {
      this.maxUploads = maxUploads;
   }

   public ListMultipartUploadsRequest withMaxUploads(int maxUploadsInt) {
      this.maxUploads = maxUploadsInt;
      return this;
   }

   public String getKeyMarker() {
      return this.keyMarker;
   }

   public void setKeyMarker(String keyMarker) {
      this.keyMarker = keyMarker;
   }

   public ListMultipartUploadsRequest withKeyMarker(String keyMarker) {
      this.keyMarker = keyMarker;
      return this;
   }

   public String getUploadIdMarker() {
      return this.uploadIdMarker;
   }

   public void setUploadIdMarker(String uploadIdMarker) {
      this.uploadIdMarker = uploadIdMarker;
   }

   public ListMultipartUploadsRequest withUploadIdMarker(String uploadIdMarker) {
      this.uploadIdMarker = uploadIdMarker;
      return this;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public ListMultipartUploadsRequest withDelimiter(String delimiter) {
      this.setDelimiter(delimiter);
      return this;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public ListMultipartUploadsRequest withPrefix(String prefix) {
      this.setPrefix(prefix);
      return this;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public ListMultipartUploadsRequest withEncodingType(String encodingType) {
      this.setEncodingType(encodingType);
      return this;
   }
}
