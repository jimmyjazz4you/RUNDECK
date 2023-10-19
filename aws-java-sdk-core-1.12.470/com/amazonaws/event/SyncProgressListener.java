package com.amazonaws.event;

public abstract class SyncProgressListener implements ProgressListener, DeliveryMode {
   @Override
   public boolean isSyncCallSafe() {
      return true;
   }
}
