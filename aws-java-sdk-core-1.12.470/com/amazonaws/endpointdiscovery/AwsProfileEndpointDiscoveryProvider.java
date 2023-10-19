package com.amazonaws.endpointdiscovery;

import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.auth.profile.internal.AllProfiles;
import com.amazonaws.auth.profile.internal.AwsProfileNameLoader;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.auth.profile.internal.BasicProfileConfigLoader;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import com.amazonaws.util.StringUtils;
import java.io.File;

public class AwsProfileEndpointDiscoveryProvider implements EndpointDiscoveryProvider {
   private final String profileName;
   private final AwsProfileFileLocationProvider locationProvider;
   private final BasicProfileConfigLoader profileConfigLoader;

   public AwsProfileEndpointDiscoveryProvider() {
      this(AwsProfileNameLoader.INSTANCE.loadProfileName());
   }

   public AwsProfileEndpointDiscoveryProvider(String profileName) {
      this(profileName, AwsProfileFileLocationProvider.DEFAULT_CONFIG_LOCATION_PROVIDER, BasicProfileConfigLoader.INSTANCE);
   }

   @SdkTestInternalApi
   AwsProfileEndpointDiscoveryProvider(String profileName, AwsProfileFileLocationProvider locationProvider, BasicProfileConfigLoader configLoader) {
      this.profileName = profileName;
      this.locationProvider = locationProvider;
      this.profileConfigLoader = configLoader;
   }

   @Override
   public Boolean endpointDiscoveryEnabled() {
      Boolean endpointDiscoveryEnabled = null;
      File configFile = this.locationProvider.getLocation();
      if (configFile != null && configFile.exists()) {
         BasicProfile profile = this.loadProfile(configFile);
         if (profile != null && !StringUtils.isNullOrEmpty(profile.getEndpointDiscovery())) {
            try {
               endpointDiscoveryEnabled = Boolean.parseBoolean(profile.getEndpointDiscovery());
            } catch (Exception var5) {
               throw new RuntimeException("Unable to parse value for aws_enable_endpoint_discovery");
            }
         }
      }

      return endpointDiscoveryEnabled;
   }

   private BasicProfile loadProfile(File configFile) {
      AllProfiles allProfiles = this.profileConfigLoader.loadProfiles(configFile);
      return allProfiles.getProfile(this.profileName);
   }
}
