package com.amazonaws.services.s3.model.replication;

import java.util.List;

public final class ReplicationAndOperator extends ReplicationNAryOperator {
   public ReplicationAndOperator(List<ReplicationFilterPredicate> operands) {
      super(operands);
   }

   @Override
   public void accept(ReplicationPredicateVisitor replicationPredicateVisitor) {
      replicationPredicateVisitor.visit(this);
   }
}
