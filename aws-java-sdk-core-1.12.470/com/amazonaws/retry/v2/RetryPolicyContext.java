package com.amazonaws.retry.v2;

import com.amazonaws.Request;
import com.amazonaws.SdkBaseException;
import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkInternalApi;

@Immutable
public class RetryPolicyContext {
   private final Object originalRequest;
   private final Request<?> request;
   private final SdkBaseException exception;
   private final int retriesAttempted;
   private final Integer httpStatusCode;

   private RetryPolicyContext(Object originalRequest, Request<?> request, SdkBaseException exception, int retriesAttempted, Integer httpStatusCode) {
      this.originalRequest = originalRequest;
      this.request = request;
      this.exception = exception;
      this.retriesAttempted = retriesAttempted;
      this.httpStatusCode = httpStatusCode;
   }

   public Object originalRequest() {
      return this.originalRequest;
   }

   public Request<?> request() {
      return this.request;
   }

   public SdkBaseException exception() {
      return this.exception;
   }

   public int retriesAttempted() {
      return this.retriesAttempted;
   }

   public int totalRequests() {
      return this.retriesAttempted() + 1;
   }

   public Integer httpStatusCode() {
      return this.httpStatusCode;
   }

   @SdkInternalApi
   public static RetryPolicyContext.Builder builder() {
      return new RetryPolicyContext.Builder();
   }

   @SdkInternalApi
   public static class Builder {
      private Object originalRequest;
      private Request<?> request;
      private SdkBaseException exception;
      private int retriesAttempted;
      private Integer httpStatusCode;

      private Builder() {
      }

      public RetryPolicyContext.Builder originalRequest(Object originalRequest) {
         this.originalRequest = originalRequest;
         return this;
      }

      public RetryPolicyContext.Builder request(Request<?> request) {
         this.request = request;
         return this;
      }

      public RetryPolicyContext.Builder exception(SdkBaseException exception) {
         this.exception = exception;
         return this;
      }

      public RetryPolicyContext.Builder retriesAttempted(int retriesAttempted) {
         this.retriesAttempted = retriesAttempted;
         return this;
      }

      public RetryPolicyContext.Builder httpStatusCode(Integer httpStatusCode) {
         this.httpStatusCode = httpStatusCode;
         return this;
      }

      public RetryPolicyContext build() {
         return new RetryPolicyContext(this.originalRequest, this.request, this.exception, this.retriesAttempted, this.httpStatusCode);
      }
   }
}
