package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListObjectsV2Result implements Serializable {
   private List<S3ObjectSummary> objectSummaries = new ArrayList<>();
   private List<String> commonPrefixes = new ArrayList<>();
   private boolean isTruncated;
   private String bucketName;
   private int keyCount;
   private String nextContinuationToken;
   private String prefix;
   private String delimiter;
   private int maxKeys;
   private String encodingType;
   private String continuationToken;
   private String startAfter;

   public boolean isTruncated() {
      return this.isTruncated;
   }

   public void setTruncated(boolean isTruncated) {
      this.isTruncated = isTruncated;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public String getContinuationToken() {
      return this.continuationToken;
   }

   public void setContinuationToken(String continuationToken) {
      this.continuationToken = continuationToken;
   }

   public String getNextContinuationToken() {
      return this.nextContinuationToken;
   }

   public void setNextContinuationToken(String nextContinuationToken) {
      this.nextContinuationToken = nextContinuationToken;
   }

   public int getKeyCount() {
      return this.keyCount;
   }

   public void setKeyCount(int keyCount) {
      this.keyCount = keyCount;
   }

   public int getMaxKeys() {
      return this.maxKeys;
   }

   public void setMaxKeys(int maxKeys) {
      this.maxKeys = maxKeys;
   }

   public String getStartAfter() {
      return this.startAfter;
   }

   public void setStartAfter(String startAfter) {
      this.startAfter = startAfter;
   }

   public List<S3ObjectSummary> getObjectSummaries() {
      return this.objectSummaries;
   }

   public List<String> getCommonPrefixes() {
      return this.commonPrefixes;
   }

   public void setCommonPrefixes(List<String> commonPrefixes) {
      this.commonPrefixes = commonPrefixes;
   }
}
