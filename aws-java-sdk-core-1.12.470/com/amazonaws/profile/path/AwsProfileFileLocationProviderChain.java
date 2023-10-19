package com.amazonaws.profile.path;

import com.amazonaws.annotation.SdkInternalApi;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SdkInternalApi
public class AwsProfileFileLocationProviderChain implements AwsProfileFileLocationProvider {
   private final List<AwsProfileFileLocationProvider> providers = new ArrayList<>();

   public AwsProfileFileLocationProviderChain(AwsProfileFileLocationProvider... providers) {
      Collections.addAll(this.providers, providers);
   }

   @Override
   public File getLocation() {
      for(AwsProfileFileLocationProvider provider : this.providers) {
         File path = provider.getLocation();
         if (path != null) {
            return path;
         }
      }

      return null;
   }
}
