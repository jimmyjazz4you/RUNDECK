package com.amazonaws.services.s3.model.intelligenttiering;

public interface IntelligentTieringPredicateVisitor {
   void visit(IntelligentTieringPrefixPredicate var1);

   void visit(IntelligentTieringTagPredicate var1);

   void visit(IntelligentTieringAndOperator var1);
}
