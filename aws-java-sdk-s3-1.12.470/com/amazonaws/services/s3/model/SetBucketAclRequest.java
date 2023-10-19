package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SetBucketAclRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private AccessControlList acl;
   private CannedAccessControlList cannedAcl;
   private String expectedBucketOwner;

   public SetBucketAclRequest(String bucketName, AccessControlList acl) {
      this.bucketName = bucketName;
      this.acl = acl;
      this.cannedAcl = null;
   }

   public SetBucketAclRequest(String bucketName, CannedAccessControlList acl) {
      this.bucketName = bucketName;
      this.acl = null;
      this.cannedAcl = acl;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public AccessControlList getAcl() {
      return this.acl;
   }

   public CannedAccessControlList getCannedAcl() {
      return this.cannedAcl;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketAclRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }
}
