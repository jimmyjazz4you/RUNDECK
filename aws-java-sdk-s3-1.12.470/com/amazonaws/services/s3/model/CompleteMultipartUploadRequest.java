package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompleteMultipartUploadRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private String uploadId;
   private List<PartETag> partETags = new ArrayList<>();
   private boolean isRequesterPays;
   private String expectedBucketOwner;

   public CompleteMultipartUploadRequest() {
   }

   public CompleteMultipartUploadRequest(String bucketName, String key, String uploadId, List<PartETag> partETags) {
      this.bucketName = bucketName;
      this.key = key;
      this.uploadId = uploadId;
      this.partETags = partETags;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public CompleteMultipartUploadRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public CompleteMultipartUploadRequest withBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public CompleteMultipartUploadRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public String getUploadId() {
      return this.uploadId;
   }

   public void setUploadId(String uploadId) {
      this.uploadId = uploadId;
   }

   public CompleteMultipartUploadRequest withUploadId(String uploadId) {
      this.uploadId = uploadId;
      return this;
   }

   public List<PartETag> getPartETags() {
      return this.partETags;
   }

   public void setPartETags(List<PartETag> partETags) {
      this.partETags = partETags;
   }

   public CompleteMultipartUploadRequest withPartETags(List<PartETag> partETags) {
      this.setPartETags(partETags);
      return this;
   }

   public CompleteMultipartUploadRequest withPartETags(UploadPartResult... uploadPartResults) {
      for(UploadPartResult result : uploadPartResults) {
         this.partETags.add(new PartETag(result.getPartNumber(), result.getETag()));
      }

      return this;
   }

   public CompleteMultipartUploadRequest withPartETags(Collection<UploadPartResult> uploadPartResultsCollection) {
      for(UploadPartResult result : uploadPartResultsCollection) {
         this.partETags.add(new PartETag(result.getPartNumber(), result.getETag()));
      }

      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public CompleteMultipartUploadRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
