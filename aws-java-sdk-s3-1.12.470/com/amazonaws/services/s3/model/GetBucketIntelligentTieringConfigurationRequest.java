package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetBucketIntelligentTieringConfigurationRequest extends AmazonWebServiceRequest implements Serializable {
   private String bucketName;
   private String id;

   public GetBucketIntelligentTieringConfigurationRequest() {
   }

   public GetBucketIntelligentTieringConfigurationRequest(String bucketName, String id) {
      this.bucketName = bucketName;
      this.id = id;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public GetBucketIntelligentTieringConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public GetBucketIntelligentTieringConfigurationRequest withId(String id) {
      this.setId(id);
      return this;
   }
}
