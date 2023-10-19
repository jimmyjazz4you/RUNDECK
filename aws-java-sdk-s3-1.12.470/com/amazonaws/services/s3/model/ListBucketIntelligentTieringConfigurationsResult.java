package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import java.io.Serializable;
import java.util.List;

public class ListBucketIntelligentTieringConfigurationsResult implements Serializable {
   private List<IntelligentTieringConfiguration> intelligentTieringConfigurationList;
   private String continuationToken;
   private boolean isTruncated;
   private String nextContinuationToken;

   public List<IntelligentTieringConfiguration> getIntelligentTieringConfigurationList() {
      return this.intelligentTieringConfigurationList;
   }

   public void setIntelligentTieringConfigurationList(List<IntelligentTieringConfiguration> intelligentTieringConfigurationList) {
      this.intelligentTieringConfigurationList = intelligentTieringConfigurationList;
   }

   public ListBucketIntelligentTieringConfigurationsResult withIntelligentTieringConfigurationList(
      List<IntelligentTieringConfiguration> intelligentTieringConfigurationList
   ) {
      this.setIntelligentTieringConfigurationList(intelligentTieringConfigurationList);
      return this;
   }

   public boolean isTruncated() {
      return this.isTruncated;
   }

   public void setTruncated(boolean isTruncated) {
      this.isTruncated = isTruncated;
   }

   public ListBucketIntelligentTieringConfigurationsResult withTruncated(boolean isTruncated) {
      this.setTruncated(isTruncated);
      return this;
   }

   public String getContinuationToken() {
      return this.continuationToken;
   }

   public void setContinuationToken(String continuationToken) {
      this.continuationToken = continuationToken;
   }

   public ListBucketIntelligentTieringConfigurationsResult withContinuationToken(String continuationToken) {
      this.setContinuationToken(continuationToken);
      return this;
   }

   public String getNextContinuationToken() {
      return this.nextContinuationToken;
   }

   public void setNextContinuationToken(String nextContinuationToken) {
      this.nextContinuationToken = nextContinuationToken;
   }

   public ListBucketIntelligentTieringConfigurationsResult withNextContinuationToken(String nextContinuationToken) {
      this.setNextContinuationToken(nextContinuationToken);
      return this;
   }
}
