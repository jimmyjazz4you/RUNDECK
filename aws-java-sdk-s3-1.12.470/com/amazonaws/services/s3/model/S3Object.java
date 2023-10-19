package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class S3Object implements Closeable, Serializable, S3RequesterChargedResult {
   private static final long serialVersionUID = 1L;
   private String key = null;
   private String bucketName = null;
   private ObjectMetadata metadata = new ObjectMetadata();
   private transient S3ObjectInputStream objectContent;
   private String redirectLocation;
   private Integer taggingCount;
   private boolean isRequesterCharged;

   public ObjectMetadata getObjectMetadata() {
      return this.metadata;
   }

   public void setObjectMetadata(ObjectMetadata metadata) {
      this.metadata = metadata;
   }

   public S3ObjectInputStream getObjectContent() {
      return this.objectContent;
   }

   public void setObjectContent(S3ObjectInputStream objectContent) {
      this.objectContent = objectContent;
   }

   public void setObjectContent(InputStream objectContent) {
      this.setObjectContent(new S3ObjectInputStream(objectContent, this.objectContent != null ? this.objectContent.getHttpRequest() : null));
   }

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

   public String getRedirectLocation() {
      return this.redirectLocation;
   }

   public void setRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
   }

   public Integer getTaggingCount() {
      return this.taggingCount;
   }

   public void setTaggingCount(Integer taggingCount) {
      this.taggingCount = taggingCount;
   }

   @Override
   public String toString() {
      return "S3Object [key=" + this.getKey() + ",bucket=" + (this.bucketName == null ? "<Unknown>" : this.bucketName) + "]";
   }

   @Override
   public void close() throws IOException {
      InputStream is = this.getObjectContent();
      if (is != null) {
         is.close();
      }
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
