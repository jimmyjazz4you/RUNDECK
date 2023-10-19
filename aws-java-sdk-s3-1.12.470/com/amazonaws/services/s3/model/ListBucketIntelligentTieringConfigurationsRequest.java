package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class ListBucketIntelligentTieringConfigurationsRequest extends AmazonWebServiceRequest implements Serializable {
   private String bucketName;
   private String continuationToken;

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public ListBucketIntelligentTieringConfigurationsRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getContinuationToken() {
      return this.continuationToken;
   }

   public void setContinuationToken(String continuationToken) {
      this.continuationToken = continuationToken;
   }

   public ListBucketIntelligentTieringConfigurationsRequest withContinuationToken(String continuationToken) {
      this.setContinuationToken(continuationToken);
      return this;
   }
}
