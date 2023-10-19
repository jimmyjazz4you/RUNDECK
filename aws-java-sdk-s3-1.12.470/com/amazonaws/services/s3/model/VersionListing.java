package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VersionListing implements Serializable {
   private List<S3VersionSummary> versionSummaries = new ArrayList<>();
   private List<String> commonPrefixes = new ArrayList<>();
   private String bucketName;
   private String nextKeyMarker;
   private String nextVersionIdMarker;
   private boolean isTruncated;
   private String prefix;
   private String keyMarker;
   private String versionIdMarker;
   private int maxKeys;
   private String delimiter;
   private String encodingType;

   public List<S3VersionSummary> getVersionSummaries() {
      return this.versionSummaries;
   }

   public void setVersionSummaries(List<S3VersionSummary> versionSummaries) {
      this.versionSummaries = versionSummaries;
   }

   public List<String> getCommonPrefixes() {
      return this.commonPrefixes;
   }

   public void setCommonPrefixes(List<String> commonPrefixes) {
      this.commonPrefixes = commonPrefixes;
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

   public String getKeyMarker() {
      return this.keyMarker;
   }

   public void setKeyMarker(String keyMarker) {
      this.keyMarker = keyMarker;
   }

   public String getVersionIdMarker() {
      return this.versionIdMarker;
   }

   public void setVersionIdMarker(String versionIdMarker) {
      this.versionIdMarker = versionIdMarker;
   }

   public int getMaxKeys() {
      return this.maxKeys;
   }

   public void setMaxKeys(int maxKeys) {
      this.maxKeys = maxKeys;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public String getNextKeyMarker() {
      return this.nextKeyMarker;
   }

   public void setNextKeyMarker(String marker) {
      this.nextKeyMarker = marker;
   }

   public String getNextVersionIdMarker() {
      return this.nextVersionIdMarker;
   }

   public void setNextVersionIdMarker(String marker) {
      this.nextVersionIdMarker = marker;
   }

   public boolean isTruncated() {
      return this.isTruncated;
   }

   public void setTruncated(boolean isTruncated) {
      this.isTruncated = isTruncated;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }
}
