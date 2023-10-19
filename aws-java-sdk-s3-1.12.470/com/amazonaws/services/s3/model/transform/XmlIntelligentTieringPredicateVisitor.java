package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringAndOperator;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringFilterPredicate;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringPredicateVisitor;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringPrefixPredicate;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringTagPredicate;

class XmlIntelligentTieringPredicateVisitor implements IntelligentTieringPredicateVisitor {
   private final XmlWriter xml;

   public XmlIntelligentTieringPredicateVisitor(XmlWriter xml) {
      this.xml = xml;
   }

   @Override
   public void visit(IntelligentTieringPrefixPredicate prefixPredicate) {
      BucketConfigurationXmlFactoryFunctions.writePrefix(this.xml, prefixPredicate.getPrefix());
   }

   @Override
   public void visit(IntelligentTieringTagPredicate tagPredicate) {
      BucketConfigurationXmlFactoryFunctions.writeTag(this.xml, tagPredicate.getTag());
   }

   @Override
   public void visit(IntelligentTieringAndOperator andOperator) {
      this.xml.start("And");

      for(IntelligentTieringFilterPredicate predicate : andOperator.getOperands()) {
         predicate.accept(this);
      }

      this.xml.end();
   }
}
