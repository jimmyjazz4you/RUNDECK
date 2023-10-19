package com.amazonaws.services.s3;

public class KeyWrapException extends SecurityException {
   private static final long serialVersionUID = 1L;

   public KeyWrapException() {
   }

   public KeyWrapException(String s) {
      super(s);
   }

   public KeyWrapException(String message, Throwable cause) {
      super(message, cause);
   }

   public KeyWrapException(Throwable cause) {
      super(cause);
   }
}
