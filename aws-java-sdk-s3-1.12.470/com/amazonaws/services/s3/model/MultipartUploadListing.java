package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipartUploadListing implements Serializable {
   private String bucketName;
   private String keyMarker;
   private String delimiter;
   private String prefix;
   private String uploadIdMarker;
   private int maxUploads;
   private String encodingType;
   private boolean isTruncated;
   private String nextKeyMarker;
   private String nextUploadIdMarker;
   private List<MultipartUpload> multipartUploads;
   private List<String> commonPrefixes = new ArrayList<>();

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getKeyMarker() {
      return this.keyMarker;
   }

   public void setKeyMarker(String keyMarker) {
      this.keyMarker = keyMarker;
   }

   public String getUploadIdMarker() {
      return this.uploadIdMarker;
   }

   public void setUploadIdMarker(String uploadIdMarker) {
      this.uploadIdMarker = uploadIdMarker;
   }

   public String getNextKeyMarker() {
      return this.nextKeyMarker;
   }

   public void setNextKeyMarker(String nextKeyMarker) {
      this.nextKeyMarker = nextKeyMarker;
   }

   public String getNextUploadIdMarker() {
      return this.nextUploadIdMarker;
   }

   public void setNextUploadIdMarker(String nextUploadIdMarker) {
      this.nextUploadIdMarker = nextUploadIdMarker;
   }

   public int getMaxUploads() {
      return this.maxUploads;
   }

   public void setMaxUploads(int maxUploads) {
      this.maxUploads = maxUploads;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public void setEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public boolean isTruncated() {
      return this.isTruncated;
   }

   public void setTruncated(boolean isTruncated) {
      this.isTruncated = isTruncated;
   }

   public List<MultipartUpload> getMultipartUploads() {
      if (this.multipartUploads == null) {
         this.multipartUploads = new ArrayList<>();
      }

      return this.multipartUploads;
   }

   public void setMultipartUploads(List<MultipartUpload> multipartUploads) {
      this.multipartUploads = multipartUploads;
   }

   public List<String> getCommonPrefixes() {
      return this.commonPrefixes;
   }

   public void setCommonPrefixes(List<String> commonPrefixes) {
      this.commonPrefixes = commonPrefixes;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }
}
