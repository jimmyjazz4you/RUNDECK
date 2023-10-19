package com.amazonaws.services.s3.model.intelligenttiering;

import java.util.List;

abstract class IntelligentTieringNAryOperator extends IntelligentTieringFilterPredicate {
   private final List<IntelligentTieringFilterPredicate> operands;

   protected IntelligentTieringNAryOperator(List<IntelligentTieringFilterPredicate> operands) {
      this.operands = operands;
   }

   public List<IntelligentTieringFilterPredicate> getOperands() {
      return this.operands;
   }
}
