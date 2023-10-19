package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketEncryptionRequest extends AmazonWebServiceRequest implements Serializable, Cloneable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private ServerSideEncryptionConfiguration serverSideEncryptionConfiguration;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketEncryptionRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketEncryptionRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public ServerSideEncryptionConfiguration getServerSideEncryptionConfiguration() {
      return this.serverSideEncryptionConfiguration;
   }

   public void setServerSideEncryptionConfiguration(ServerSideEncryptionConfiguration serverSideEncryptionConfiguration) {
      this.serverSideEncryptionConfiguration = serverSideEncryptionConfiguration;
   }

   public SetBucketEncryptionRequest withServerSideEncryptionConfiguration(ServerSideEncryptionConfiguration serverSideEncryptionConfiguration) {
      this.setServerSideEncryptionConfiguration(serverSideEncryptionConfiguration);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getBucketName() != null) {
         sb.append("BucketName: ").append(this.getBucketName()).append(",");
      }

      if (this.getServerSideEncryptionConfiguration() != null) {
         sb.append("ServerSideEncryptionConfiguration: ").append(this.getServerSideEncryptionConfiguration()).append(",");
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
      } else if (!(obj instanceof SetBucketEncryptionRequest)) {
         return false;
      } else {
         SetBucketEncryptionRequest other = (SetBucketEncryptionRequest)obj;
         if (other.getBucketName() == null ^ this.getBucketName() == null) {
            return false;
         } else if (other.getBucketName() != null && !other.getBucketName().equals(this.getBucketName())) {
            return false;
         } else if (other.getServerSideEncryptionConfiguration() == null ^ this.getServerSideEncryptionConfiguration() == null) {
            return false;
         } else {
            return other.getServerSideEncryptionConfiguration() == null
               || other.getServerSideEncryptionConfiguration().equals(this.getServerSideEncryptionConfiguration());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getBucketName() == null ? 0 : this.getBucketName().hashCode());
      return 31 * hashCode + (this.getServerSideEncryptionConfiguration() == null ? 0 : this.getServerSideEncryptionConfiguration().hashCode());
   }

   public SetBucketEncryptionRequest clone() {
      return (SetBucketEncryptionRequest)super.clone();
   }
}
