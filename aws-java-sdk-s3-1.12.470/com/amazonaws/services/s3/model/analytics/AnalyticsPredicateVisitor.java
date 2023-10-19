package com.amazonaws.services.s3.model.analytics;

public interface AnalyticsPredicateVisitor {
   void visit(AnalyticsPrefixPredicate var1);

   void visit(AnalyticsTagPredicate var1);

   void visit(AnalyticsAndOperator var1);
}
