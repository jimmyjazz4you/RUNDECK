package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.internal.ExceptionUtils;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AWSCredentialsProviderChain implements AWSCredentialsProvider {
   private static final Log log = LogFactory.getLog(AWSCredentialsProviderChain.class);
   private final List<AWSCredentialsProvider> credentialsProviders = new LinkedList<>();
   private boolean reuseLastProvider = true;
   private AWSCredentialsProvider lastUsedProvider;

   public AWSCredentialsProviderChain(List<? extends AWSCredentialsProvider> credentialsProviders) {
      if (credentialsProviders != null && credentialsProviders.size() != 0) {
         this.credentialsProviders.addAll(credentialsProviders);
      } else {
         throw new IllegalArgumentException("No credential providers specified");
      }
   }

   public AWSCredentialsProviderChain(AWSCredentialsProvider... credentialsProviders) {
      if (credentialsProviders != null && credentialsProviders.length != 0) {
         for(AWSCredentialsProvider provider : credentialsProviders) {
            this.credentialsProviders.add(provider);
         }
      } else {
         throw new IllegalArgumentException("No credential providers specified");
      }
   }

   public boolean getReuseLastProvider() {
      return this.reuseLastProvider;
   }

   public void setReuseLastProvider(boolean b) {
      this.reuseLastProvider = b;
   }

   @Override
   public AWSCredentials getCredentials() {
      if (this.reuseLastProvider && this.lastUsedProvider != null) {
         return this.lastUsedProvider.getCredentials();
      } else {
         List<String> exceptionMessages = null;

         for(AWSCredentialsProvider provider : this.credentialsProviders) {
            try {
               AWSCredentials credentials = provider.getCredentials();
               if (credentials.getAWSAccessKeyId() != null && credentials.getAWSSecretKey() != null) {
                  log.debug("Loading credentials from " + provider.toString());
                  this.lastUsedProvider = provider;
                  return credentials;
               }
            } catch (Exception var6) {
               String message;
               if (log.isDebugEnabled()) {
                  message = provider + ": " + ExceptionUtils.exceptionStackTrace(var6);
                  log.debug("Unable to load credentials from " + message);
               } else {
                  message = provider + ": " + var6.getMessage();
               }

               if (exceptionMessages == null) {
                  exceptionMessages = new LinkedList<>();
               }

               exceptionMessages.add(message);
            }
         }

         throw new SdkClientException("Unable to load AWS credentials from any provider in the chain: " + exceptionMessages);
      }
   }

   @Override
   public void refresh() {
      for(AWSCredentialsProvider provider : this.credentialsProviders) {
         provider.refresh();
      }
   }
}
