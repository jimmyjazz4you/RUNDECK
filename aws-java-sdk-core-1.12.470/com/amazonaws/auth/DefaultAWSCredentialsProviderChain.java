package com.amazonaws.auth;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class DefaultAWSCredentialsProviderChain extends AWSCredentialsProviderChain {
   private static final DefaultAWSCredentialsProviderChain INSTANCE = new DefaultAWSCredentialsProviderChain();

   public DefaultAWSCredentialsProviderChain() {
      super(
         new EnvironmentVariableCredentialsProvider(),
         new SystemPropertiesCredentialsProvider(),
         WebIdentityTokenCredentialsProvider.create(),
         new ProfileCredentialsProvider(),
         new EC2ContainerCredentialsProviderWrapper()
      );
   }

   public static DefaultAWSCredentialsProviderChain getInstance() {
      return INSTANCE;
   }
}
