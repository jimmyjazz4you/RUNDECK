package com.amazonaws.retry.v2;

public interface BackoffStrategy {
   long computeDelayBeforeNextRetry(RetryPolicyContext var1);
}
