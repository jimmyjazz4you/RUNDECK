package com.amazonaws.internal.auth;

import com.amazonaws.Request;
import com.amazonaws.RequestConfig;
import java.net.URI;

public class SignerProviderContext {
   private final URI uri;
   private final boolean isRedirect;
   private final Request<?> request;
   private final RequestConfig requestConfig;

   private SignerProviderContext(SignerProviderContext.Builder builder) {
      this.uri = builder.uri;
      this.isRedirect = builder.isRedirect;
      this.request = builder.request;
      this.requestConfig = builder.requestConfig;
   }

   public URI getUri() {
      return this.uri;
   }

   public boolean isRedirect() {
      return this.isRedirect;
   }

   public Request<?> getRequest() {
      return this.request;
   }

   public RequestConfig getRequestConfig() {
      return this.requestConfig;
   }

   public static SignerProviderContext.Builder builder() {
      return new SignerProviderContext.Builder();
   }

   public static class Builder {
      private URI uri;
      private boolean isRedirect;
      private Request<?> request;
      private RequestConfig requestConfig;

      private Builder() {
      }

      public SignerProviderContext.Builder withUri(URI uri) {
         this.uri = uri;
         return this;
      }

      public SignerProviderContext.Builder withIsRedirect(boolean withIsRedirect) {
         this.isRedirect = withIsRedirect;
         return this;
      }

      public SignerProviderContext.Builder withRequest(Request<?> request) {
         this.request = request;
         return this;
      }

      public SignerProviderContext.Builder withRequestConfig(RequestConfig requestConfig) {
         this.requestConfig = requestConfig;
         return this;
      }

      public SignerProviderContext build() {
         return new SignerProviderContext(this);
      }
   }
}
