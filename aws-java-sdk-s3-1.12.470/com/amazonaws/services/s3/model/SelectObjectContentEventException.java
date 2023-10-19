package com.amazonaws.services.s3.model;

import com.amazonaws.SdkClientException;

public final class SelectObjectContentEventException extends SdkClientException {
   private String errorCode;
   private String errorMessage;

   public SelectObjectContentEventException(String exceptionMessage) {
      super(exceptionMessage);
   }

   public SelectObjectContentEventException(String exceptionMessage, Exception cause) {
      super(exceptionMessage, cause);
   }

   public String getMessage() {
      return super.getMessage();
   }

   public String getErrorCode() {
      return this.errorCode;
   }

   public void setErrorCode(String errorCode) {
      this.errorCode = errorCode;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }
}
