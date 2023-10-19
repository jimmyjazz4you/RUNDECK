package com.amazonaws.event;

import com.amazonaws.SdkClientException;

public interface ProgressListener {
   ProgressListener NOOP = new ProgressListener.NoOpProgressListener();

   void progressChanged(ProgressEvent var1);

   public static class ExceptionReporter implements ProgressListener, DeliveryMode {
      private final ProgressListener listener;
      private final boolean syncCallSafe;
      private volatile Throwable cause;

      public ExceptionReporter(ProgressListener listener) {
         if (listener == null) {
            throw new IllegalArgumentException();
         } else {
            this.listener = listener;
            if (listener instanceof DeliveryMode) {
               DeliveryMode cs = (DeliveryMode)listener;
               this.syncCallSafe = cs.isSyncCallSafe();
            } else {
               this.syncCallSafe = false;
            }
         }
      }

      @Override
      public void progressChanged(ProgressEvent progressEvent) {
         if (this.cause == null) {
            try {
               this.listener.progressChanged(progressEvent);
            } catch (Throwable var3) {
               this.cause = var3;
            }
         }
      }

      public void throwExceptionIfAny() {
         if (this.cause != null) {
            throw new SdkClientException(this.cause);
         }
      }

      public Throwable getCause() {
         return this.cause;
      }

      public static ProgressListener.ExceptionReporter wrap(ProgressListener listener) {
         return new ProgressListener.ExceptionReporter(listener);
      }

      @Override
      public boolean isSyncCallSafe() {
         return this.syncCallSafe;
      }
   }

   public static class NoOpProgressListener implements ProgressListener, DeliveryMode {
      @Override
      public boolean isSyncCallSafe() {
         return true;
      }

      @Override
      public void progressChanged(ProgressEvent progressEvent) {
      }
   }
}
