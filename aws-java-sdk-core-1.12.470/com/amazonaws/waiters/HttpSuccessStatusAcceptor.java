package com.amazonaws.waiters;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public class HttpSuccessStatusAcceptor<Output> extends WaiterAcceptor<Output> {
   private final WaiterState waiterState;

   public HttpSuccessStatusAcceptor(WaiterState waiterState) {
      this.waiterState = waiterState;
   }

   @Override
   public boolean matches(Output output) {
      return true;
   }

   @Override
   public WaiterState getState() {
      return this.waiterState;
   }
}
