package com.amazonaws.services.s3.model.intelligenttiering;

public final class IntelligentTieringPrefixPredicate extends IntelligentTieringFilterPredicate {
   private final String prefix;

   public IntelligentTieringPrefixPredicate(String prefix) {
      this.prefix = prefix;
   }

   public String getPrefix() {
      return this.prefix;
   }

   @Override
   public void accept(IntelligentTieringPredicateVisitor intelligentTieringPredicateVisitor) {
      intelligentTieringPredicateVisitor.visit(this);
   }
}
