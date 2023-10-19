package com.amazonaws.http.timers.client;

import com.amazonaws.SdkClientException;

public class ClientExecutionTimeoutException extends SdkClientException {
   private static final long serialVersionUID = 4861767589924758934L;

   public ClientExecutionTimeoutException() {
      this("Client execution did not complete before the specified timeout configuration.");
   }

   public ClientExecutionTimeoutException(String message) {
      super(message);
   }
}
