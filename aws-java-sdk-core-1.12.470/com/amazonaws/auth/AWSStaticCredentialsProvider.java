package com.amazonaws.auth;

import com.amazonaws.util.ValidationUtils;

public class AWSStaticCredentialsProvider implements AWSCredentialsProvider {
   private final AWSCredentials credentials;

   public AWSStaticCredentialsProvider(AWSCredentials credentials) {
      this.credentials = ValidationUtils.assertNotNull(credentials, "credentials");
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.credentials;
   }

   @Override
   public void refresh() {
   }
}
