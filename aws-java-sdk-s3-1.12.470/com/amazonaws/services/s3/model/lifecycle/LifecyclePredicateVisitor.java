package com.amazonaws.services.s3.model.lifecycle;

public interface LifecyclePredicateVisitor {
   void visit(LifecyclePrefixPredicate var1);

   void visit(LifecycleTagPredicate var1);

   void visit(LifecycleObjectSizeGreaterThanPredicate var1);

   void visit(LifecycleObjectSizeLessThanPredicate var1);

   void visit(LifecycleAndOperator var1);
}
