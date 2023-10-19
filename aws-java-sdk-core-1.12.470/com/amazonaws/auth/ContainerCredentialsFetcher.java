package com.amazonaws.auth;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.internal.CredentialsEndpointProvider;
import com.amazonaws.internal.EC2ResourceFetcher;

@SdkInternalApi
class ContainerCredentialsFetcher extends BaseCredentialsFetcher {
   private final CredentialsEndpointProvider credentialsEndpointProvider;

   ContainerCredentialsFetcher(CredentialsEndpointProvider credentialsEndpointProvider) {
      super(SdkClock.STANDARD, false);
      this.credentialsEndpointProvider = credentialsEndpointProvider;
   }

   @Override
   protected String getCredentialsResponse() {
      return EC2ResourceFetcher.defaultResourceFetcher()
         .readResource(
            this.credentialsEndpointProvider.getCredentialsEndpoint(),
            this.credentialsEndpointProvider.getRetryPolicy(),
            this.credentialsEndpointProvider.getHeaders()
         );
   }

   @Override
   public String toString() {
      return "ContainerCredentialsFetcher";
   }
}
