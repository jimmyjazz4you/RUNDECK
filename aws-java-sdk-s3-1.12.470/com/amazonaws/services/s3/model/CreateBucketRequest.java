package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.s3.model.ownership.ObjectOwnership;
import java.io.Serializable;

public class CreateBucketRequest extends AmazonWebServiceRequest implements Serializable, S3AccelerateUnsupported {
   private String bucketName;
   @Deprecated
   private String region;
   private CannedAccessControlList cannedAcl;
   private AccessControlList accessControlList;
   private boolean objectLockEnabled;
   private String objectOwnership;

   public CreateBucketRequest(String bucketName) {
      this(bucketName, Region.US_Standard);
   }

   public CreateBucketRequest(String bucketName, Region region) {
      this(bucketName, region.toString());
   }

   public CreateBucketRequest(String bucketName, String region) {
      this.setBucketName(bucketName);
      this.setRegion(region);
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   @Deprecated
   public void setRegion(String region) {
      this.region = region;
   }

   @Deprecated
   public String getRegion() {
      return this.region;
   }

   public CannedAccessControlList getCannedAcl() {
      return this.cannedAcl;
   }

   public void setCannedAcl(CannedAccessControlList cannedAcl) {
      this.cannedAcl = cannedAcl;
   }

   public CreateBucketRequest withCannedAcl(CannedAccessControlList cannedAcl) {
      this.setCannedAcl(cannedAcl);
      return this;
   }

   public AccessControlList getAccessControlList() {
      return this.accessControlList;
   }

   public void setAccessControlList(AccessControlList accessControlList) {
      this.accessControlList = accessControlList;
   }

   public CreateBucketRequest withAccessControlList(AccessControlList accessControlList) {
      this.setAccessControlList(accessControlList);
      return this;
   }

   public boolean getObjectLockEnabledForBucket() {
      return this.objectLockEnabled;
   }

   public CreateBucketRequest withObjectLockEnabledForBucket(boolean objectLockEnabled) {
      this.objectLockEnabled = objectLockEnabled;
      return this;
   }

   public void setObjectLockEnabledForBucket(boolean objectLockEnabled) {
      this.withObjectLockEnabledForBucket(objectLockEnabled);
   }

   public String getObjectOwnership() {
      return this.objectOwnership;
   }

   public CreateBucketRequest withObjectOwnership(String objectOwnership) {
      this.setObjectOwnership(objectOwnership);
      return this;
   }

   public CreateBucketRequest withObjectOwnership(ObjectOwnership objectOwnership) {
      this.setObjectOwnership(objectOwnership);
      return this;
   }

   public void setObjectOwnership(String objectOwnership) {
      this.objectOwnership = objectOwnership;
   }

   public void setObjectOwnership(ObjectOwnership objectOwnership) {
      this.setObjectOwnership(objectOwnership.toString());
   }
}
