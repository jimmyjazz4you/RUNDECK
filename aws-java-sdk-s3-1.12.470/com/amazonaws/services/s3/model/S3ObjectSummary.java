package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Date;

public class S3ObjectSummary implements Serializable {
   protected String bucketName;
   protected String key;
   protected String eTag;
   protected long size;
   protected Date lastModified;
   protected String storageClass;
   protected Owner owner;

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

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String eTag) {
      this.eTag = eTag;
   }

   public long getSize() {
      return this.size;
   }

   public void setSize(long size) {
      this.size = size;
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

   public String getStorageClass() {
      return this.storageClass;
   }

   public void setStorageClass(String storageClass) {
      this.storageClass = storageClass;
   }

   @Override
   public String toString() {
      return "S3ObjectSummary{bucketName='"
         + this.bucketName
         + '\''
         + ", key='"
         + this.key
         + '\''
         + ", eTag='"
         + this.eTag
         + '\''
         + ", size="
         + this.size
         + ", lastModified="
         + this.lastModified
         + ", storageClass='"
         + this.storageClass
         + '\''
         + ", owner="
         + this.owner
         + '}';
   }
}
