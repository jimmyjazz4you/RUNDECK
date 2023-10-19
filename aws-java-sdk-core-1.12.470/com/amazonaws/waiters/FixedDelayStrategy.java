package com.amazonaws.waiters;

public class FixedDelayStrategy implements PollingStrategy.DelayStrategy {
   private final int defaultDelayInSeconds;

   public FixedDelayStrategy(int defaultDelayInSeconds) {
      this.defaultDelayInSeconds = defaultDelayInSeconds;
   }

   @Override
   public void delayBeforeNextRetry(PollingStrategyContext pollingStrategyContext) throws InterruptedException {
      Thread.sleep((long)(this.defaultDelayInSeconds * 1000));
   }
}
