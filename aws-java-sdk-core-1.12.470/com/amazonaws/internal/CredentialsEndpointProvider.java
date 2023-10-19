package com.amazonaws.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.retry.internal.CredentialsEndpointRetryPolicy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SdkInternalApi
public abstract class CredentialsEndpointProvider {
   public abstract URI getCredentialsEndpoint();

   public CredentialsEndpointRetryPolicy getRetryPolicy() {
      return CredentialsEndpointRetryPolicy.NO_RETRY;
   }

   public Map<String, String> getHeaders() {
      return new HashMap<>();
   }
}
