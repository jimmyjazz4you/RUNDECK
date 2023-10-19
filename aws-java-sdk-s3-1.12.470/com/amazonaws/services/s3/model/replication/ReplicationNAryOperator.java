package com.amazonaws.services.s3.model.replication;

import java.util.List;

abstract class ReplicationNAryOperator extends ReplicationFilterPredicate {
   private final List<ReplicationFilterPredicate> operands;

   public ReplicationNAryOperator(List<ReplicationFilterPredicate> operands) {
      this.operands = operands;
   }

   public List<ReplicationFilterPredicate> getOperands() {
      return this.operands;
   }
}
