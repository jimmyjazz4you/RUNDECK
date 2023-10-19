package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class PresignedUrlUploadResult implements Serializable {
   private ObjectMetadata metadata;
   private String contentMd5;

   public void setContentMd5(String contentMd5) {
      this.contentMd5 = contentMd5;
   }

   public PresignedUrlUploadResult withContentMd5(String contentMd5) {
      this.setContentMd5(contentMd5);
      return this;
   }

   public String getContentMd5() {
      return this.contentMd5;
   }

   public ObjectMetadata getMetadata() {
      return this.metadata;
   }

   public void setMetadata(ObjectMetadata metadata) {
      this.metadata = metadata;
   }

   public PresignedUrlUploadResult withMetadata(ObjectMetadata metadata) {
      this.setMetadata(metadata);
      return this;
   }
}
