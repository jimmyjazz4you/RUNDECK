package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.DeliveryMode;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.event.SDKProgressPublisher;
import com.amazonaws.services.s3.transfer.PersistableTransfer;
import java.util.concurrent.Future;
import org.apache.commons.logging.LogFactory;

public class S3ProgressPublisher extends SDKProgressPublisher {
   public static Future<?> publishTransferPersistable(ProgressListener listener, PersistableTransfer persistableTransfer) {
      if (persistableTransfer != null && listener instanceof S3ProgressListener) {
         S3ProgressListener s3listener = (S3ProgressListener)listener;
         return deliverEvent(s3listener, persistableTransfer);
      } else {
         return null;
      }
   }

   private static Future<?> deliverEvent(final S3ProgressListener listener, final PersistableTransfer persistableTransfer) {
      if (listener instanceof DeliveryMode) {
         DeliveryMode mode = (DeliveryMode)listener;
         if (mode.isSyncCallSafe()) {
            return quietlyCallListener(listener, persistableTransfer);
         }
      }

      return setLatestFutureTask(getExecutorService().submit(new Runnable() {
         @Override
         public void run() {
            listener.onPersistableTransfer(persistableTransfer);
         }
      }));
   }

   private static Future<?> quietlyCallListener(S3ProgressListener listener, PersistableTransfer persistableTransfer) {
      try {
         listener.onPersistableTransfer(persistableTransfer);
      } catch (Throwable var3) {
         LogFactory.getLog(S3ProgressPublisher.class).debug("Failure from the event listener", var3);
      }

      return null;
   }
}
