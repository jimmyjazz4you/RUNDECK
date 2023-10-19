package com.amazonaws.http.timers.client;

import com.amazonaws.annotation.SdkInternalApi;
import org.apache.http.client.methods.HttpRequestBase;

@SdkInternalApi
public interface ClientExecutionAbortTask extends Runnable {
   void setCurrentHttpRequest(HttpRequestBase var1);

   boolean hasClientExecutionAborted();

   boolean isEnabled();

   void cancel();
}
