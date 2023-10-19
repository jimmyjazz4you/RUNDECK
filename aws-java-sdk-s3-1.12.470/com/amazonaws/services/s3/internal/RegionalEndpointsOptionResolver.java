package com.amazonaws.services.s3.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.auth.profile.internal.AwsProfileNameLoader;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ThreadSafe
@SdkInternalApi
public final class RegionalEndpointsOptionResolver {
   private static final Log log = LogFactory.getLog(RegionalEndpointsOptionResolver.class);
   private static final String ENV_VAR = "AWS_S3_US_EAST_1_REGIONAL_ENDPOINT";
   private static final String PROFILE_PROPERTY = "s3_us_east_1_regional_endpoint";
   private final AwsProfileFileLocationProvider configFileLocationProvider;
   private volatile String profileName;
   private volatile ProfilesConfigFile configFile;
   private volatile boolean profileLoadAttempted;

   public RegionalEndpointsOptionResolver() {
      this.configFileLocationProvider = AwsProfileFileLocationProvider.DEFAULT_CONFIG_LOCATION_PROVIDER;
   }

   public RegionalEndpointsOptionResolver(AwsProfileFileLocationProvider configFileLocationProvider) {
      this.configFileLocationProvider = configFileLocationProvider;
   }

   public boolean useRegionalMode() {
      RegionalEndpointsOptionResolver.Option option = this.envVarOption();
      if (option == null) {
         option = this.profileOption();
      }

      return option == RegionalEndpointsOptionResolver.Option.REGIONAL;
   }

   private RegionalEndpointsOptionResolver.Option envVarOption() {
      String val = System.getenv("AWS_S3_US_EAST_1_REGIONAL_ENDPOINT");
      return this.resolveOption(val, String.format("Unexpected value set for %s environment variable: '%s'", "AWS_S3_US_EAST_1_REGIONAL_ENDPOINT", val));
   }

   private synchronized RegionalEndpointsOptionResolver.Option profileOption() {
      String profileName = this.getProfileName();
      BasicProfile profile = this.getProfile(profileName);
      if (profile == null) {
         return null;
      } else {
         String val = profile.getPropertyValue("s3_us_east_1_regional_endpoint");
         return this.resolveOption(
            val, String.format("Unexpected option for '%s' property in profile '%s': %s", "s3_us_east_1_regional_endpoint", profileName, val)
         );
      }
   }

   private RegionalEndpointsOptionResolver.Option resolveOption(String value, String errMsg) {
      if (value == null) {
         return null;
      } else if ("legacy".equalsIgnoreCase(value)) {
         return RegionalEndpointsOptionResolver.Option.LEGACY;
      } else if ("regional".equalsIgnoreCase(value)) {
         return RegionalEndpointsOptionResolver.Option.REGIONAL;
      } else {
         throw new SdkClientException(errMsg);
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
      ProfilesConfigFile profilesConfigFile = this.getProfilesConfigFile();
      return profilesConfigFile != null ? (BasicProfile)profilesConfigFile.getAllBasicProfiles().get(profileName) : null;
   }

   private ProfilesConfigFile getProfilesConfigFile() {
      if (!this.profileLoadAttempted) {
         synchronized(this) {
            if (!this.profileLoadAttempted) {
               File location = null;

               try {
                  location = this.configFileLocationProvider.getLocation();
                  if (location != null) {
                     this.configFile = new ProfilesConfigFile(location);
                  }
               } catch (Exception var9) {
                  if (log.isWarnEnabled()) {
                     log.warn("Unable to load config file " + location, var9);
                  }
               } finally {
                  this.profileLoadAttempted = true;
               }
            }
         }
      }

      return this.configFile;
   }

   private static enum Option {
      LEGACY,
      REGIONAL;
   }
}
