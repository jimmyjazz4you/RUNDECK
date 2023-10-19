package com.amazonaws.services.s3.model.lifecycle;

public final class LifecycleObjectSizeLessThanPredicate extends LifecycleFilterPredicate {
   private final Long objectSizeLessThan;

   public LifecycleObjectSizeLessThanPredicate(Long objectSizeLessThan) {
      this.objectSizeLessThan = objectSizeLessThan;
   }

   public Long getObjectSizeLessThan() {
      return this.objectSizeLessThan;
   }

   @Override
   public void accept(LifecyclePredicateVisitor lifecyclePredicateVisitor) {
      lifecyclePredicateVisitor.visit(this);
   }
}
