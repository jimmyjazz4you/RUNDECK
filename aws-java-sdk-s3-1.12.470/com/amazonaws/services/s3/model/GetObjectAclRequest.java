package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class GetObjectAclRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private S3ObjectIdBuilder s3ObjectIdBuilder = new S3ObjectIdBuilder();
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   public GetObjectAclRequest(String bucketName, String key) {
      this(bucketName, key, null);
   }

   public GetObjectAclRequest(String bucketName, String key, String versionId) {
      this.setBucketName(bucketName);
      this.setKey(key);
      this.setVersionId(versionId);
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public GetObjectAclRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.s3ObjectIdBuilder.getBucket();
   }

   public void setBucketName(String bucketName) {
      this.s3ObjectIdBuilder.setBucket(bucketName);
   }

   public GetObjectAclRequest withBucket(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.s3ObjectIdBuilder.getKey();
   }

   public void setKey(String key) {
      this.s3ObjectIdBuilder.setKey(key);
   }

   public GetObjectAclRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getVersionId() {
      return this.s3ObjectIdBuilder.getVersionId();
   }

   public void setVersionId(String versionId) {
      this.s3ObjectIdBuilder.setVersionId(versionId);
   }

   public GetObjectAclRequest withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public GetObjectAclRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
