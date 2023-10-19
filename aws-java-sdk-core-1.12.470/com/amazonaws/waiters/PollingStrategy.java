package com.amazonaws.waiters;

public class PollingStrategy {
   private final PollingStrategy.RetryStrategy retryStrategy;
   private final PollingStrategy.DelayStrategy delayStrategy;

   public PollingStrategy(PollingStrategy.RetryStrategy retryStrategy, PollingStrategy.DelayStrategy delayStrategy) {
      this.retryStrategy = retryStrategy;
      this.delayStrategy = delayStrategy;
   }

   PollingStrategy.RetryStrategy getRetryStrategy() {
      return this.retryStrategy;
   }

   PollingStrategy.DelayStrategy getDelayStrategy() {
      return this.delayStrategy;
   }

   public interface DelayStrategy {
      void delayBeforeNextRetry(PollingStrategyContext var1) throws InterruptedException;
   }

   public interface RetryStrategy {
      boolean shouldRetry(PollingStrategyContext var1);
   }
}
