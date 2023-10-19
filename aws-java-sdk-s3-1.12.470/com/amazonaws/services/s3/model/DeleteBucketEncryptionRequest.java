package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class DeleteBucketEncryptionRequest extends AmazonWebServiceRequest implements Serializable, Cloneable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public DeleteBucketEncryptionRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public DeleteBucketEncryptionRequest withBucketName(String bucket) {
      this.setBucketName(bucket);
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
      } else if (!(obj instanceof DeleteBucketEncryptionRequest)) {
         return false;
      } else {
         DeleteBucketEncryptionRequest other = (DeleteBucketEncryptionRequest)obj;
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

   public DeleteBucketEncryptionRequest clone() {
      return (DeleteBucketEncryptionRequest)super.clone();
   }
}
