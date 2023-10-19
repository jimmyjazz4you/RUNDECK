package com.amazonaws.retry.v2;

public interface RetryCondition {
   boolean shouldRetry(RetryPolicyContext var1);
}
