package com.amazonaws;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public class ClientConfigurationFactory {
   public final ClientConfiguration getConfig() {
      return SDKGlobalConfiguration.isInRegionOptimizedModeEnabled() ? this.getInRegionOptimizedConfig() : this.getDefaultConfig();
   }

   protected ClientConfiguration getDefaultConfig() {
      return new ClientConfiguration();
   }

   protected ClientConfiguration getInRegionOptimizedConfig() {
      return new ClientConfiguration().withConnectionTimeout(1000);
   }
}
