package com.amazonaws.services.s3.transfer.exception;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.transfer.PauseStatus;

public class PauseException extends SdkClientException {
   private static final long serialVersionUID = 1L;
   private final PauseStatus status;

   public PauseException(PauseStatus status) {
      super("Failed to pause operation; status=" + status);
      if (status != null && status != PauseStatus.SUCCESS) {
         this.status = status;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public PauseStatus getPauseStatus() {
      return this.status;
   }

   public boolean isRetryable() {
      return false;
   }
}
