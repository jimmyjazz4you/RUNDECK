package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.s3.model.metrics.MetricsConfiguration;
import java.io.Serializable;

public class SetBucketMetricsConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private MetricsConfiguration metricsConfiguration;
   private String expectedBucketOwner;

   public SetBucketMetricsConfigurationRequest() {
   }

   public SetBucketMetricsConfigurationRequest(String bucketName, MetricsConfiguration metricsConfiguration) {
      this.bucketName = bucketName;
      this.metricsConfiguration = metricsConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketMetricsConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketMetricsConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public MetricsConfiguration getMetricsConfiguration() {
      return this.metricsConfiguration;
   }

   public void setMetricsConfiguration(MetricsConfiguration metricsConfiguration) {
      this.metricsConfiguration = metricsConfiguration;
   }

   public SetBucketMetricsConfigurationRequest withMetricsConfiguration(MetricsConfiguration metricsConfiguration) {
      this.setMetricsConfiguration(metricsConfiguration);
      return this;
   }
}
