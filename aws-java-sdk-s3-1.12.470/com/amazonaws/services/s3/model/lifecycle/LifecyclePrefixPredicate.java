package com.amazonaws.services.s3.model.lifecycle;

public final class LifecyclePrefixPredicate extends LifecycleFilterPredicate {
   private final String prefix;

   public LifecyclePrefixPredicate(String prefix) {
      this.prefix = prefix;
   }

   public String getPrefix() {
      return this.prefix;
   }

   @Override
   public void accept(LifecyclePredicateVisitor lifecyclePredicateVisitor) {
      lifecyclePredicateVisitor.visit(this);
   }
}
