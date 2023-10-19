package com.amazonaws.http.timers.request;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface HttpRequestAbortTask extends Runnable {
   boolean httpRequestAborted();

   boolean isEnabled();
}
