package com.amazonaws.services.s3.model.metrics;

import java.util.List;

public final class MetricsAndOperator extends MetricsNAryOperator {
   public MetricsAndOperator(List<MetricsFilterPredicate> operands) {
      super(operands);
   }

   @Override
   public void accept(MetricsPredicateVisitor metricsPredicateVisitor) {
      metricsPredicateVisitor.visit(this);
   }
}
