package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.s3.model.analytics.AnalyticsConfiguration;
import java.io.Serializable;

public class SetBucketAnalyticsConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private AnalyticsConfiguration analyticsConfiguration;
   private String expectedBucketOwner;

   public SetBucketAnalyticsConfigurationRequest() {
   }

   public SetBucketAnalyticsConfigurationRequest(String bucketName, AnalyticsConfiguration analyticsConfiguration) {
      this.bucketName = bucketName;
      this.analyticsConfiguration = analyticsConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketAnalyticsConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketAnalyticsConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public AnalyticsConfiguration getAnalyticsConfiguration() {
      return this.analyticsConfiguration;
   }

   public void setAnalyticsConfiguration(AnalyticsConfiguration analyticsConfiguration) {
      this.analyticsConfiguration = analyticsConfiguration;
   }

   public SetBucketAnalyticsConfigurationRequest withAnalyticsConfiguration(AnalyticsConfiguration analyticsConfiguration) {
      this.setAnalyticsConfiguration(analyticsConfiguration);
      return this;
   }
}
