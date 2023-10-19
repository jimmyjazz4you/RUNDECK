package com.amazonaws.http.timers.client;

import com.amazonaws.annotation.SdkInternalApi;
import org.apache.http.client.methods.HttpRequestBase;

@SdkInternalApi
public interface ClientExecutionAbortTrackerTask {
   void setCurrentHttpRequest(HttpRequestBase var1);

   boolean hasTimeoutExpired();

   boolean isEnabled();

   void cancelTask();
}
