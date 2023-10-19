package com.amazonaws.services.s3.model.analytics;

import java.io.Serializable;

public class AnalyticsExportDestination implements Serializable {
   private AnalyticsS3BucketDestination s3BucketDestination;

   public AnalyticsS3BucketDestination getS3BucketDestination() {
      return this.s3BucketDestination;
   }

   public void setS3BucketDestination(AnalyticsS3BucketDestination s3BucketDestination) {
      this.s3BucketDestination = s3BucketDestination;
   }

   public AnalyticsExportDestination withS3BucketDestination(AnalyticsS3BucketDestination s3BucketDestination) {
      this.setS3BucketDestination(s3BucketDestination);
      return this;
   }
}
