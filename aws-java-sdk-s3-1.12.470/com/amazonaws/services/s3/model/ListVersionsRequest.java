package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class ListVersionsRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String prefix;
   private String keyMarker;
   private String versionIdMarker;
   private String delimiter;
   private Integer maxResults;
   private String encodingType;
   private String expectedBucketOwner;

   public ListVersionsRequest() {
   }

   public ListVersionsRequest(String bucketName, String prefix, String keyMarker, String versionIdMarker, String delimiter, Integer maxResults) {
      this.setBucketName(bucketName);
      this.setPrefix(prefix);
      this.setKeyMarker(keyMarker);
      this.setVersionIdMarker(versionIdMarker);
      this.setDelimiter(delimiter);
      this.setMaxResults(maxResults);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public ListVersionsRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public ListVersionsRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public ListVersionsRequest withPrefix(String prefix) {
      this.setPrefix(prefix);
      return this;
   }

   public String getKeyMarker() {
      return this.keyMarker;
   }

   public void setKeyMarker(String keyMarker) {
      this.keyMarker = keyMarker;
   }

   public ListVersionsRequest withKeyMarker(String keyMarker) {
      this.setKeyMarker(keyMarker);
      return this;
   }

   public String getVersionIdMarker() {
      return this.versionIdMarker;
   }

   public void setVersionIdMarker(String versionIdMarker) {
      this.versionIdMarker = versionIdMarker;
   }

   public ListVersionsRequest withVersionIdMarker(String versionIdMarker) {
      this.setVersionIdMarker(versionIdMarker);
      return this;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public ListVersionsRequest withDelimiter(String delimiter) {
      this.setDelimiter(delimiter);
      return this;
   }

   public Integer getMaxResults() {
      return this.maxResults;
   }

   public void setMaxResults(Integer maxResults) {
      this.maxResults = maxResults;
   }

   public ListVersionsRequest withMaxResults(Integer maxResults) {
      this.setMaxResults(maxResults);
      return this;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public ListVersionsRequest withEncodingType(String encodingType) {
      this.setEncodingType(encodingType);
      return this;
   }
}
