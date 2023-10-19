package com.amazonaws.services.s3.model.metrics;

public final class MetricsAccessPointArnPredicate extends MetricsFilterPredicate {
   private final String accessPointArn;

   public MetricsAccessPointArnPredicate(String accessPointArn) {
      this.accessPointArn = accessPointArn;
   }

   public String getAccessPointArn() {
      return this.accessPointArn;
   }

   @Override
   public void accept(MetricsPredicateVisitor metricsPredicateVisitor) {
      metricsPredicateVisitor.visit(this);
   }
}
