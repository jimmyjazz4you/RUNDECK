package com.amazonaws.services.s3.model.replication;

public interface ReplicationPredicateVisitor {
   void visit(ReplicationPrefixPredicate var1);

   void visit(ReplicationTagPredicate var1);

   void visit(ReplicationAndOperator var1);
}
