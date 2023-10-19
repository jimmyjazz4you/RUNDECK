package com.amazonaws.services.s3.model.metrics;

public final class MetricsPrefixPredicate extends MetricsFilterPredicate {
   private final String prefix;

   public MetricsPrefixPredicate(String prefix) {
      this.prefix = prefix;
   }

   public String getPrefix() {
      return this.prefix;
   }

   @Override
   public void accept(MetricsPredicateVisitor metricsPredicateVisitor) {
      metricsPredicateVisitor.visit(this);
   }
}
