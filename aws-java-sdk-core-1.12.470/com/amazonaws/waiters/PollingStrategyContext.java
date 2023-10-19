package com.amazonaws.waiters;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public class PollingStrategyContext {
   private final AmazonWebServiceRequest originalRequest;
   private final int retriesAttempted;

   PollingStrategyContext(AmazonWebServiceRequest originalRequest, int retriesAttempted) {
      this.originalRequest = originalRequest;
      this.retriesAttempted = retriesAttempted;
   }

   public AmazonWebServiceRequest getOriginalRequest() {
      return this.originalRequest;
   }

   public int getRetriesAttempted() {
      return this.retriesAttempted;
   }
}
