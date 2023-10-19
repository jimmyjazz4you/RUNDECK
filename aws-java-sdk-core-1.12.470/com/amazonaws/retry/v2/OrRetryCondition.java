package com.amazonaws.retry.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrRetryCondition implements RetryCondition {
   private List<RetryCondition> conditions = new ArrayList<>();

   public OrRetryCondition(RetryCondition... conditions) {
      Collections.addAll(this.conditions, conditions);
   }

   @Override
   public boolean shouldRetry(RetryPolicyContext context) {
      for(RetryCondition retryCondition : this.conditions) {
         if (retryCondition.shouldRetry(context)) {
            return true;
         }
      }

      return false;
   }
}
