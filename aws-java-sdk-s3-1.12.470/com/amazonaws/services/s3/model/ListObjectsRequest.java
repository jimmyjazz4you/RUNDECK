package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class ListObjectsRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String prefix;
   private String marker;
   private String delimiter;
   private Integer maxKeys;
   private String encodingType;
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   public ListObjectsRequest() {
   }

   public ListObjectsRequest(String bucketName, String prefix, String marker, String delimiter, Integer maxKeys) {
      this.setBucketName(bucketName);
      this.setPrefix(prefix);
      this.setMarker(marker);
      this.setDelimiter(delimiter);
      this.setMaxKeys(maxKeys);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public ListObjectsRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public ListObjectsRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public ListObjectsRequest withPrefix(String prefix) {
      this.setPrefix(prefix);
      return this;
   }

   public String getMarker() {
      return this.marker;
   }

   public void setMarker(String marker) {
      this.marker = marker;
   }

   public ListObjectsRequest withMarker(String marker) {
      this.setMarker(marker);
      return this;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public ListObjectsRequest withDelimiter(String delimiter) {
      this.setDelimiter(delimiter);
      return this;
   }

   public Integer getMaxKeys() {
      return this.maxKeys;
   }

   public void setMaxKeys(Integer maxKeys) {
      this.maxKeys = maxKeys;
   }

   public ListObjectsRequest withMaxKeys(Integer maxKeys) {
      this.setMaxKeys(maxKeys);
      return this;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public ListObjectsRequest withEncodingType(String encodingType) {
      this.setEncodingType(encodingType);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public ListObjectsRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
