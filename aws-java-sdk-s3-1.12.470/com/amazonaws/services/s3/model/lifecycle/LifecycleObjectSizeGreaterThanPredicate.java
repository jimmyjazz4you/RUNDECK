package com.amazonaws.services.s3.model.lifecycle;

public final class LifecycleObjectSizeGreaterThanPredicate extends LifecycleFilterPredicate {
   private final Long objectSizeGreaterThan;

   public LifecycleObjectSizeGreaterThanPredicate(Long objectSizeGreaterThan) {
      this.objectSizeGreaterThan = objectSizeGreaterThan;
   }

   public Long getObjectSizeGreaterThan() {
      return this.objectSizeGreaterThan;
   }

   @Override
   public void accept(LifecyclePredicateVisitor lifecyclePredicateVisitor) {
      lifecyclePredicateVisitor.visit(this);
   }
}
