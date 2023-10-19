package com.amazonaws.services.s3.model.replication;

public final class ReplicationPrefixPredicate extends ReplicationFilterPredicate {
   private final String prefix;

   public ReplicationPrefixPredicate(String prefix) {
      this.prefix = prefix;
   }

   public String getPrefix() {
      return this.prefix;
   }

   @Override
   public void accept(ReplicationPredicateVisitor replicationPredicateVisitor) {
      replicationPredicateVisitor.visit(this);
   }
}
