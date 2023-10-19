package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class HeadBucketResult implements Serializable {
   private String bucketRegion;

   public String getBucketRegion() {
      return this.bucketRegion;
   }

   public void setBucketRegion(String bucketRegion) {
      this.bucketRegion = bucketRegion;
   }

   public HeadBucketResult withBucketRegion(String bucketRegion) {
      this.setBucketRegion(bucketRegion);
      return this;
   }
}
