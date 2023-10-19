package com.amazonaws.auth.profile.internal;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ProcessCredentialsProvider;

public class ProfileProcessCredentialsProvider implements AWSCredentialsProvider {
   private final ProcessCredentialsProvider delegate;

   public ProfileProcessCredentialsProvider(BasicProfile profile) {
      this.delegate = ProcessCredentialsProvider.builder().withCommand(profile.getCredentialProcess()).build();
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.delegate.getCredentials();
   }

   @Override
   public void refresh() {
      this.delegate.refresh();
   }
}
