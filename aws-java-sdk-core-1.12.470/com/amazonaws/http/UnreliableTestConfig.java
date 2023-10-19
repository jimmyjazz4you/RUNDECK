package com.amazonaws.http;

class UnreliableTestConfig {
   private int maxNumErrors = 1;
   private int bytesReadBeforeException = 100;
   private boolean isFakeIOException;
   private int resetIntervalBeforeException = 2;

   int getMaxNumErrors() {
      return this.maxNumErrors;
   }

   int getBytesReadBeforeException() {
      return this.bytesReadBeforeException;
   }

   boolean isFakeIOException() {
      return this.isFakeIOException;
   }

   int getResetIntervalBeforeException() {
      return this.resetIntervalBeforeException;
   }

   UnreliableTestConfig withMaxNumErrors(int maxNumErrors) {
      this.maxNumErrors = maxNumErrors;
      return this;
   }

   UnreliableTestConfig withBytesReadBeforeException(int bytesReadBeforeException) {
      this.bytesReadBeforeException = bytesReadBeforeException;
      return this;
   }

   UnreliableTestConfig withFakeIOException(boolean isFakeIOException) {
      this.isFakeIOException = isFakeIOException;
      return this;
   }

   UnreliableTestConfig withResetIntervalBeforeException(int resetIntervalBeforeException) {
      this.resetIntervalBeforeException = resetIntervalBeforeException;
      return this;
   }
}
