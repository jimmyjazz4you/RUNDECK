package com.amazonaws.internal;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

@Deprecated
public class StaticCredentialsProvider implements AWSCredentialsProvider {
   private final AWSCredentials credentials;

   public StaticCredentialsProvider(AWSCredentials credentials) {
      this.credentials = credentials;
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.credentials;
   }

   @Override
   public void refresh() {
   }
}
