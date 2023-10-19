package com.amazonaws.services.s3.model.metrics;

public interface MetricsPredicateVisitor {
   void visit(MetricsPrefixPredicate var1);

   void visit(MetricsTagPredicate var1);

   void visit(MetricsAndOperator var1);

   void visit(MetricsAccessPointArnPredicate var1);
}
