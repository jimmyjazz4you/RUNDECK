package com.amazonaws;

public class AbortedException extends SdkClientException {
   private static final long serialVersionUID = 1L;

   public AbortedException(String message, Throwable t) {
      super(message, t);
   }

   public AbortedException(Throwable t) {
      super("", t);
   }

   public AbortedException(String message) {
      super(message);
   }

   public AbortedException() {
      super("");
   }

   @Override
   public boolean isRetryable() {
      return false;
   }
}
