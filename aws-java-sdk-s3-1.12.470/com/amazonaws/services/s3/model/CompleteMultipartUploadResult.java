package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.ObjectExpirationResult;
import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import com.amazonaws.services.s3.internal.SSEResultBase;
import java.io.Serializable;
import java.util.Date;

public class CompleteMultipartUploadResult extends SSEResultBase implements ObjectExpirationResult, S3RequesterChargedResult, Serializable {
   private String bucketName;
   private String key;
   private String location;
   private String eTag;
   private String versionId;
   private Date expirationTime;
   private String expirationTimeRuleId;
   private boolean isRequesterCharged;

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String location) {
      this.location = location;
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

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String etag) {
      this.eTag = etag;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   @Override
   public Date getExpirationTime() {
      return this.expirationTime;
   }

   @Override
   public void setExpirationTime(Date expirationTime) {
      this.expirationTime = expirationTime;
   }

   @Override
   public String getExpirationTimeRuleId() {
      return this.expirationTimeRuleId;
   }

   @Override
   public void setExpirationTimeRuleId(String expirationTimeRuleId) {
      this.expirationTimeRuleId = expirationTimeRuleId;
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
