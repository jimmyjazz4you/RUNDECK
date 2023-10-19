package com.amazonaws.services.s3.model.intelligenttiering;

import java.util.List;

public class IntelligentTieringAndOperator extends IntelligentTieringNAryOperator {
   public IntelligentTieringAndOperator(List<IntelligentTieringFilterPredicate> operands) {
      super(operands);
   }

   @Override
   public void accept(IntelligentTieringPredicateVisitor intelligentTieringPredicateVisitor) {
      intelligentTieringPredicateVisitor.visit(this);
   }
}
