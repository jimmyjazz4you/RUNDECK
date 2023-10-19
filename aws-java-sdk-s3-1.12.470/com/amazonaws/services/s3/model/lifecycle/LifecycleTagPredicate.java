package com.amazonaws.services.s3.model.lifecycle;

import com.amazonaws.services.s3.model.Tag;

public final class LifecycleTagPredicate extends LifecycleFilterPredicate {
   private final Tag tag;

   public LifecycleTagPredicate(Tag tag) {
      this.tag = tag;
   }

   public Tag getTag() {
      return this.tag;
   }

   @Override
   public void accept(LifecyclePredicateVisitor lifecyclePredicateVisitor) {
      lifecyclePredicateVisitor.visit(this);
   }
}
