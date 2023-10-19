package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartListing implements Serializable, S3RequesterChargedResult {
   private String bucketName;
   private String key;
   private String uploadId;
   private Integer maxParts;
   private Integer partNumberMarker;
   private String encodingType;
   private Owner owner;
   private Owner initiator;
   private String storageClass;
   private boolean isTruncated;
   private Integer nextPartNumberMarker;
   private List<PartSummary> parts;
   private Date abortDate;
   private String abortRuleId;
   private boolean isRequesterCharged;

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public String getUploadId() {
      return this.uploadId;
   }

   public void setUploadId(String uploadId) {
      this.uploadId = uploadId;
   }

   public Owner getOwner() {
      return this.owner;
   }

   public void setOwner(Owner owner) {
      this.owner = owner;
   }

   public Owner getInitiator() {
      return this.initiator;
   }

   public void setInitiator(Owner initiator) {
      this.initiator = initiator;
   }

   public String getStorageClass() {
      return this.storageClass;
   }

   public void setStorageClass(String storageClass) {
      this.storageClass = storageClass;
   }

   public Integer getPartNumberMarker() {
      return this.partNumberMarker;
   }

   public void setPartNumberMarker(int partNumberMarker) {
      this.partNumberMarker = partNumberMarker;
   }

   public Integer getNextPartNumberMarker() {
      return this.nextPartNumberMarker;
   }

   public void setNextPartNumberMarker(int nextPartNumberMarker) {
      this.nextPartNumberMarker = nextPartNumberMarker;
   }

   public Integer getMaxParts() {
      return this.maxParts;
   }

   public void setMaxParts(int maxParts) {
      this.maxParts = maxParts;
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

   public List<PartSummary> getParts() {
      if (this.parts == null) {
         this.parts = new ArrayList<>();
      }

      return this.parts;
   }

   public void setParts(List<PartSummary> parts) {
      this.parts = parts;
   }

   public Date getAbortDate() {
      return this.abortDate;
   }

   public void setAbortDate(Date abortDate) {
      this.abortDate = abortDate;
   }

   public String getAbortRuleId() {
      return this.abortRuleId;
   }

   public void setAbortRuleId(String abortRuleId) {
      this.abortRuleId = abortRuleId;
   }

   @Override
   public boolean isRequesterCharged() {
      return this.isRequesterCharged;
   }

   @Override
   public void setRequesterCharged(boolean isRequesterCharged) {
      this.isRequesterCharged = isRequesterCharged;
   }
}
