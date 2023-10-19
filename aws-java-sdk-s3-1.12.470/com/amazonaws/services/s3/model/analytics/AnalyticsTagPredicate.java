package com.amazonaws.services.s3.model.analytics;

import com.amazonaws.services.s3.model.Tag;

public final class AnalyticsTagPredicate extends AnalyticsFilterPredicate {
   private final Tag tag;

   public AnalyticsTagPredicate(Tag tag) {
      this.tag = tag;
   }

   public Tag getTag() {
      return this.tag;
   }

   @Override
   public void accept(AnalyticsPredicateVisitor analyticsPredicateVisitor) {
      analyticsPredicateVisitor.visit(this);
   }
}
