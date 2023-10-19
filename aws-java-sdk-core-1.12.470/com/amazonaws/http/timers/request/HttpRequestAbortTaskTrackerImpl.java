package com.amazonaws.http.timers.request;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.ValidationUtils;
import java.util.concurrent.ScheduledFuture;

@SdkInternalApi
public class HttpRequestAbortTaskTrackerImpl implements HttpRequestAbortTaskTracker {
   private final HttpRequestAbortTask task;
   private final ScheduledFuture<?> future;

   public HttpRequestAbortTaskTrackerImpl(HttpRequestAbortTask task, ScheduledFuture<?> future) {
      this.task = ValidationUtils.assertNotNull(task, "task");
      this.future = ValidationUtils.assertNotNull(future, "future");
   }

   @Override
   public boolean httpRequestAborted() {
      return this.task.httpRequestAborted();
   }

   @Override
   public boolean isEnabled() {
      return this.task.isEnabled();
   }

   @Override
   public void cancelTask() {
      this.future.cancel(false);
   }
}
