package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;
import java.io.Serializable;
import java.util.List;

public class ListBucketInventoryConfigurationsResult implements Serializable {
   private List<InventoryConfiguration> inventoryConfigurationList;
   private String continuationToken;
   private boolean isTruncated;
   private String nextContinuationToken;

   public List<InventoryConfiguration> getInventoryConfigurationList() {
      return this.inventoryConfigurationList;
   }

   public void setInventoryConfigurationList(List<InventoryConfiguration> inventoryConfigurationList) {
      this.inventoryConfigurationList = inventoryConfigurationList;
   }

   public ListBucketInventoryConfigurationsResult withInventoryConfigurationList(List<InventoryConfiguration> inventoryConfigurationList) {
      this.setInventoryConfigurationList(inventoryConfigurationList);
      return this;
   }

   public boolean isTruncated() {
      return this.isTruncated;
   }

   public void setTruncated(boolean isTruncated) {
      this.isTruncated = isTruncated;
   }

   public ListBucketInventoryConfigurationsResult withTruncated(boolean isTruncated) {
      this.setTruncated(isTruncated);
      return this;
   }

   public String getContinuationToken() {
      return this.continuationToken;
   }

   public void setContinuationToken(String continuationToken) {
      this.continuationToken = continuationToken;
   }

   public ListBucketInventoryConfigurationsResult withContinuationToken(String continuationToken) {
      this.setContinuationToken(continuationToken);
      return this;
   }

   public String getNextContinuationToken() {
      return this.nextContinuationToken;
   }

   public void setNextContinuationToken(String nextContinuationToken) {
      this.nextContinuationToken = nextContinuationToken;
   }

   public ListBucketInventoryConfigurationsResult withNextContinuationToken(String nextContinuationToken) {
      this.setNextContinuationToken(nextContinuationToken);
      return this;
   }
}
