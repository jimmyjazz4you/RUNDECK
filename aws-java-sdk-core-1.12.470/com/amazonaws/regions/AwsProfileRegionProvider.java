package com.amazonaws.regions;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.auth.profile.internal.AllProfiles;
import com.amazonaws.auth.profile.internal.AwsProfileNameLoader;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.auth.profile.internal.BasicProfileConfigLoader;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import com.amazonaws.util.StringUtils;
import java.io.File;

public class AwsProfileRegionProvider extends AwsRegionProvider {
   private final String profileName;
   private final AwsProfileFileLocationProvider locationProvider;
   private final BasicProfileConfigLoader profileConfigLoader;

   public AwsProfileRegionProvider() {
      this(AwsProfileNameLoader.INSTANCE.loadProfileName());
   }

   public AwsProfileRegionProvider(String profileName) {
      this(profileName, AwsProfileFileLocationProvider.DEFAULT_CONFIG_LOCATION_PROVIDER, BasicProfileConfigLoader.INSTANCE);
   }

   @SdkTestInternalApi
   AwsProfileRegionProvider(String profileName, AwsProfileFileLocationProvider locationProvider, BasicProfileConfigLoader configLoader) {
      this.profileName = profileName;
      this.locationProvider = locationProvider;
      this.profileConfigLoader = configLoader;
   }

   @Override
   public String getRegion() throws SdkClientException {
      File configFile = this.locationProvider.getLocation();
      if (configFile != null && configFile.exists()) {
         BasicProfile profile = this.loadProfile(configFile);
         if (profile != null && !StringUtils.isNullOrEmpty(profile.getRegion())) {
            return profile.getRegion();
         }
      }

      return null;
   }

   private BasicProfile loadProfile(File configFile) {
      AllProfiles allProfiles = this.profileConfigLoader.loadProfiles(configFile);
      return allProfiles.getProfile(this.profileName);
   }
}
