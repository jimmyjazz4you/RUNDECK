package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import java.io.Serializable;

public class SetBucketIntelligentTieringConfigurationRequest extends AmazonWebServiceRequest implements Serializable {
   private String bucketName;
   private IntelligentTieringConfiguration intelligentTieringConfiguration;

   public SetBucketIntelligentTieringConfigurationRequest() {
   }

   public SetBucketIntelligentTieringConfigurationRequest(String bucketName, IntelligentTieringConfiguration intelligentTieringConfiguration) {
      this.bucketName = bucketName;
      this.intelligentTieringConfiguration = intelligentTieringConfiguration;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public SetBucketIntelligentTieringConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public IntelligentTieringConfiguration getIntelligentTierinConfiguration() {
      return this.intelligentTieringConfiguration;
   }

   public void setIntelligentTierinConfiguration(IntelligentTieringConfiguration intelligentTieringConfiguration) {
      this.intelligentTieringConfiguration = intelligentTieringConfiguration;
   }

   public SetBucketIntelligentTieringConfigurationRequest withIntelligentTieringConfiguration(IntelligentTieringConfiguration intelligentTieringConfiguration) {
      this.setIntelligentTierinConfiguration(intelligentTieringConfiguration);
      return this;
   }
}
