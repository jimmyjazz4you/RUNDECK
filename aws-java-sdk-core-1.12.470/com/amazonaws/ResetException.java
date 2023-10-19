package com.amazonaws;

public class ResetException extends SdkClientException {
   private static final long serialVersionUID = 1L;
   private String extraInfo;

   public ResetException(String message, Throwable t) {
      super(message, t);
   }

   public ResetException(Throwable t) {
      super("", t);
   }

   public ResetException(String message) {
      super(message);
   }

   public ResetException() {
      super("");
   }

   @Override
   public boolean isRetryable() {
      return false;
   }

   @Override
   public String getMessage() {
      String msg = super.getMessage();
      return this.extraInfo == null ? msg : msg + ";  " + this.extraInfo;
   }

   public String getExtraInfo() {
      return this.extraInfo;
   }

   public void setExtraInfo(String extraInfo) {
      this.extraInfo = extraInfo;
   }
}
