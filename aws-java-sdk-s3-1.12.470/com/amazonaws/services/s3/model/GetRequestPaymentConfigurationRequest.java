package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetRequestPaymentConfigurationRequest extends AmazonWebServiceRequest implements Serializable {
   private String bucketName;

   public GetRequestPaymentConfigurationRequest(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }
}
