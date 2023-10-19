package com.amazonaws.services.s3.model;

import com.amazonaws.event.DeliveryMode;

@Deprecated
public class LegacyS3ProgressListener implements com.amazonaws.event.ProgressListener, DeliveryMode {
   private final ProgressListener listener;
   private final boolean syncCallSafe;

   public LegacyS3ProgressListener(ProgressListener listener) {
      this.listener = listener;
      if (listener instanceof DeliveryMode) {
         DeliveryMode mode = (DeliveryMode)listener;
         this.syncCallSafe = mode.isSyncCallSafe();
      } else {
         this.syncCallSafe = false;
      }
   }

   public ProgressListener unwrap() {
      return this.listener;
   }

   public void progressChanged(com.amazonaws.event.ProgressEvent progressEvent) {
      if (this.listener != null) {
         this.listener.progressChanged(this.adaptToLegacyEvent(progressEvent));
      }
   }

   private ProgressEvent adaptToLegacyEvent(com.amazonaws.event.ProgressEvent event) {
      long bytes = event.getBytesTransferred();
      return bytes != 0L ? new ProgressEvent(bytes) : new ProgressEvent(event.getEventType());
   }

   public boolean isSyncCallSafe() {
      return this.syncCallSafe;
   }
}
