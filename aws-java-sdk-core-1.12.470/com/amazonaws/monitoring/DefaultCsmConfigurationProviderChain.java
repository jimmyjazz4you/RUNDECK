package com.amazonaws.monitoring;

public final class DefaultCsmConfigurationProviderChain extends CsmConfigurationProviderChain {
   private static final DefaultCsmConfigurationProviderChain INSTANCE = new DefaultCsmConfigurationProviderChain();

   private DefaultCsmConfigurationProviderChain() {
      super(new EnvironmentVariableCsmConfigurationProvider(), new SystemPropertyCsmConfigurationProvider(), new ProfileCsmConfigurationProvider());
   }

   public static DefaultCsmConfigurationProviderChain getInstance() {
      return INSTANCE;
   }
}
