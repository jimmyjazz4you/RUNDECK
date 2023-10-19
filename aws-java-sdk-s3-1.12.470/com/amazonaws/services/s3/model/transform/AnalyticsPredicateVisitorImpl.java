package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.analytics.AnalyticsAndOperator;
import com.amazonaws.services.s3.model.analytics.AnalyticsFilterPredicate;
import com.amazonaws.services.s3.model.analytics.AnalyticsPredicateVisitor;
import com.amazonaws.services.s3.model.analytics.AnalyticsPrefixPredicate;
import com.amazonaws.services.s3.model.analytics.AnalyticsTagPredicate;

class AnalyticsPredicateVisitorImpl implements AnalyticsPredicateVisitor {
   private final XmlWriter xml;

   public AnalyticsPredicateVisitorImpl(XmlWriter xml) {
      this.xml = xml;
   }

   @Override
   public void visit(AnalyticsPrefixPredicate analyticsPrefixPredicate) {
      BucketConfigurationXmlFactoryFunctions.writePrefix(this.xml, analyticsPrefixPredicate.getPrefix());
   }

   @Override
   public void visit(AnalyticsTagPredicate analyticsTagPredicate) {
      BucketConfigurationXmlFactoryFunctions.writeTag(this.xml, analyticsTagPredicate.getTag());
   }

   @Override
   public void visit(AnalyticsAndOperator analyticsAndOperator) {
      this.xml.start("And");

      for(AnalyticsFilterPredicate predicate : analyticsAndOperator.getOperands()) {
         predicate.accept(this);
      }

      this.xml.end();
   }
}
