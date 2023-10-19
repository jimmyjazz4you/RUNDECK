package com.amazonaws.monitoring;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.ThreadSafe;

@ThreadSafe
public final class SystemPropertyCsmConfigurationProvider implements CsmConfigurationProvider {
   @Override
   public CsmConfiguration getConfiguration() throws SdkClientException {
      String enabled = System.getProperty("com.amazonaws.sdk.csm.enabled");
      if (enabled == null) {
         throw new SdkClientException("Unable to load Client Side Monitoring configurations from system properties variables!");
      } else {
         String host = System.getProperty("com.amazonaws.sdk.csm.host", "127.0.0.1");
         String port = System.getProperty("com.amazonaws.sdk.csm.port");
         String clientId = System.getProperty("com.amazonaws.sdk.csm.clientId", "");

         try {
            int portNumber = port == null ? 31000 : Integer.parseInt(port);
            return CsmConfiguration.builder().withEnabled(Boolean.parseBoolean(enabled)).withHost(host).withPort(portNumber).withClientId(clientId).build();
         } catch (Exception var6) {
            throw new SdkClientException("Unable to load Client Side Monitoring configurations from system properties variables!", var6);
         }
      }
   }
}
