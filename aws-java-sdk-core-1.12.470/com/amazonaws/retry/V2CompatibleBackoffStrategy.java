package com.amazonaws.retry;

import com.amazonaws.retry.v2.BackoffStrategy;

public interface V2CompatibleBackoffStrategy extends RetryPolicy.BackoffStrategy, BackoffStrategy {
}
