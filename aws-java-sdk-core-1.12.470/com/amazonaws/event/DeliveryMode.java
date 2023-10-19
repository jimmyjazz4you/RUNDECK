package com.amazonaws.event;

public interface DeliveryMode {
   boolean isSyncCallSafe();

   public static class Check {
      public static boolean isSyncCallSafe(ProgressListener listener) {
         if (listener instanceof DeliveryMode) {
            DeliveryMode mode = (DeliveryMode)listener;
            return mode.isSyncCallSafe();
         } else {
            return listener == null;
         }
      }
   }
}
