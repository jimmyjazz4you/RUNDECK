package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.ObjectExpirationResult;
import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import com.amazonaws.services.s3.internal.S3VersionResult;
import com.amazonaws.services.s3.internal.SSEResultBase;
import java.io.Serializable;
import java.util.Date;

public class CopyObjectResult extends SSEResultBase implements ObjectExpirationResult, S3RequesterChargedResult, S3VersionResult, Serializable {
   private String etag;
   private Date lastModifiedDate;
   private String versionId;
   private Date expirationTime;
   private String expirationTimeRuleId;
   private boolean isRequesterCharged;

   public String getETag() {
      return this.etag;
   }

   public void setETag(String etag) {
      this.etag = etag;
   }

   public Date getLastModifiedDate() {
      return this.lastModifiedDate;
   }

   public void setLastModifiedDate(Date lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
   }

   @Override
   public String getVersionId() {
      return this.versionId;
   }

   @Override
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
