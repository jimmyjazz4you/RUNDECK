package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class ListObjectsV2Request extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String delimiter;
   private String encodingType;
   private Integer maxKeys;
   private String prefix;
   private String continuationToken;
   private boolean fetchOwner;
   private String startAfter;
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public ListObjectsV2Request withExpectedBucketOwner(String expectedBucketOwner) {
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

   public ListObjectsV2Request withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public ListObjectsV2Request withDelimiter(String delimiter) {
      this.setDelimiter(delimiter);
      return this;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public ListObjectsV2Request withEncodingType(String encodingType) {
      this.setEncodingType(encodingType);
      return this;
   }

   public Integer getMaxKeys() {
      return this.maxKeys;
   }

   public void setMaxKeys(Integer maxKeys) {
      this.maxKeys = maxKeys;
   }

   public ListObjectsV2Request withMaxKeys(Integer maxKeys) {
      this.setMaxKeys(maxKeys);
      return this;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public ListObjectsV2Request withPrefix(String prefix) {
      this.setPrefix(prefix);
      return this;
   }

   public String getContinuationToken() {
      return this.continuationToken;
   }

   public void setContinuationToken(String continuationToken) {
      this.continuationToken = continuationToken;
   }

   public ListObjectsV2Request withContinuationToken(String continuationToken) {
      this.setContinuationToken(continuationToken);
      return this;
   }

   public boolean isFetchOwner() {
      return this.fetchOwner;
   }

   public void setFetchOwner(boolean fetchOwner) {
      this.fetchOwner = fetchOwner;
   }

   public ListObjectsV2Request withFetchOwner(boolean fetchOwner) {
      this.setFetchOwner(fetchOwner);
      return this;
   }

   public String getStartAfter() {
      return this.startAfter;
   }

   public void setStartAfter(String startAfter) {
      this.startAfter = startAfter;
   }

   public ListObjectsV2Request withStartAfter(String startAfter) {
      this.setStartAfter(startAfter);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public ListObjectsV2Request withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
