package com.amazonaws.waiters;

import com.amazonaws.SdkClientException;

public class WaiterUnrecoverableException extends SdkClientException {
   public WaiterUnrecoverableException(String message) {
      super(message);
   }
}
