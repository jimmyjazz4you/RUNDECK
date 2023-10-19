package com.amazonaws.retry.internal;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface CredentialsEndpointRetryPolicy {
   CredentialsEndpointRetryPolicy NO_RETRY = new CredentialsEndpointRetryPolicy() {
      @Override
      public boolean shouldRetry(int retriesAttempted, CredentialsEndpointRetryParameters retryParams) {
         return false;
      }
   };

   boolean shouldRetry(int var1, CredentialsEndpointRetryParameters var2);
}
