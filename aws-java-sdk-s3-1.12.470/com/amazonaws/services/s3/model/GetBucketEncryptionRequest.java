package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetBucketEncryptionRequest extends AmazonWebServiceRequest implements Serializable, Cloneable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetBucketEncryptionRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public GetBucketEncryptionRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
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

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof GetBucketEncryptionRequest)) {
         return false;
      } else {
         GetBucketEncryptionRequest other = (GetBucketEncryptionRequest)obj;
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

   public GetBucketEncryptionRequest clone() {
      return (GetBucketEncryptionRequest)super.clone();
   }
}
