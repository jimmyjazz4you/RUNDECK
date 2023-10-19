package com.amazonaws.http.timers.request;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.http.timers.TimeoutThreadPoolBuilder;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.methods.HttpRequestBase;

@ThreadSafe
@SdkInternalApi
public class HttpRequestTimer {
   private static final String threadNamePrefix = "AwsSdkRequestTimerThread";
   private volatile ScheduledThreadPoolExecutor executor;

   public HttpRequestAbortTaskTracker startTimer(HttpRequestBase apacheRequest, int requestTimeoutMillis) {
      if (this.isTimeoutDisabled(requestTimeoutMillis)) {
         return NoOpHttpRequestAbortTaskTracker.INSTANCE;
      } else {
         if (this.executor == null) {
            this.initializeExecutor();
         }

         HttpRequestAbortTaskImpl timerTask = new HttpRequestAbortTaskImpl(apacheRequest);
         ScheduledFuture<?> timerTaskFuture = this.executor.schedule(timerTask, (long)requestTimeoutMillis, TimeUnit.MILLISECONDS);
         return new HttpRequestAbortTaskTrackerImpl(timerTask, timerTaskFuture);
      }
   }

   private boolean isTimeoutDisabled(int requestTimeoutMillis) {
      return requestTimeoutMillis <= 0;
   }

   private synchronized void initializeExecutor() {
      if (this.executor == null) {
         this.executor = TimeoutThreadPoolBuilder.buildDefaultTimeoutThreadPool("AwsSdkRequestTimerThread");
      }
   }

   public synchronized void shutdown() {
      if (this.executor != null) {
         this.executor.shutdown();
      }
   }

   @SdkTestInternalApi
   public ScheduledThreadPoolExecutor getExecutor() {
      return this.executor;
   }
}
