package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import com.amazonaws.services.s3.internal.SSEResultBase;
import java.io.Serializable;
import java.util.Date;

public class InitiateMultipartUploadResult extends SSEResultBase implements S3RequesterChargedResult, Serializable {
   private String bucketName;
   private String key;
   private String uploadId;
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
