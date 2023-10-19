package com.amazonaws.services.s3.model.replication;

import com.amazonaws.services.s3.model.Tag;

public final class ReplicationTagPredicate extends ReplicationFilterPredicate {
   private final Tag tag;

   public ReplicationTagPredicate(Tag tag) {
      this.tag = tag;
   }

   public Tag getTag() {
      return this.tag;
   }

   @Override
   public void accept(ReplicationPredicateVisitor replicationPredicateVisitor) {
      replicationPredicateVisitor.visit(this);
   }
}
