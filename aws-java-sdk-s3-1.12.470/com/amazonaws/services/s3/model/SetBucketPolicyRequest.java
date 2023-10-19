package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketPolicyRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String policyText;
   private Boolean confirmRemoveSelfBucketAccess;
   private String expectedBucketOwner;

   public SetBucketPolicyRequest() {
   }

   public SetBucketPolicyRequest(String bucketName, String policyText) {
      this.bucketName = bucketName;
      this.policyText = policyText;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketPolicyRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketPolicyRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getPolicyText() {
      return this.policyText;
   }

   public void setPolicyText(String policyText) {
      this.policyText = policyText;
   }

   public SetBucketPolicyRequest withPolicyText(String policyText) {
      this.setPolicyText(policyText);
      return this;
   }

   public Boolean getConfirmRemoveSelfBucketAccess() {
      return this.confirmRemoveSelfBucketAccess;
   }

   public void setConfirmRemoveSelfBucketAccess(Boolean confirmRemoveSelfBucketAccess) {
      this.confirmRemoveSelfBucketAccess = confirmRemoveSelfBucketAccess;
   }

   public SetBucketPolicyRequest withConfirmRemoveSelfBucketAccess(Boolean confirmRemoveSelfBucketAccess) {
      this.setConfirmRemoveSelfBucketAccess(confirmRemoveSelfBucketAccess);
      return this;
   }
}
