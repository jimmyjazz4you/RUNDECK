package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.util.json.Jackson;
import java.io.Serializable;

public class SetBucketReplicationConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private BucketReplicationConfiguration replicationConfiguration;
   private String token;
   private String expectedBucketOwner;

   public SetBucketReplicationConfigurationRequest() {
   }

   public SetBucketReplicationConfigurationRequest(String bucketName, BucketReplicationConfiguration replicationConfiguration) {
      this.bucketName = bucketName;
      this.replicationConfiguration = replicationConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketReplicationConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketReplicationConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public BucketReplicationConfiguration getReplicationConfiguration() {
      return this.replicationConfiguration;
   }

   public void setReplicationConfiguration(BucketReplicationConfiguration replicationConfiguration) {
      this.replicationConfiguration = replicationConfiguration;
   }

   public SetBucketReplicationConfigurationRequest withReplicationConfiguration(BucketReplicationConfiguration replicationConfiguration) {
      this.setReplicationConfiguration(replicationConfiguration);
      return this;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public SetBucketReplicationConfigurationRequest withToken(String token) {
      this.setToken(token);
      return this;
   }

   public String getToken() {
      return this.token;
   }

   @Override
   public String toString() {
      return Jackson.toJsonString(this);
   }
}
