package com.amazonaws;

public class SdkClientException extends AmazonClientException {
   public SdkClientException(String message, Throwable t) {
      super(message, t);
   }

   public SdkClientException(String message) {
      super(message);
   }

   public SdkClientException(Throwable t) {
      super(t);
   }
}
