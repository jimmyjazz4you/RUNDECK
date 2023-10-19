package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.internal.EC2ResourceFetcher;
import com.amazonaws.internal.InstanceMetadataServiceResourceFetcher;
import com.amazonaws.retry.internal.CredentialsEndpointRetryParameters;
import com.amazonaws.retry.internal.CredentialsEndpointRetryPolicy;
import com.amazonaws.util.EC2MetadataUtils;
import java.net.URI;

@SdkInternalApi
final class InstanceMetadataServiceCredentialsFetcher extends BaseCredentialsFetcher implements CredentialsEndpointRetryPolicy {
   private final EC2ResourceFetcher resourceFetcher;

   InstanceMetadataServiceCredentialsFetcher() {
      super(SdkClock.STANDARD, true);
      this.resourceFetcher = InstanceMetadataServiceResourceFetcher.getInstance();
   }

   @SdkTestInternalApi
   InstanceMetadataServiceCredentialsFetcher(SdkClock clock, EC2ResourceFetcher resourceFetcher) {
      super(clock, true);
      this.resourceFetcher = resourceFetcher;
   }

   @Override
   protected String getCredentialsResponse() {
      URI credentialsEndpoint = this.getCredentialsEndpoint();
      return this.resourceFetcher.readResource(credentialsEndpoint, this);
   }

   @Override
   public String toString() {
      return "InstanceMetadataServiceCredentialsFetcher";
   }

   private URI getCredentialsEndpoint() {
      String host = EC2MetadataUtils.getHostAddressForEC2MetadataService();
      String securityCredentialsList = this.resourceFetcher.readResource(URI.create(host + "/latest/meta-data/iam/security-credentials/"), this);
      String[] securityCredentials = securityCredentialsList.trim().split("\n");
      if (securityCredentials.length == 0) {
         throw new SdkClientException("Unable to load credentials path");
      } else {
         return URI.create(host + "/latest/meta-data/iam/security-credentials/" + securityCredentials[0]);
      }
   }

   @Override
   public boolean shouldRetry(int retriesAttempted, CredentialsEndpointRetryParameters retryParams) {
      return false;
   }
}
