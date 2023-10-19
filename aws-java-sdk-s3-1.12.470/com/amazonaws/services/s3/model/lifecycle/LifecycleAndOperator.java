package com.amazonaws.services.s3.model.lifecycle;

import java.util.List;

public final class LifecycleAndOperator extends LifecycleNAryOperator {
   public LifecycleAndOperator(List<LifecycleFilterPredicate> operands) {
      super(operands);
   }

   @Override
   public void accept(LifecyclePredicateVisitor lifecyclePredicateVisitor) {
      lifecyclePredicateVisitor.visit(this);
   }
}
