package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;

public class GetBucketInventoryConfigurationResult {
   private InventoryConfiguration inventoryConfiguration;

   public InventoryConfiguration getInventoryConfiguration() {
      return this.inventoryConfiguration;
   }

   public void setInventoryConfiguration(InventoryConfiguration inventoryConfiguration) {
      this.inventoryConfiguration = inventoryConfiguration;
   }

   public GetBucketInventoryConfigurationResult withInventoryConfiguration(InventoryConfiguration inventoryConfiguration) {
      this.setInventoryConfiguration(inventoryConfiguration);
      return this;
   }
}
