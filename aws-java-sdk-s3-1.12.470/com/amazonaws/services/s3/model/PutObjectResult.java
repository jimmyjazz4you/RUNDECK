package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.ObjectExpirationResult;
import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import com.amazonaws.services.s3.internal.S3VersionResult;
import com.amazonaws.services.s3.internal.SSEResultBase;
import java.io.Serializable;
import java.util.Date;

public class PutObjectResult extends SSEResultBase implements ObjectExpirationResult, S3RequesterChargedResult, S3VersionResult, Serializable {
   private String versionId;
   private String eTag;
   private Date expirationTime;
   private String expirationTimeRuleId;
   private String contentMd5;
   private ObjectMetadata metadata;
   private boolean isRequesterCharged;

   @Override
   public String getVersionId() {
      return this.versionId;
   }

   @Override
   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String eTag) {
      this.eTag = eTag;
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

   public void setContentMd5(String contentMd5) {
      this.contentMd5 = contentMd5;
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

   @Override
   public boolean isRequesterCharged() {
      return this.isRequesterCharged;
   }

   @Override
   public void setRequesterCharged(boolean isRequesterCharged) {
      this.isRequesterCharged = isRequesterCharged;
   }
}
