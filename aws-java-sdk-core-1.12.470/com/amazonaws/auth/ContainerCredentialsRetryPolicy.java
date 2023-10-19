package com.amazonaws.auth;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.retry.internal.CredentialsEndpointRetryParameters;
import com.amazonaws.retry.internal.CredentialsEndpointRetryPolicy;
import java.io.IOException;

@SdkInternalApi
class ContainerCredentialsRetryPolicy implements CredentialsEndpointRetryPolicy {
   private static final int MAX_RETRIES = 5;
   private static ContainerCredentialsRetryPolicy instance;

   private ContainerCredentialsRetryPolicy() {
   }

   public static ContainerCredentialsRetryPolicy getInstance() {
      if (instance == null) {
         instance = new ContainerCredentialsRetryPolicy();
      }

      return instance;
   }

   @Override
   public boolean shouldRetry(int retriesAttempted, CredentialsEndpointRetryParameters retryParams) {
      if (retriesAttempted >= 5) {
         return false;
      } else {
         Integer statusCode = retryParams.getStatusCode();
         if (statusCode != null && statusCode >= 500 && statusCode < 600) {
            return true;
         } else {
            return retryParams.getException() != null && retryParams.getException() instanceof IOException;
         }
      }
   }
}
