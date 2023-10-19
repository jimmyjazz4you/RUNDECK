package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetRequestPaymentConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private RequestPaymentConfiguration configuration;
   private String expectedBucketOwner;

   public SetRequestPaymentConfigurationRequest(String bucketName, RequestPaymentConfiguration configuration) {
      this.setBucketName(bucketName);
      this.configuration = configuration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetRequestPaymentConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public RequestPaymentConfiguration getConfiguration() {
      return this.configuration;
   }

   public void setConfiguration(RequestPaymentConfiguration configuration) {
      this.configuration = configuration;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }
}
