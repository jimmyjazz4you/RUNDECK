package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.analytics.AnalyticsConfiguration;
import java.io.Serializable;
import java.util.List;

public class ListBucketAnalyticsConfigurationsResult implements Serializable {
   private List<AnalyticsConfiguration> analyticsConfigurationList;
   private String continuationToken;
   private boolean isTruncated;
   private String nextContinuationToken;

   public List<AnalyticsConfiguration> getAnalyticsConfigurationList() {
      return this.analyticsConfigurationList;
   }

   public void setAnalyticsConfigurationList(List<AnalyticsConfiguration> analyticsConfigurationList) {
      this.analyticsConfigurationList = analyticsConfigurationList;
   }

   public ListBucketAnalyticsConfigurationsResult withAnalyticsConfigurationList(List<AnalyticsConfiguration> analyticsConfigurationList) {
      this.setAnalyticsConfigurationList(analyticsConfigurationList);
      return this;
   }

   public boolean isTruncated() {
      return this.isTruncated;
   }

   public void setTruncated(boolean isTruncated) {
      this.isTruncated = isTruncated;
   }

   public ListBucketAnalyticsConfigurationsResult withTruncated(boolean isTruncated) {
      this.setTruncated(isTruncated);
      return this;
   }

   public String getContinuationToken() {
      return this.continuationToken;
   }

   public void setContinuationToken(String continuationToken) {
      this.continuationToken = continuationToken;
   }

   public ListBucketAnalyticsConfigurationsResult withContinuationToken(String continuationToken) {
      this.setContinuationToken(continuationToken);
      return this;
   }

   public String getNextContinuationToken() {
      return this.nextContinuationToken;
   }

   public void setNextContinuationToken(String nextContinuationToken) {
      this.nextContinuationToken = nextContinuationToken;
   }

   public ListBucketAnalyticsConfigurationsResult withNextContinuationToken(String nextContinuationToken) {
      this.setNextContinuationToken(nextContinuationToken);
      return this;
   }
}
