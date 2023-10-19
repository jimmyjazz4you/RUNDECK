package com.amazonaws.services.s3.model.inventory;

public enum InventoryFrequency {
   Daily("Daily"),
   Weekly("Weekly");

   private final String frequency;

   private InventoryFrequency(String frequency) {
      this.frequency = frequency;
   }

   @Override
   public String toString() {
      return this.frequency;
   }
}
