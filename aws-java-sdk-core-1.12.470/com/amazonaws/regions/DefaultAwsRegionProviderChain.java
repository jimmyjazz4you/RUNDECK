package com.amazonaws.regions;

public class DefaultAwsRegionProviderChain extends AwsRegionProviderChain {
   public DefaultAwsRegionProviderChain() {
      super(new AwsEnvVarOverrideRegionProvider(), new AwsSystemPropertyRegionProvider(), new AwsProfileRegionProvider(), new InstanceMetadataRegionProvider());
   }
}
