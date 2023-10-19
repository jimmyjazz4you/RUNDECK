package com.amazonaws.handlers;

import com.amazonaws.Request;

public final class HandlerBeforeAttemptContext {
   private final Request<?> request;

   private HandlerBeforeAttemptContext(Request<?> request) {
      this.request = request;
   }

   public Request<?> getRequest() {
      return this.request;
   }

   public static HandlerBeforeAttemptContext.HandlerBeforeAttemptContextBuilder builder() {
      return new HandlerBeforeAttemptContext.HandlerBeforeAttemptContextBuilder();
   }

   public static class HandlerBeforeAttemptContextBuilder {
      private Request<?> request;

      private HandlerBeforeAttemptContextBuilder() {
      }

      public HandlerBeforeAttemptContext.HandlerBeforeAttemptContextBuilder withRequest(Request<?> request) {
         this.request = request;
         return this;
      }

      public HandlerBeforeAttemptContext build() {
         return new HandlerBeforeAttemptContext(this.request);
      }
   }
}
