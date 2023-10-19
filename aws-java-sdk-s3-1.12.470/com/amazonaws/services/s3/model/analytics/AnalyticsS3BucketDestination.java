package com.amazonaws.services.s3.model.analytics;

import java.io.Serializable;

public class AnalyticsS3BucketDestination implements Serializable {
   private String format;
   private String bucketAccountId;
   private String bucketArn;
   private String prefix;

   public void setFormat(AnalyticsS3ExportFileFormat format) {
      if (format == null) {
         this.setFormat((String)null);
      } else {
         this.setFormat(format.toString());
      }
   }

   public AnalyticsS3BucketDestination withFormat(AnalyticsS3ExportFileFormat format) {
      this.setFormat(format);
      return this;
   }

   public String getFormat() {
      return this.format;
   }

   public void setFormat(String format) {
      this.format = format;
   }

   public AnalyticsS3BucketDestination withFormat(String format) {
      this.setFormat(format);
      return this;
   }

   public String getBucketAccountId() {
      return this.bucketAccountId;
   }

   public void setBucketAccountId(String bucketAccountId) {
      this.bucketAccountId = bucketAccountId;
   }

   public AnalyticsS3BucketDestination withBucketAccountId(String bucketAccountId) {
      this.setBucketAccountId(bucketAccountId);
      return this;
   }

   public String getBucketArn() {
      return this.bucketArn;
   }

   public void setBucketArn(String bucketArn) {
      this.bucketArn = bucketArn;
   }

   public AnalyticsS3BucketDestination withBucketArn(String bucketArn) {
      this.setBucketArn(bucketArn);
      return this;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public AnalyticsS3BucketDestination withPrefix(String prefix) {
      this.setPrefix(prefix);
      return this;
   }
}
