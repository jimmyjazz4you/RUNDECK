package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.metrics.MetricsAccessPointArnPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsAndOperator;
import com.amazonaws.services.s3.model.metrics.MetricsFilterPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsPredicateVisitor;
import com.amazonaws.services.s3.model.metrics.MetricsPrefixPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsTagPredicate;

class MetricsPredicateVisitorImpl implements MetricsPredicateVisitor {
   private final XmlWriter xml;

   public MetricsPredicateVisitorImpl(XmlWriter xml) {
      this.xml = xml;
   }

   @Override
   public void visit(MetricsPrefixPredicate metricsPrefixPredicate) {
      BucketConfigurationXmlFactoryFunctions.writePrefix(this.xml, metricsPrefixPredicate.getPrefix());
   }

   @Override
   public void visit(MetricsTagPredicate metricsTagPredicate) {
      BucketConfigurationXmlFactoryFunctions.writeTag(this.xml, metricsTagPredicate.getTag());
   }

   @Override
   public void visit(MetricsAndOperator metricsAndOperator) {
      this.xml.start("And");

      for(MetricsFilterPredicate predicate : metricsAndOperator.getOperands()) {
         predicate.accept(this);
      }

      this.xml.end();
   }

   @Override
   public void visit(MetricsAccessPointArnPredicate metricsAccessPointArnPredicate) {
      BucketConfigurationXmlFactory.writeAccessPointArn(this.xml, metricsAccessPointArnPredicate.getAccessPointArn());
   }
}
