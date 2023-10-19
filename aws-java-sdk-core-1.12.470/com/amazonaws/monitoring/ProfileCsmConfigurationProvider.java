package com.amazonaws.monitoring;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.auth.profile.internal.AwsProfileNameLoader;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;

@ThreadSafe
public final class ProfileCsmConfigurationProvider implements CsmConfigurationProvider {
   public static final String CSM_ENABLED_PROPERTY = "csm_enabled";
   public static final String CSM_HOST_PROPERTY = "csm_host";
   public static final String CSM_PORT_PROPERTY = "csm_port";
   public static final String CSM_CLIENT_ID_PROPERTY = "csm_client_id";
   private final AwsProfileFileLocationProvider configFileLocationProvider;
   private volatile String profileName;
   private volatile ProfilesConfigFile configFile;

   public ProfileCsmConfigurationProvider() {
      this(null, AwsProfileFileLocationProvider.DEFAULT_CONFIG_LOCATION_PROVIDER);
   }

   public ProfileCsmConfigurationProvider(String profileName) {
      this(profileName, AwsProfileFileLocationProvider.DEFAULT_CONFIG_LOCATION_PROVIDER);
   }

   public ProfileCsmConfigurationProvider(String profileName, AwsProfileFileLocationProvider configFileLocationProvider) {
      this.profileName = profileName;
      this.configFileLocationProvider = configFileLocationProvider;
   }

   @Override
   public CsmConfiguration getConfiguration() {
      String profileName = this.getProfileName();
      BasicProfile profile = this.getProfile(profileName);
      if (profile == null) {
         throw new SdkClientException(String.format("Could not find the '%s' profile!", profileName));
      } else {
         String enabled = profile.getPropertyValue("csm_enabled");
         if (enabled == null) {
            throw new SdkClientException(String.format("The '%s' profile does not define all the required properties!", profileName));
         } else {
            String host = profile.getPropertyValue("csm_host");
            host = host == null ? "127.0.0.1" : host;
            String port = profile.getPropertyValue("csm_port");
            String clientId = profile.getPropertyValue("csm_client_id");
            clientId = clientId == null ? "" : clientId;

            try {
               int portNumber = port == null ? 31000 : Integer.parseInt(port);
               return CsmConfiguration.builder().withEnabled(Boolean.parseBoolean(enabled)).withHost(host).withPort(portNumber).withClientId(clientId).build();
            } catch (Exception var8) {
               throw new SdkClientException(String.format("Unable to load configuration from the '%s' profile!", profileName), var8);
            }
         }
      }
   }

   private String getProfileName() {
      if (this.profileName == null) {
         synchronized(this) {
            if (this.profileName == null) {
               this.profileName = AwsProfileNameLoader.INSTANCE.loadProfileName();
            }
         }
      }

      return this.profileName;
   }

   private synchronized BasicProfile getProfile(String profileName) {
      return this.getProfilesConfigFile().getBasicProfile(profileName);
   }

   private ProfilesConfigFile getProfilesConfigFile() {
      if (this.configFile == null) {
         synchronized(this) {
            if (this.configFile == null) {
               try {
                  this.configFile = new ProfilesConfigFile(this.configFileLocationProvider.getLocation());
               } catch (Exception var4) {
                  throw new SdkClientException("Unable to load config file", var4);
               }
            }
         }
      }

      return this.configFile;
   }
}
