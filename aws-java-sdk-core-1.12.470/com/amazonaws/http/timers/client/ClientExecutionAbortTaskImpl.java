package com.amazonaws.http.timers.client;

import com.amazonaws.annotation.SdkInternalApi;
import org.apache.http.client.methods.HttpRequestBase;

@SdkInternalApi
public class ClientExecutionAbortTaskImpl implements ClientExecutionAbortTask {
   private volatile boolean hasTaskExecuted;
   private HttpRequestBase currentHttpRequest;
   private final Thread thread;
   private volatile boolean isCancelled;
   private final Object lock = new Object();

   public ClientExecutionAbortTaskImpl(Thread thread) {
      this.thread = thread;
   }

   @Override
   public void run() {
      synchronized(this.lock) {
         if (!this.isCancelled) {
            this.hasTaskExecuted = true;
            if (!this.thread.isInterrupted()) {
               this.thread.interrupt();
            }

            if (!this.currentHttpRequest.isAborted()) {
               this.currentHttpRequest.abort();
            }
         }
      }
   }

   @Override
   public void setCurrentHttpRequest(HttpRequestBase newRequest) {
      this.currentHttpRequest = newRequest;
   }

   @Override
   public boolean hasClientExecutionAborted() {
      synchronized(this.lock) {
         return this.hasTaskExecuted;
      }
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   @Override
   public void cancel() {
      synchronized(this.lock) {
         this.isCancelled = true;
      }
   }
}
