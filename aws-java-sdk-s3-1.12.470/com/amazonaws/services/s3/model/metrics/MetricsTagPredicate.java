package com.amazonaws.services.s3.model.metrics;

import com.amazonaws.services.s3.model.Tag;

public final class MetricsTagPredicate extends MetricsFilterPredicate {
   private final Tag tag;

   public MetricsTagPredicate(Tag tag) {
      this.tag = tag;
   }

   public Tag getTag() {
      return this.tag;
   }

   @Override
   public void accept(MetricsPredicateVisitor metricsPredicateVisitor) {
      metricsPredicateVisitor.visit(this);
   }
}
