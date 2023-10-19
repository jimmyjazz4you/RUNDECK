package com.amazonaws.http.timers.client;

import org.apache.http.client.methods.HttpRequestBase;

public class NoOpClientExecutionAbortTrackerTask implements ClientExecutionAbortTrackerTask {
   public static final NoOpClientExecutionAbortTrackerTask INSTANCE = new NoOpClientExecutionAbortTrackerTask();

   private NoOpClientExecutionAbortTrackerTask() {
   }

   @Override
   public void setCurrentHttpRequest(HttpRequestBase newRequest) {
   }

   @Override
   public boolean hasTimeoutExpired() {
      return false;
   }

   @Override
   public boolean isEnabled() {
      return false;
   }

   @Override
   public void cancelTask() {
   }
}
