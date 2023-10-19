package com.amazonaws.services.s3.model.lifecycle;

import java.util.List;

abstract class LifecycleNAryOperator extends LifecycleFilterPredicate {
   private final List<LifecycleFilterPredicate> operands;

   public LifecycleNAryOperator(List<LifecycleFilterPredicate> operands) {
      this.operands = operands;
   }

   public List<LifecycleFilterPredicate> getOperands() {
      return this.operands;
   }
}
