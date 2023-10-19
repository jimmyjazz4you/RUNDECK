package com.amazonaws.retry.internal;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public class CredentialsEndpointRetryParameters {
   private final Integer statusCode;
   private final Exception exception;

   private CredentialsEndpointRetryParameters(CredentialsEndpointRetryParameters.Builder builder) {
      this.statusCode = builder.statusCode;
      this.exception = builder.exception;
   }

   public Integer getStatusCode() {
      return this.statusCode;
   }

   public Exception getException() {
      return this.exception;
   }

   public static CredentialsEndpointRetryParameters.Builder builder() {
      return new CredentialsEndpointRetryParameters.Builder();
   }

   public static class Builder {
      private final Integer statusCode;
      private final Exception exception;

      private Builder() {
         this.statusCode = null;
         this.exception = null;
      }

      private Builder(Integer statusCode, Exception exception) {
         this.statusCode = statusCode;
         this.exception = exception;
      }

      public CredentialsEndpointRetryParameters.Builder withStatusCode(Integer statusCode) {
         return new CredentialsEndpointRetryParameters.Builder(statusCode, this.exception);
      }

      public CredentialsEndpointRetryParameters.Builder withException(Exception exception) {
         return new CredentialsEndpointRetryParameters.Builder(this.statusCode, exception);
      }

      public CredentialsEndpointRetryParameters build() {
         return new CredentialsEndpointRetryParameters(this);
      }
   }
}
