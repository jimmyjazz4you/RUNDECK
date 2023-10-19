package com.amazonaws.http.timers.request;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface HttpRequestAbortTaskTracker {
   boolean httpRequestAborted();

   boolean isEnabled();

   void cancelTask();
}
