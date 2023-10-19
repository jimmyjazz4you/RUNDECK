package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetBucketInventoryConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String id;
   private String expectedBucketOwner;

   public GetBucketInventoryConfigurationRequest() {
   }

   public GetBucketInventoryConfigurationRequest(String bucketName, String id) {
      this.bucketName = bucketName;
      this.id = id;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetBucketInventoryConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public GetBucketInventoryConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public GetBucketInventoryConfigurationRequest withId(String id) {
      this.setId(id);
      return this;
   }
}
