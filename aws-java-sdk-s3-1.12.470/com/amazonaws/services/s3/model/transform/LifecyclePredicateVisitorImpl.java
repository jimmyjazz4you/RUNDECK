package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleAndOperator;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilterPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleObjectSizeGreaterThanPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleObjectSizeLessThanPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecyclePredicateVisitor;
import com.amazonaws.services.s3.model.lifecycle.LifecyclePrefixPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;

class LifecyclePredicateVisitorImpl implements LifecyclePredicateVisitor {
   private final XmlWriter xml;

   public LifecyclePredicateVisitorImpl(XmlWriter xml) {
      this.xml = xml;
   }

   @Override
   public void visit(LifecyclePrefixPredicate lifecyclePrefixPredicate) {
      BucketConfigurationXmlFactoryFunctions.writePrefix(this.xml, lifecyclePrefixPredicate.getPrefix());
   }

   @Override
   public void visit(LifecycleTagPredicate lifecycleTagPredicate) {
      BucketConfigurationXmlFactoryFunctions.writeTag(this.xml, lifecycleTagPredicate.getTag());
   }

   @Override
   public void visit(LifecycleObjectSizeGreaterThanPredicate lifecycleObjectSizeGreaterThanPredicate) {
      BucketConfigurationXmlFactoryFunctions.writeObjectSizeGreaterThan(this.xml, lifecycleObjectSizeGreaterThanPredicate.getObjectSizeGreaterThan());
   }

   @Override
   public void visit(LifecycleObjectSizeLessThanPredicate lifecycleObjectSizeLessThanPredicate) {
      BucketConfigurationXmlFactoryFunctions.writeObjectSizeLessThan(this.xml, lifecycleObjectSizeLessThanPredicate.getObjectSizeLessThan());
   }

   @Override
   public void visit(LifecycleAndOperator lifecycleAndOperator) {
      this.xml.start("And");

      for(LifecycleFilterPredicate predicate : lifecycleAndOperator.getOperands()) {
         predicate.accept(this);
      }

      this.xml.end();
   }
}
