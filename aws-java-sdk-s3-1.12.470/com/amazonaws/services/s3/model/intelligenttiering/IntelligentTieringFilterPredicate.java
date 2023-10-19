package com.amazonaws.services.s3.model.intelligenttiering;

import java.io.Serializable;

public abstract class IntelligentTieringFilterPredicate implements Serializable {
   public abstract void accept(IntelligentTieringPredicateVisitor var1);
}
