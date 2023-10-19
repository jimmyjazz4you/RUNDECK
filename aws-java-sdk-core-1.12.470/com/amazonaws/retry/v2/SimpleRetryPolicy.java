package com.amazonaws.retry.v2;

import com.amazonaws.util.ValidationUtils;

public class SimpleRetryPolicy implements RetryPolicy {
   private final RetryCondition retryCondition;
   private final BackoffStrategy backoffStrategy;

   public SimpleRetryPolicy(RetryCondition retryCondition, BackoffStrategy backoffStrategy) {
      this.retryCondition = ValidationUtils.assertNotNull(retryCondition, "retryCondition");
      this.backoffStrategy = ValidationUtils.assertNotNull(backoffStrategy, "backoffStrategy");
   }

   @Override
   public long computeDelayBeforeNextRetry(RetryPolicyContext context) {
      return this.backoffStrategy.computeDelayBeforeNextRetry(context);
   }

   @Override
   public boolean shouldRetry(RetryPolicyContext context) {
      return this.retryCondition.shouldRetry(context);
   }
}
