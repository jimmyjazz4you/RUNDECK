package com.amazonaws.services.s3.model.inventory;

public enum InventoryIncludedObjectVersions {
   All("All"),
   Current("Current");

   private final String name;

   private InventoryIncludedObjectVersions(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return this.name;
   }
}
