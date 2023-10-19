package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class HeadBucketRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public HeadBucketRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public HeadBucketRequest(String bucketName) {
      this.bucketName = bucketName;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof HeadBucketRequest)) {
         return false;
      } else {
         HeadBucketRequest other = (HeadBucketRequest)obj;
         if (other.getBucketName() == null ^ this.getBucketName() == null) {
            return false;
         } else {
            return other.getBucketName() == null || other.getBucketName().equals(this.getBucketName());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getBucketName() == null ? 0 : this.getBucketName().hashCode());
   }
}
