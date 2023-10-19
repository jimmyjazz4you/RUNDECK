package com.amazonaws.http.timers.client;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.ValidationUtils;
import java.util.concurrent.ScheduledFuture;
import org.apache.http.client.methods.HttpRequestBase;

@SdkInternalApi
public class ClientExecutionAbortTrackerTaskImpl implements ClientExecutionAbortTrackerTask {
   private final ClientExecutionAbortTask task;
   private final ScheduledFuture<?> future;

   public ClientExecutionAbortTrackerTaskImpl(ClientExecutionAbortTask task, ScheduledFuture<?> future) {
      this.task = ValidationUtils.assertNotNull(task, "task");
      this.future = ValidationUtils.assertNotNull(future, "future");
   }

   @Override
   public void setCurrentHttpRequest(HttpRequestBase newRequest) {
      this.task.setCurrentHttpRequest(newRequest);
   }

   @Override
   public boolean hasTimeoutExpired() {
      return this.task.hasClientExecutionAborted();
   }

   @Override
   public boolean isEnabled() {
      return this.task.isEnabled();
   }

   @Override
   public void cancelTask() {
      this.future.cancel(false);
      this.task.cancel();
   }
}
