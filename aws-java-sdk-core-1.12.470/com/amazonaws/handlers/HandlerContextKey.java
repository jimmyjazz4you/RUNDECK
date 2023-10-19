package com.amazonaws.handlers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.client.builder.AdvancedConfig;
import java.net.URI;

public class HandlerContextKey<T> {
   public static final HandlerContextKey<AWSCredentials> AWS_CREDENTIALS = new HandlerContextKey<>("AWSCredentials");
   public static final HandlerContextKey<String> SIGNING_REGION = new HandlerContextKey<>("SigningRegion");
   public static final HandlerContextKey<String> SIGNING_NAME = new HandlerContextKey<>("SIGNING_NAME");
   public static final HandlerContextKey<String> OPERATION_NAME = new HandlerContextKey<>("OperationName");
   public static final HandlerContextKey<String> SERVICE_ID = new HandlerContextKey<>("ServiceId");
   public static final HandlerContextKey<Boolean> REQUIRES_LENGTH = new HandlerContextKey<>("RequiresLength");
   public static final HandlerContextKey<Boolean> HAS_STREAMING_INPUT = new HandlerContextKey<>("HasStreamingInput");
   public static final HandlerContextKey<Boolean> HAS_STREAMING_OUTPUT = new HandlerContextKey<>("HasStreamingOutput");
   public static final HandlerContextKey<AdvancedConfig> ADVANCED_CONFIG = new HandlerContextKey<>("AdvancedConfig");
   public static final HandlerContextKey<Boolean> ENDPOINT_OVERRIDDEN = new HandlerContextKey<>("EndpointOverridden");
   public static final HandlerContextKey<URI> CLIENT_ENDPOINT = new HandlerContextKey<>("ClientEndpoint");
   private final String name;

   public HandlerContextKey(String name) {
      if (name == null) {
         throw new IllegalArgumentException("Name cannot be null");
      } else {
         this.name = name;
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         HandlerContextKey<?> key = (HandlerContextKey)o;
         return this.name.equals(key.getName());
      } else {
         return false;
      }
   }

   public String getName() {
      return this.name;
   }

   @Override
   public int hashCode() {
      return this.name.hashCode();
   }
}
