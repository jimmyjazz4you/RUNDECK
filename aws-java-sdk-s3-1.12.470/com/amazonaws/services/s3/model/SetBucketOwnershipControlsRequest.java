package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.s3.model.ownership.OwnershipControls;
import java.io.Serializable;

public class SetBucketOwnershipControlsRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String expectedBucketOwner;
   private OwnershipControls OwnershipControls;

   public SetBucketOwnershipControlsRequest() {
   }

   public SetBucketOwnershipControlsRequest(String bucketName, OwnershipControls ownershipControls) {
      this.bucketName = bucketName;
      this.OwnershipControls = ownershipControls;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public SetBucketOwnershipControlsRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public OwnershipControls getOwnershipControls() {
      return this.OwnershipControls;
   }

   public void setOwnershipControls(OwnershipControls OwnershipControls) {
      this.OwnershipControls = OwnershipControls;
   }

   public SetBucketOwnershipControlsRequest withOwnershipControls(OwnershipControls OwnershipControls) {
      this.setOwnershipControls(OwnershipControls);
      return this;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketOwnershipControlsRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
