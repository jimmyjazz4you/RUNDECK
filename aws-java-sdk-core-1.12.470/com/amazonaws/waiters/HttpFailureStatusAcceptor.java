package com.amazonaws.waiters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public class HttpFailureStatusAcceptor<Output> extends WaiterAcceptor<Output> {
   private final int expectedStatusCode;
   private final WaiterState waiterState;

   public HttpFailureStatusAcceptor(int expectedStatusCode, WaiterState waiterState) {
      this.expectedStatusCode = expectedStatusCode;
      this.waiterState = waiterState;
   }

   @Override
   public boolean matches(AmazonServiceException ase) {
      return this.expectedStatusCode == ase.getStatusCode();
   }

   @Override
   public WaiterState getState() {
      return this.waiterState;
   }
}
