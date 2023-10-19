package com.amazonaws.services.s3.model.replication;

import java.io.Serializable;

public class ReplicationFilter implements Serializable {
   private ReplicationFilterPredicate predicate;

   public ReplicationFilter() {
   }

   public ReplicationFilter(ReplicationFilterPredicate predicate) {
      this.predicate = predicate;
   }

   public ReplicationFilterPredicate getPredicate() {
      return this.predicate;
   }

   public void setPredicate(ReplicationFilterPredicate predicate) {
      this.predicate = predicate;
   }

   public ReplicationFilter withPredicate(ReplicationFilterPredicate predicate) {
      this.setPredicate(predicate);
      return this;
   }
}
