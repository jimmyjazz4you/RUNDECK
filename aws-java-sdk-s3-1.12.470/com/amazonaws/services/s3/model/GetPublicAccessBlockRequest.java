package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetPublicAccessBlockRequest extends AmazonWebServiceRequest implements Serializable, Cloneable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetPublicAccessBlockRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public GetPublicAccessBlockRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         GetPublicAccessBlockRequest that = (GetPublicAccessBlockRequest)o;
         return this.bucketName != null ? this.bucketName.equals(that.bucketName) : that.bucketName == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.bucketName != null ? this.bucketName.hashCode() : 0;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getBucketName() != null) {
         sb.append("BucketName: ").append(this.getBucketName()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   public GetPublicAccessBlockRequest clone() {
      return (GetPublicAccessBlockRequest)super.clone();
   }
}
