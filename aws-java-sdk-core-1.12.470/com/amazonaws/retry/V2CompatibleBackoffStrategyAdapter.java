package com.amazonaws.retry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.retry.v2.RetryPolicyContext;

@SdkInternalApi
abstract class V2CompatibleBackoffStrategyAdapter implements V2CompatibleBackoffStrategy {
   @Override
   public long delayBeforeNextRetry(AmazonWebServiceRequest originalRequest, AmazonClientException exception, int retriesAttempted) {
      return this.computeDelayBeforeNextRetry(
         RetryPolicyContext.builder().originalRequest(originalRequest).exception(exception).retriesAttempted(retriesAttempted).build()
      );
   }
}
