package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketVersioningConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private BucketVersioningConfiguration versioningConfiguration;
   private MultiFactorAuthentication mfa;
   private String expectedBucketOwner;

   public SetBucketVersioningConfigurationRequest(String bucketName, BucketVersioningConfiguration configuration) {
      this.bucketName = bucketName;
      this.versioningConfiguration = configuration;
   }

   public SetBucketVersioningConfigurationRequest(String bucketName, BucketVersioningConfiguration configuration, MultiFactorAuthentication mfa) {
      this(bucketName, configuration);
      this.mfa = mfa;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketVersioningConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketVersioningConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public BucketVersioningConfiguration getVersioningConfiguration() {
      return this.versioningConfiguration;
   }

   public void setVersioningConfiguration(BucketVersioningConfiguration versioningConfiguration) {
      this.versioningConfiguration = versioningConfiguration;
   }

   public SetBucketVersioningConfigurationRequest withVersioningConfiguration(BucketVersioningConfiguration versioningConfiguration) {
      this.setVersioningConfiguration(versioningConfiguration);
      return this;
   }

   public MultiFactorAuthentication getMfa() {
      return this.mfa;
   }

   public void setMfa(MultiFactorAuthentication mfa) {
      this.mfa = mfa;
   }

   public SetBucketVersioningConfigurationRequest withMfa(MultiFactorAuthentication mfa) {
      this.setMfa(mfa);
      return this;
   }
}
