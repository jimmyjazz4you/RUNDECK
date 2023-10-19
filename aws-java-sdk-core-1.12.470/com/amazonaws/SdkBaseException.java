package com.amazonaws;

public class SdkBaseException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public SdkBaseException(String message, Throwable t) {
      super(message, t);
   }

   public SdkBaseException(String message) {
      super(message);
   }

   public SdkBaseException(Throwable t) {
      super(t);
   }
}
