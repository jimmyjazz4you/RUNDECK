package com.amazonaws.services.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class S3CredentialsProviderChain extends DefaultAWSCredentialsProviderChain {
   private static Log LOG = LogFactory.getLog(S3CredentialsProviderChain.class);

   public AWSCredentials getCredentials() {
      try {
         return super.getCredentials();
      } catch (AmazonClientException var2) {
         LOG.debug("No credentials available; falling back to anonymous access");
         return new AnonymousAWSCredentials();
      }
   }
}
