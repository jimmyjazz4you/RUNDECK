package com.amazonaws.retry.v2;

import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndRetryCondition implements RetryCondition {
   private List<RetryCondition> conditions = new ArrayList<>();

   public AndRetryCondition(RetryCondition... conditions) {
      Collections.addAll(this.conditions, ValidationUtils.assertNotEmpty(conditions, "conditions"));
   }

   @Override
   public boolean shouldRetry(RetryPolicyContext context) {
      for(RetryCondition retryCondition : this.conditions) {
         if (!retryCondition.shouldRetry(context)) {
            return false;
         }
      }

      return true;
   }
}
