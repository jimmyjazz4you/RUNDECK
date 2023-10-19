package com.amazonaws.services.s3.transfer;

public final class PauseResult<T> {
   private final PauseStatus pauseStatus;
   private final T infoToResume;

   public PauseResult(PauseStatus pauseStatus, T infoToResume) {
      if (pauseStatus == null) {
         throw new IllegalArgumentException();
      } else {
         this.pauseStatus = pauseStatus;
         this.infoToResume = infoToResume;
      }
   }

   public PauseResult(PauseStatus pauseStatus) {
      this(pauseStatus, (T)null);
   }

   public PauseStatus getPauseStatus() {
      return this.pauseStatus;
   }

   public T getInfoToResume() {
      return this.infoToResume;
   }
}
