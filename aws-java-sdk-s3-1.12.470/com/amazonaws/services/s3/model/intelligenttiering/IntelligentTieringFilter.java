package com.amazonaws.services.s3.model.intelligenttiering;

import java.io.Serializable;

public class IntelligentTieringFilter implements Serializable {
   private IntelligentTieringFilterPredicate predicate;

   public IntelligentTieringFilter() {
   }

   public IntelligentTieringFilter(IntelligentTieringFilterPredicate predicate) {
      this.predicate = predicate;
   }

   public IntelligentTieringFilterPredicate getPredicate() {
      return this.predicate;
   }

   public void setPredicate(IntelligentTieringFilterPredicate predicate) {
      this.predicate = predicate;
   }

   public IntelligentTieringFilter withPredicate(IntelligentTieringFilterPredicate predicate) {
      this.setPredicate(predicate);
      return this;
   }
}
