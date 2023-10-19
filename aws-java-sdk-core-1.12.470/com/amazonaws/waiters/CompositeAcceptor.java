package com.amazonaws.waiters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.List;

@SdkInternalApi
class CompositeAcceptor<Output> {
   private List<WaiterAcceptor<Output>> acceptors = new ArrayList<>();

   public CompositeAcceptor(List<WaiterAcceptor<Output>> acceptors) {
      this.acceptors = ValidationUtils.assertNotEmpty(acceptors, "acceptors");
   }

   public List<WaiterAcceptor<Output>> getAcceptors() {
      return this.acceptors;
   }

   public WaiterState accepts(Output response) {
      for(WaiterAcceptor<Output> acceptor : this.acceptors) {
         if (acceptor.matches(response)) {
            return acceptor.getState();
         }
      }

      return WaiterState.RETRY;
   }

   public WaiterState accepts(AmazonServiceException exception) throws AmazonServiceException {
      for(WaiterAcceptor<Output> acceptor : this.acceptors) {
         if (acceptor.matches(exception)) {
            return acceptor.getState();
         }
      }

      throw exception;
   }
}
