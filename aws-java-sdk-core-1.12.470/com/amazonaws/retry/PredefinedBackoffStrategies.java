package com.amazonaws.retry;

import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.retry.v2.BackoffStrategy;
import com.amazonaws.retry.v2.RetryPolicyContext;
import com.amazonaws.util.ValidationUtils;
import java.util.Random;

public class PredefinedBackoffStrategies {
   private static final int SDK_DEFAULT_BASE_DELAY = 100;
   static final int SDK_DEFAULT_THROTTLED_BASE_DELAY = 500;
   static final int SDK_DEFAULT_MAX_BACKOFF_IN_MILLISECONDS = 20000;
   static final int DYNAMODB_DEFAULT_BASE_DELAY = 25;
   private static final int MAX_RETRIES = 30;
   static final int STANDARD_DEFAULT_BASE_DELAY_IN_MILLISECONDS = 100;
   static final V2CompatibleBackoffStrategy STANDARD_BACKOFF_STRATEGY = new PredefinedBackoffStrategies.FullJitterBackoffStrategy(100, 20000);

   private static int calculateExponentialDelay(int retriesAttempted, int baseDelay, int maxBackoffTime) {
      int retries = Math.min(retriesAttempted, 30);
      return (int)Math.min((1L << retries) * (long)baseDelay, (long)maxBackoffTime);
   }

   public static class EqualJitterBackoffStrategy extends V2CompatibleBackoffStrategyAdapter {
      private final int baseDelay;
      private final int maxBackoffTime;
      private final Random random = new Random();

      public EqualJitterBackoffStrategy(int baseDelay, int maxBackoffTime) {
         this.baseDelay = ValidationUtils.assertIsPositive(baseDelay, "Base delay");
         this.maxBackoffTime = ValidationUtils.assertIsPositive(maxBackoffTime, "Max backoff");
      }

      @Override
      public long computeDelayBeforeNextRetry(RetryPolicyContext context) {
         int ceil = PredefinedBackoffStrategies.calculateExponentialDelay(context.retriesAttempted(), this.baseDelay, this.maxBackoffTime);
         return (long)(ceil / 2 + this.random.nextInt(ceil / 2 + 1));
      }
   }

   public static class ExponentialBackoffStrategy extends V2CompatibleBackoffStrategyAdapter {
      private final int baseDelay;
      private final int maxBackoffTime;

      public ExponentialBackoffStrategy(int baseDelay, int maxBackoffTime) {
         this.baseDelay = ValidationUtils.assertIsPositive(baseDelay, "Base delay");
         this.maxBackoffTime = ValidationUtils.assertIsPositive(maxBackoffTime, "Max backoff");
      }

      @Override
      public long computeDelayBeforeNextRetry(RetryPolicyContext context) {
         return (long)PredefinedBackoffStrategies.calculateExponentialDelay(context.retriesAttempted(), this.baseDelay, this.maxBackoffTime);
      }
   }

   public static class FullJitterBackoffStrategy extends V2CompatibleBackoffStrategyAdapter {
      private final int baseDelay;
      private final int maxBackoffTime;
      private final Random random;

      public FullJitterBackoffStrategy(int baseDelay, int maxBackoffTime) {
         this(baseDelay, maxBackoffTime, new Random());
      }

      @SdkTestInternalApi
      FullJitterBackoffStrategy(int baseDelay, int maxBackoffTime, Random random) {
         this.baseDelay = ValidationUtils.assertIsPositive(baseDelay, "Base delay");
         this.maxBackoffTime = ValidationUtils.assertIsPositive(maxBackoffTime, "Max backoff");
         this.random = random;
      }

      @Override
      public long computeDelayBeforeNextRetry(RetryPolicyContext context) {
         int ceil = PredefinedBackoffStrategies.calculateExponentialDelay(context.retriesAttempted(), this.baseDelay, this.maxBackoffTime);
         return (long)(this.random.nextInt(ceil) + 1);
      }
   }

   public static class SDKDefaultBackoffStrategy extends V2CompatibleBackoffStrategyAdapter {
      private final BackoffStrategy fullJitterBackoffStrategy;
      private final BackoffStrategy equalJitterBackoffStrategy;

      public SDKDefaultBackoffStrategy() {
         this.fullJitterBackoffStrategy = new PredefinedBackoffStrategies.FullJitterBackoffStrategy(100, 20000);
         this.equalJitterBackoffStrategy = new PredefinedBackoffStrategies.EqualJitterBackoffStrategy(500, 20000);
      }

      public SDKDefaultBackoffStrategy(int baseDelay, int throttledBaseDelay, int maxBackoff) {
         this.fullJitterBackoffStrategy = new PredefinedBackoffStrategies.FullJitterBackoffStrategy(baseDelay, maxBackoff);
         this.equalJitterBackoffStrategy = new PredefinedBackoffStrategies.EqualJitterBackoffStrategy(throttledBaseDelay, maxBackoff);
      }

      @Override
      public long computeDelayBeforeNextRetry(RetryPolicyContext context) {
         return RetryUtils.isThrottlingException(context.exception())
            ? this.equalJitterBackoffStrategy.computeDelayBeforeNextRetry(context)
            : this.fullJitterBackoffStrategy.computeDelayBeforeNextRetry(context);
      }
   }
}
