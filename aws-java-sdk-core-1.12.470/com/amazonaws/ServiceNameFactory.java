package com.amazonaws;

import com.amazonaws.internal.config.HttpClientConfig;
import com.amazonaws.internal.config.InternalConfig;

enum ServiceNameFactory {
   static String getServiceName(String httpClientName) {
      InternalConfig config = InternalConfig.Factory.getInternalConfig();
      HttpClientConfig clientConfig = config.getHttpClientConfig(httpClientName);
      return clientConfig == null ? null : clientConfig.getServiceName();
   }

   static String getServiceNameInRegionMetadata(String httpClientName) {
      InternalConfig config = InternalConfig.Factory.getInternalConfig();
      HttpClientConfig clientConfig = config.getHttpClientConfig(httpClientName);
      return clientConfig == null ? null : clientConfig.getRegionMetadataServiceName();
   }
}
