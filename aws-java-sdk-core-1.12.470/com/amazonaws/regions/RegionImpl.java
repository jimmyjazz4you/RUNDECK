package com.amazonaws.regions;

import com.amazonaws.annotation.SdkInternalApi;
import java.util.Collection;

@SdkInternalApi
public interface RegionImpl {
   String getName();

   String getDomain();

   String getPartition();

   boolean isServiceSupported(String var1);

   String getServiceEndpoint(String var1);

   boolean hasHttpEndpoint(String var1);

   boolean hasHttpsEndpoint(String var1);

   Collection<String> getAvailableEndpoints();
}
