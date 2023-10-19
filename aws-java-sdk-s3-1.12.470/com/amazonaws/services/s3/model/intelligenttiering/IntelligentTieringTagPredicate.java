package com.amazonaws.services.s3.model.intelligenttiering;

import com.amazonaws.services.s3.model.Tag;

public final class IntelligentTieringTagPredicate extends IntelligentTieringFilterPredicate {
   private final Tag tag;

   public IntelligentTieringTagPredicate(Tag tag) {
      this.tag = tag;
   }

   public Tag getTag() {
      return this.tag;
   }

   @Override
   public void accept(IntelligentTieringPredicateVisitor intelligentTieringPredicateVisitor) {
      intelligentTieringPredicateVisitor.visit(this);
   }
}
