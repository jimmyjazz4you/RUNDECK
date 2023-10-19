package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Date;

public class S3VersionSummary implements Serializable {
   protected String bucketName;
   private String key;
   private String versionId;
   private boolean isLatest;
   private Date lastModified;
   private Owner owner;
   private String eTag;
   private long size;
   private String storageClass;
   private boolean isDeleteMarker;

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

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String id) {
      this.versionId = id;
   }

   public boolean isLatest() {
      return this.isLatest;
   }

   public void setIsLatest(boolean isLatest) {
      this.isLatest = isLatest;
   }

   public Date getLastModified() {
      return this.lastModified;
   }

   public void setLastModified(Date lastModified) {
      this.lastModified = lastModified;
   }

   public Owner getOwner() {
      return this.owner;
   }

   public void setOwner(Owner owner) {
      this.owner = owner;
   }

   public boolean isDeleteMarker() {
      return this.isDeleteMarker;
   }

   public void setIsDeleteMarker(boolean isDeleteMarker) {
      this.isDeleteMarker = isDeleteMarker;
   }

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String eTag) {
      this.eTag = eTag;
   }

   public String getStorageClass() {
      return this.storageClass;
   }

   public void setStorageClass(String storageClass) {
      this.storageClass = storageClass;
   }

   public long getSize() {
      return this.size;
   }

   public void setSize(long size) {
      this.size = size;
   }
}
