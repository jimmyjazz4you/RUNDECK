package com.amazonaws.services.s3.model.inventory;

import java.io.Serializable;

public class InventoryFilter implements Serializable {
   private InventoryFilterPredicate predicate;

   public InventoryFilter() {
   }

   public InventoryFilter(InventoryFilterPredicate predicate) {
      this.predicate = predicate;
   }

   public InventoryFilterPredicate getPredicate() {
      return this.predicate;
   }

   public void setPredicate(InventoryFilterPredicate predicate) {
      this.predicate = predicate;
   }

   public InventoryFilter withPredicate(InventoryFilterPredicate predicate) {
      this.setPredicate(predicate);
      return this;
   }
}
