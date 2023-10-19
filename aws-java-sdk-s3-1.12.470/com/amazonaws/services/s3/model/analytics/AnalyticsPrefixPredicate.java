package com.amazonaws.services.s3.model.analytics;

public final class AnalyticsPrefixPredicate extends AnalyticsFilterPredicate {
   private final String prefix;

   public AnalyticsPrefixPredicate(String prefix) {
      this.prefix = prefix;
   }

   public String getPrefix() {
      return this.prefix;
   }

   @Override
   public void accept(AnalyticsPredicateVisitor analyticsPredicateVisitor) {
      analyticsPredicateVisitor.visit(this);
   }
}
