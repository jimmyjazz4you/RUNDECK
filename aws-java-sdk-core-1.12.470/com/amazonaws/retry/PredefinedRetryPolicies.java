package com.amazonaws.retry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import java.io.IOException;

public class PredefinedRetryPolicies {
   public static final RetryPolicy NO_RETRY_POLICY = new RetryPolicy(
      RetryPolicy.RetryCondition.NO_RETRY_CONDITION, RetryPolicy.BackoffStrategy.NO_DELAY, 0, false
   );
   public static final int DEFAULT_MAX_ERROR_RETRY = 3;
   public static final int DEFAULT_MAX_ERROR_RETRY_STANDARD_MODE = 2;
   public static final RetryPolicy DEFAULT = getDefaultRetryPolicy();
   private static final int DYNAMODB_STANDARD_DEFAULT_MAX_ERROR_RETRY = 10;
   public static final int DYNAMODB_DEFAULT_MAX_ERROR_RETRY = 10;
   public static final RetryPolicy DYNAMODB_DEFAULT = getDynamoDBDefaultRetryPolicy();
   public static final RetryPolicy.RetryCondition DEFAULT_RETRY_CONDITION = new PredefinedRetryPolicies.SDKDefaultRetryCondition();
   public static final RetryPolicy.BackoffStrategy DEFAULT_BACKOFF_STRATEGY = new PredefinedBackoffStrategies.SDKDefaultBackoffStrategy();
   public static final V2CompatibleBackoffStrategy DEFAULT_BACKOFF_STRATEGY_V2 = new PredefinedBackoffStrategies.SDKDefaultBackoffStrategy();
   public static final RetryPolicy.BackoffStrategy DYNAMODB_DEFAULT_BACKOFF_STRATEGY = new PredefinedBackoffStrategies.SDKDefaultBackoffStrategy(25, 500, 20000);

   public static RetryPolicy.BackoffStrategy getDefaultBackoffStrategy(RetryMode retryMode) {
      switch(retryMode) {
         case LEGACY:
            return DEFAULT_BACKOFF_STRATEGY;
         case ADAPTIVE:
         case STANDARD:
            return PredefinedBackoffStrategies.STANDARD_BACKOFF_STRATEGY;
         default:
            throw new IllegalStateException("Unsupported RetryMode: " + retryMode);
      }
   }

   public static RetryPolicy getDefaultRetryPolicy() {
      return new RetryPolicy(DEFAULT_RETRY_CONDITION, DEFAULT_BACKOFF_STRATEGY, 3, true, true, true);
   }

   public static RetryPolicy getDynamoDBDefaultRetryPolicy() {
      return new RetryPolicy(DEFAULT_RETRY_CONDITION, DYNAMODB_DEFAULT_BACKOFF_STRATEGY, 10, true, false, false);
   }

   public static RetryPolicy getDefaultRetryPolicyWithCustomMaxRetries(int maxErrorRetry) {
      return new RetryPolicy(DEFAULT_RETRY_CONDITION, DEFAULT_BACKOFF_STRATEGY, maxErrorRetry, false);
   }

   public static RetryPolicy getDynamoDBDefaultRetryPolicyWithCustomMaxRetries(int maxErrorRetry) {
      return new RetryPolicy(DEFAULT_RETRY_CONDITION, DYNAMODB_DEFAULT_BACKOFF_STRATEGY, maxErrorRetry, false);
   }

   public static class SDKDefaultRetryCondition implements RetryPolicy.RetryCondition {
      @Override
      public boolean shouldRetry(AmazonWebServiceRequest originalRequest, AmazonClientException exception, int retriesAttempted) {
         if (exception.getCause() instanceof IOException) {
            return true;
         } else {
            if (exception instanceof AmazonServiceException) {
               AmazonServiceException ase = (AmazonServiceException)exception;
               if (RetryUtils.isRetryableServiceException(ase)) {
                  return true;
               }

               if (RetryUtils.isThrottlingException(ase)) {
                  return true;
               }

               if (RetryUtils.isClockSkewError(ase)) {
                  return true;
               }
            }

            return false;
         }
      }
   }
}
