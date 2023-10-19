package com.amazonaws.waiters;

public class MaxAttemptsRetryStrategy implements PollingStrategy.RetryStrategy {
   private final int defaultMaxAttempts;

   public MaxAttemptsRetryStrategy(int defaultMaxAttempts) {
      this.defaultMaxAttempts = defaultMaxAttempts;
   }

   @Override
   public boolean shouldRetry(PollingStrategyContext pollingStrategyContext) {
      return pollingStrategyContext.getRetriesAttempted() < this.defaultMaxAttempts;
   }
}
