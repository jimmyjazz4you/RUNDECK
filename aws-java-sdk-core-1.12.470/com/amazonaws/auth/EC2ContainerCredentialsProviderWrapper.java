package com.amazonaws.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EC2ContainerCredentialsProviderWrapper implements AWSCredentialsProvider {
   private static final Log LOG = LogFactory.getLog(EC2ContainerCredentialsProviderWrapper.class);
   private final AWSCredentialsProvider provider = this.initializeProvider();

   private AWSCredentialsProvider initializeProvider() {
      try {
         if (System.getenv("AWS_CONTAINER_CREDENTIALS_RELATIVE_URI") != null) {
            return new ContainerCredentialsProvider(new ContainerCredentialsProvider.ECSCredentialsEndpointProvider());
         } else {
            return (AWSCredentialsProvider)(System.getenv("AWS_CONTAINER_CREDENTIALS_FULL_URI") != null
               ? new ContainerCredentialsProvider(new ContainerCredentialsProvider.FullUriCredentialsEndpointProvider())
               : InstanceProfileCredentialsProvider.getInstance());
         }
      } catch (SecurityException var2) {
         LOG.debug(
            "Security manager did not allow access to the ECS credentials environment variable AWS_CONTAINER_CREDENTIALS_RELATIVE_URIor the container full URI environment variable AWS_CONTAINER_CREDENTIALS_FULL_URI. Please provide access to this environment variable if you want to load credentials from ECS Container."
         );
         return InstanceProfileCredentialsProvider.getInstance();
      }
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.provider.getCredentials();
   }

   @Override
   public void refresh() {
      this.provider.refresh();
   }
}
