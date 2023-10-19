package com.amazonaws.profile.path;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import java.io.File;

@SdkInternalApi
public abstract class AwsDirectoryBasePathProvider implements AwsProfileFileLocationProvider {
   protected final File getAwsDirectory() {
      return new File(this.getHomeDirectory(), ".aws");
   }

   private String getHomeDirectory() {
      String userHome = System.getProperty("user.home");
      if (userHome == null) {
         throw new SdkClientException("Unable to load AWS profiles: 'user.home' System property is not set.");
      } else {
         return userHome;
      }
   }
}
