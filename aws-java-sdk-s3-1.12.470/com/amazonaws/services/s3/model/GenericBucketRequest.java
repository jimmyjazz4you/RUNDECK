package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GenericBucketRequest extends AmazonWebServiceRequest implements Serializable {
   private String bucketName;

   public GenericBucketRequest(String bucketName) {
      this.bucketName = bucketName;
   }

   @Deprecated
   public String getBucket() {
      return this.bucketName;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public GenericBucketRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }
}
