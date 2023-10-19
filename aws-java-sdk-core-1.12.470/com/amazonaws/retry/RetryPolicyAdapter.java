package com.amazonaws.retry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.retry.internal.MaxAttemptsResolver;
import com.amazonaws.retry.v2.RetryPolicyContext;
import com.amazonaws.util.ValidationUtils;

@SdkInternalApi
public class RetryPolicyAdapter implements com.amazonaws.retry.v2.RetryPolicy {
   private final RetryPolicy legacyRetryPolicy;
   private final ClientConfiguration clientConfiguration;
   private final int maxErrorRetry;
   private final RetryPolicy.BackoffStrategy backoffStrategy;

   public RetryPolicyAdapter(RetryPolicy legacyRetryPolicy, ClientConfiguration clientConfiguration) {
      this.legacyRetryPolicy = ValidationUtils.assertNotNull(legacyRetryPolicy, "legacyRetryPolicy");
      this.clientConfiguration = ValidationUtils.assertNotNull(clientConfiguration, "clientConfiguration");
      this.maxErrorRetry = this.resolveMaxErrorRetry();
      this.backoffStrategy = this.resolveBackoffStrategy();
   }

   @Override
   public long computeDelayBeforeNextRetry(RetryPolicyContext context) {
      return this.backoffStrategy
         .delayBeforeNextRetry((AmazonWebServiceRequest)context.originalRequest(), (AmazonClientException)context.exception(), context.retriesAttempted());
   }

   @Override
   public boolean shouldRetry(RetryPolicyContext context) {
      return !this.maxRetriesExceeded(context) && this.isRetryable(context);
   }

   public boolean isRetryable(RetryPolicyContext context) {
      return this.legacyRetryPolicy
         .getRetryCondition()
         .shouldRetry((AmazonWebServiceRequest)context.originalRequest(), (AmazonClientException)context.exception(), context.retriesAttempted());
   }

   public RetryPolicy getLegacyRetryPolicy() {
      return this.legacyRetryPolicy;
   }

   private RetryPolicy.BackoffStrategy resolveBackoffStrategy() {
      return this.legacyRetryPolicy.isBackoffStrategyInRetryModeHonored() ? this.backoffStrategyByRetryMode() : this.legacyRetryPolicy.getBackoffStrategy();
   }

   private RetryPolicy.BackoffStrategy backoffStrategyByRetryMode() {
      RetryMode retryMode = this.clientConfiguration.getRetryMode() == null ? this.legacyRetryPolicy.getRetryMode() : this.clientConfiguration.getRetryMode();
      return PredefinedRetryPolicies.getDefaultBackoffStrategy(retryMode);
   }

   private int resolveMaxErrorRetry() {
      if (this.legacyRetryPolicy.isMaxErrorRetryInClientConfigHonored() && this.clientConfiguration.getMaxErrorRetry() >= 0) {
         return this.clientConfiguration.getMaxErrorRetry();
      } else {
         Integer resolvedMaxAttempts = new MaxAttemptsResolver().maxAttempts();
         if (resolvedMaxAttempts != null) {
            return resolvedMaxAttempts - 1;
         } else {
            return this.shouldUseStandardModeDefaultMaxRetry() ? 2 : this.legacyRetryPolicy.getMaxErrorRetry();
         }
      }
   }

   private boolean shouldUseStandardModeDefaultMaxRetry() {
      RetryMode retryMode = this.clientConfiguration.getRetryMode() == null ? this.legacyRetryPolicy.getRetryMode() : this.clientConfiguration.getRetryMode();
      return (retryMode.equals(RetryMode.STANDARD) || retryMode.equals(RetryMode.ADAPTIVE))
         && this.legacyRetryPolicy.isDefaultMaxErrorRetryInRetryModeHonored();
   }

   public boolean maxRetriesExceeded(RetryPolicyContext context) {
      return context.retriesAttempted() >= this.maxErrorRetry;
   }

   public int getMaxErrorRetry() {
      return this.maxErrorRetry;
   }

   public RetryPolicy.BackoffStrategy getBackoffStrategy() {
      return this.backoffStrategy;
   }
}
