package com.amazonaws.waiters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.ValidationUtils;

@SdkProtectedApi
public class WaiterExecution<Input extends AmazonWebServiceRequest, Output> {
   private final SdkFunction<Input, Output> sdkFunction;
   private final Input request;
   private final CompositeAcceptor<Output> acceptor;
   private final PollingStrategy pollingStrategy;

   public WaiterExecution(WaiterExecutionBuilder<Input, Output> waiterExecutionBuilder) {
      this.sdkFunction = ValidationUtils.assertNotNull(waiterExecutionBuilder.getSdkFunction(), "sdkFunction");
      this.request = ValidationUtils.assertNotNull(waiterExecutionBuilder.getRequest(), "request");
      this.acceptor = new CompositeAcceptor<>(ValidationUtils.assertNotNull(waiterExecutionBuilder.getAcceptorsList(), "acceptors"));
      this.pollingStrategy = ValidationUtils.assertNotNull(waiterExecutionBuilder.getPollingStrategy(), "pollingStrategy");
   }

   public boolean pollResource() throws AmazonServiceException, WaiterTimedOutException, WaiterUnrecoverableException {
      int retriesAttempted = 0;

      while(true) {
         switch(this.getCurrentState()) {
            case SUCCESS:
               return true;
            case FAILURE:
               throw new WaiterUnrecoverableException("Resource never entered the desired state as it failed.");
            case RETRY:
               PollingStrategyContext pollingStrategyContext = new PollingStrategyContext(this.request, retriesAttempted);
               if (!this.pollingStrategy.getRetryStrategy().shouldRetry(pollingStrategyContext)) {
                  throw new WaiterTimedOutException("Reached maximum attempts without transitioning to the desired state");
               }

               this.safeCustomDelay(pollingStrategyContext);
               ++retriesAttempted;
         }
      }
   }

   private WaiterState getCurrentState() throws AmazonServiceException {
      try {
         return this.acceptor.accepts(this.sdkFunction.apply(this.request));
      } catch (AmazonServiceException var2) {
         return this.acceptor.accepts(var2);
      }
   }

   private void safeCustomDelay(PollingStrategyContext pollingStrategyContext) {
      try {
         this.pollingStrategy.getDelayStrategy().delayBeforeNextRetry(pollingStrategyContext);
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         throw new RuntimeException(var3);
      }
   }
}
