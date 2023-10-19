package com.amazonaws.auth.profile.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import com.amazonaws.util.ValidationUtils;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public final class BasicProfileConfigFileLoader {
   public static final BasicProfileConfigFileLoader INSTANCE = new BasicProfileConfigFileLoader();
   private static final Log log = LogFactory.getLog(BasicProfileConfigFileLoader.class);
   private final AwsProfileFileLocationProvider configFileLocationProvider;
   private volatile String profileName;
   private volatile ProfilesConfigFile configFile;
   private volatile boolean profileLoadAttempted;

   private BasicProfileConfigFileLoader() {
      this.configFileLocationProvider = AwsProfileFileLocationProvider.DEFAULT_CONFIG_LOCATION_PROVIDER;
   }

   @SdkTestInternalApi
   public BasicProfileConfigFileLoader(AwsProfileFileLocationProvider configFileLocationProvider) {
      this.configFileLocationProvider = ValidationUtils.assertNotNull(configFileLocationProvider, "configFileLocationProvider");
   }

   public BasicProfile getProfile() {
      ProfilesConfigFile profilesConfigFile = this.getProfilesConfigFile();
      return profilesConfigFile != null ? profilesConfigFile.getBasicProfile(this.getProfileName()) : null;
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
}
