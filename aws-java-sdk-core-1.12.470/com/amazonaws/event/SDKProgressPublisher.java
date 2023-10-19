package com.amazonaws.event;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import org.apache.commons.logging.LogFactory;

public class SDKProgressPublisher {
   private static volatile Future<?> latestFutureTask;

   public static Future<?> publishProgress(ProgressListener listener, ProgressEventType type) {
      return listener != ProgressListener.NOOP && listener != null && type != null ? deliverEvent(listener, new ProgressEvent(type)) : null;
   }

   private static Future<?> deliverEvent(final ProgressListener listener, final ProgressEvent event) {
      if (listener instanceof DeliveryMode) {
         DeliveryMode mode = (DeliveryMode)listener;
         if (mode.isSyncCallSafe()) {
            return quietlyCallListener(listener, event);
         }
      }

      return latestFutureTask = SDKProgressPublisher.LazyHolder.executor.submit(new Runnable() {
         @Override
         public void run() {
            listener.progressChanged(event);
         }
      });
   }

   private static Future<?> quietlyCallListener(ProgressListener listener, ProgressEvent event) {
      try {
         listener.progressChanged(event);
      } catch (Throwable var3) {
         LogFactory.getLog(SDKProgressPublisher.class).debug("Failure from the event listener", var3);
      }

      return null;
   }

   public static Future<?> publishRequestContentLength(ProgressListener listener, long bytes) {
      return publishByteCountEvent(listener, ProgressEventType.REQUEST_CONTENT_LENGTH_EVENT, bytes);
   }

   public static Future<?> publishResponseContentLength(ProgressListener listener, long bytes) {
      return publishByteCountEvent(listener, ProgressEventType.RESPONSE_CONTENT_LENGTH_EVENT, bytes);
   }

   public static Future<?> publishRequestBytesTransferred(ProgressListener listener, long bytes) {
      return publishByteCountEvent(listener, ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT, bytes);
   }

   public static Future<?> publishResponseBytesTransferred(ProgressListener listener, long bytes) {
      return publishByteCountEvent(listener, ProgressEventType.RESPONSE_BYTE_TRANSFER_EVENT, bytes);
   }

   private static Future<?> publishByteCountEvent(ProgressListener listener, ProgressEventType type, long bytes) {
      return listener != ProgressListener.NOOP && listener != null && bytes > 0L ? deliverEvent(listener, new ProgressEvent(type, bytes)) : null;
   }

   public static Future<?> publishRequestReset(ProgressListener listener, long bytesReset) {
      return publishResetEvent(listener, ProgressEventType.HTTP_REQUEST_CONTENT_RESET_EVENT, bytesReset);
   }

   public static Future<?> publishResponseReset(ProgressListener listener, long bytesReset) {
      return publishResetEvent(listener, ProgressEventType.HTTP_RESPONSE_CONTENT_RESET_EVENT, bytesReset);
   }

   public static Future<?> publishResponseBytesDiscarded(ProgressListener listener, long bytesDiscarded) {
      return publishResetEvent(listener, ProgressEventType.RESPONSE_BYTE_DISCARD_EVENT, bytesDiscarded);
   }

   private static Future<?> publishResetEvent(ProgressListener listener, ProgressEventType resetEventType, long bytesReset) {
      if (bytesReset <= 0L) {
         return null;
      } else {
         return listener != ProgressListener.NOOP && listener != null ? deliverEvent(listener, new ProgressEvent(resetEventType, bytesReset)) : null;
      }
   }

   protected static ExecutorService getExecutorService() {
      return SDKProgressPublisher.LazyHolder.executor;
   }

   protected static Future<?> setLatestFutureTask(Future<?> f) {
      latestFutureTask = f;
      return f;
   }

   @Deprecated
   public static void waitTillCompletion() throws InterruptedException, ExecutionException {
      if (latestFutureTask != null) {
         latestFutureTask.get();
      }
   }

   public static void shutdown(boolean now) {
      if (now) {
         SDKProgressPublisher.LazyHolder.executor.shutdownNow();
      } else {
         SDKProgressPublisher.LazyHolder.executor.shutdown();
      }
   }

   private static final class LazyHolder {
      private static final ExecutorService executor = createNewExecutorService();

      private static ExecutorService createNewExecutorService() {
         return Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
               Thread t = new Thread(r);
               t.setName("java-sdk-progress-listener-callback-thread");
               t.setDaemon(true);
               return t;
            }
         });
      }
   }
}
