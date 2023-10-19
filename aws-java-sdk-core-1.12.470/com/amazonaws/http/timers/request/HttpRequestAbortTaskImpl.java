package com.amazonaws.http.timers.request;

import com.amazonaws.annotation.SdkInternalApi;
import org.apache.http.client.methods.HttpRequestBase;

@SdkInternalApi
public class HttpRequestAbortTaskImpl implements HttpRequestAbortTask {
   private final HttpRequestBase httpRequest;
   private volatile boolean httpRequestAborted;

   public HttpRequestAbortTaskImpl(HttpRequestBase httpRequest) {
      this.httpRequest = httpRequest;
   }

   @Override
   public void run() {
      if (!this.httpRequest.isAborted()) {
         this.httpRequestAborted = true;
         this.httpRequest.abort();
      }
   }

   @Override
   public boolean httpRequestAborted() {
      return this.httpRequestAborted;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
