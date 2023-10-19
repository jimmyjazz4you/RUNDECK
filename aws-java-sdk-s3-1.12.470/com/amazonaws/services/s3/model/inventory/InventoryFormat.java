package com.amazonaws.services.s3.model.inventory;

public enum InventoryFormat {
   CSV("CSV"),
   ORC("ORC"),
   Parquet("Parquet");

   private final String format;

   private InventoryFormat(String format) {
      this.format = format;
   }

   @Override
   public String toString() {
      return this.format;
   }
}
