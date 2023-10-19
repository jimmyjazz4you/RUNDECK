package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;
import java.io.Serializable;

public class SetBucketInventoryConfigurationRequest extends AmazonWebServiceRequest implements Serializable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private InventoryConfiguration inventoryConfiguration;
   private String expectedBucketOwner;

   public SetBucketInventoryConfigurationRequest() {
   }

   public SetBucketInventoryConfigurationRequest(String bucketName, InventoryConfiguration inventoryConfiguration) {
      this.bucketName = bucketName;
      this.inventoryConfiguration = inventoryConfiguration;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SetBucketInventoryConfigurationRequest withExpectedBucketOwner(String expectedBucketOwner) {
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

   public SetBucketInventoryConfigurationRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public InventoryConfiguration getInventoryConfiguration() {
      return this.inventoryConfiguration;
   }

   public void setInventoryConfiguration(InventoryConfiguration inventoryConfiguration) {
      this.inventoryConfiguration = inventoryConfiguration;
   }

   public SetBucketInventoryConfigurationRequest withInventoryConfiguration(InventoryConfiguration inventoryConfiguration) {
      this.setInventoryConfiguration(inventoryConfiguration);
      return this;
   }
}
