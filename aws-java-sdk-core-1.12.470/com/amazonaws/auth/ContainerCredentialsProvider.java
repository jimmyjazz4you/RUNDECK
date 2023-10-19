package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.internal.CredentialsEndpointProvider;
import com.amazonaws.retry.internal.CredentialsEndpointRetryPolicy;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContainerCredentialsProvider implements AWSCredentialsProvider {
   static final String ECS_CONTAINER_CREDENTIALS_PATH = "AWS_CONTAINER_CREDENTIALS_RELATIVE_URI";
   static final String CONTAINER_CREDENTIALS_FULL_URI = "AWS_CONTAINER_CREDENTIALS_FULL_URI";
   static final String CONTAINER_AUTHORIZATION_TOKEN = "AWS_CONTAINER_AUTHORIZATION_TOKEN";
   private static final String HTTPS = "https";
   private static final String ECS_CREDENTIALS_ENDPOINT = "http://169.254.170.2";
   private final ContainerCredentialsFetcher credentialsFetcher;

   @Deprecated
   public ContainerCredentialsProvider() {
      this(new ContainerCredentialsProvider.ECSCredentialsEndpointProvider());
   }

   public ContainerCredentialsProvider(CredentialsEndpointProvider credentialsEndpointProvider) {
      this.credentialsFetcher = new ContainerCredentialsFetcher(credentialsEndpointProvider);
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.credentialsFetcher.getCredentials();
   }

   @Override
   public void refresh() {
      this.credentialsFetcher.refresh();
   }

   public Date getCredentialsExpiration() {
      return this.credentialsFetcher.getCredentialsExpiration();
   }

   static class ECSCredentialsEndpointProvider extends CredentialsEndpointProvider {
      @Override
      public URI getCredentialsEndpoint() {
         String path = System.getenv("AWS_CONTAINER_CREDENTIALS_RELATIVE_URI");
         if (path == null) {
            throw new SdkClientException("The environment variable AWS_CONTAINER_CREDENTIALS_RELATIVE_URI is empty");
         } else {
            return URI.create("http://169.254.170.2" + path);
         }
      }

      @Override
      public CredentialsEndpointRetryPolicy getRetryPolicy() {
         return ContainerCredentialsRetryPolicy.getInstance();
      }
   }

   static class FullUriCredentialsEndpointProvider extends CredentialsEndpointProvider {
      @Override
      public URI getCredentialsEndpoint() {
         String fullUri = System.getenv("AWS_CONTAINER_CREDENTIALS_FULL_URI");
         if (fullUri != null && fullUri.length() != 0) {
            URI uri = URI.create(fullUri);
            if (!this.isHttps(uri) && !this.isAllowedHost(uri.getHost())) {
               throw new SdkClientException(
                  "The full URI ("
                     + uri
                     + ") contained withing environment variable "
                     + "AWS_CONTAINER_CREDENTIALS_FULL_URI"
                     + " has an invalid host. Host should resolve to a loopback address or have the full URI be HTTPS."
               );
            } else {
               return uri;
            }
         } else {
            throw new SdkClientException("The environment variable AWS_CONTAINER_CREDENTIALS_FULL_URI is empty");
         }
      }

      @Override
      public Map<String, String> getHeaders() {
         return (Map<String, String>)(System.getenv("AWS_CONTAINER_AUTHORIZATION_TOKEN") != null
            ? Collections.singletonMap("Authorization", System.getenv("AWS_CONTAINER_AUTHORIZATION_TOKEN"))
            : new HashMap<>());
      }

      private boolean isHttps(URI endpoint) {
         return Objects.equals("https", endpoint.getScheme());
      }

      private boolean isAllowedHost(String host) {
         try {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            boolean allAllowed = true;

            for(InetAddress address : addresses) {
               if (!this.isLoopbackAddress(address)) {
                  allAllowed = false;
               }
            }

            return addresses.length > 0 && allAllowed;
         } catch (UnknownHostException var8) {
            throw new SdkClientException(String.format("host (%s) could not be resolved to an IP address.", host), var8);
         }
      }

      private boolean isLoopbackAddress(InetAddress inetAddress) {
         return inetAddress.isLoopbackAddress();
      }
   }
}
