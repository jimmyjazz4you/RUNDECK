package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.metrics.MetricsConfiguration;
import java.io.Serializable;
import java.util.List;

public class ListBucketMetricsConfigurationsResult implements Serializable {
   private List<MetricsConfiguration> metricsConfigurationList;
   private String continuationToken;
   private boolean isTruncated;
   private String nextContinuationToken;

   public List<MetricsConfiguration> getMetricsConfigurationList() {
      return this.metricsConfigurationList;
   }

   public void setMetricsConfigurationList(List<MetricsConfiguration> metricsConfigurationList) {
      this.metricsConfigurationList = metricsConfigurationList;
   }

   public ListBucketMetricsConfigurationsResult withMetricsConfigurationList(List<MetricsConfiguration> metricsConfigurationList) {
      this.setMetricsConfigurationList(metricsConfigurationList);
      return this;
   }

   public boolean isTruncated() {
      return this.isTruncated;
   }

   public void setTruncated(boolean isTruncated) {
      this.isTruncated = isTruncated;
   }

   public ListBucketMetricsConfigurationsResult withTruncated(boolean isTruncated) {
      this.setTruncated(isTruncated);
      return this;
   }

   public String getContinuationToken() {
      return this.continuationToken;
   }

   public void setContinuationToken(String continuationToken) {
      this.continuationToken = continuationToken;
   }

   public ListBucketMetricsConfigurationsResult withContinuationToken(String continuationToken) {
      this.setContinuationToken(continuationToken);
      return this;
   }

   public String getNextContinuationToken() {
      return this.nextContinuationToken;
   }

   public void setNextContinuationToken(String nextContinuationToken) {
      this.nextContinuationToken = nextContinuationToken;
   }

   public ListBucketMetricsConfigurationsResult withNextContinuationToken(String nextContinuationToken) {
      this.setNextContinuationToken(nextContinuationToken);
      return this;
   }
}
