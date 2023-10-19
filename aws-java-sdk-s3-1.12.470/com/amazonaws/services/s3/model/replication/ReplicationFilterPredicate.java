package com.amazonaws.services.s3.model.replication;

import java.io.Serializable;

public abstract class ReplicationFilterPredicate implements Serializable {
   public abstract void accept(ReplicationPredicateVisitor var1);
}
