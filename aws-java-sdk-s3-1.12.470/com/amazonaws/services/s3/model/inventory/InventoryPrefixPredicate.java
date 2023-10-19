package com.amazonaws.services.s3.model.inventory;

public final class InventoryPrefixPredicate extends InventoryFilterPredicate {
   private final String prefix;

   public InventoryPrefixPredicate(String prefix) {
      this.prefix = prefix;
   }

   public String getPrefix() {
      return this.prefix;
   }

   @Override
   public void accept(InventoryPredicateVisitor inventoryPredicateVisitor) {
      inventoryPredicateVisitor.visit(this);
   }
}
