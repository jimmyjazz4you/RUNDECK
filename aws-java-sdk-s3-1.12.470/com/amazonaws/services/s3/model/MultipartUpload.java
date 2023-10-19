package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Date;

public class MultipartUpload implements Serializable {
   private String key;
   private String uploadId;
   private Owner owner;
   private Owner initiator;
   private String storageClass;
   private Date initiated;

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

   public Date getInitiated() {
      return this.initiated;
   }

   public void setInitiated(Date initiated) {
      this.initiated = initiated;
   }
}
