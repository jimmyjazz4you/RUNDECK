package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class Filter implements Serializable {
   private S3KeyFilter s3KeyFilter;

   public S3KeyFilter getS3KeyFilter() {
      return this.s3KeyFilter;
   }

   public void setS3KeyFilter(S3KeyFilter s3KeyFilter) {
      this.s3KeyFilter = s3KeyFilter;
   }

   public Filter withS3KeyFilter(S3KeyFilter s3KeyFilter) {
      this.setS3KeyFilter(s3KeyFilter);
      return this;
   }
}
