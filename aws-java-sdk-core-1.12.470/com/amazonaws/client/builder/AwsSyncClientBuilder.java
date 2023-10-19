package com.amazonaws.client.builder;

import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.regions.AwsRegionProvider;

@NotThreadSafe
@SdkProtectedApi
public abstract class AwsSyncClientBuilder<Subclass extends AwsSyncClientBuilder, TypeToBuild> extends AwsClientBuilder<Subclass, TypeToBuild> {
   protected AwsSyncClientBuilder(ClientConfigurationFactory clientConfigFactory) {
      super(clientConfigFactory);
   }

   @SdkTestInternalApi
   protected AwsSyncClientBuilder(ClientConfigurationFactory clientConfigFactory, AwsRegionProvider regionProvider) {
      super(clientConfigFactory, regionProvider);
   }

   @Override
   public final TypeToBuild build() {
      return this.configureMutableProperties(this.build(this.getSyncClientParams()));
   }

   protected abstract TypeToBuild build(AwsSyncClientParams var1);
}
