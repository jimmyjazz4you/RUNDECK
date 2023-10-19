package com.amazonaws.auth.profile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.internal.AllProfiles;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.auth.profile.internal.BasicProfileConfigLoader;
import com.amazonaws.auth.profile.internal.Profile;
import com.amazonaws.auth.profile.internal.ProfileAssumeRoleCredentialsProvider;
import com.amazonaws.auth.profile.internal.ProfileProcessCredentialsProvider;
import com.amazonaws.auth.profile.internal.ProfileStaticCredentialsProvider;
import com.amazonaws.auth.profile.internal.securitytoken.ProfileCredentialsService;
import com.amazonaws.auth.profile.internal.securitytoken.STSProfileCredentialsServiceLoader;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import com.amazonaws.util.ValidationUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ProfilesConfigFile {
   @Deprecated
   public static final String AWS_PROFILE_ENVIRONMENT_VARIABLE = "AWS_PROFILE";
   @Deprecated
   public static final String AWS_PROFILE_SYSTEM_PROPERTY = "aws.profile";
   @Deprecated
   public static final String DEFAULT_PROFILE_NAME = "default";
   private final File profileFile;
   private final ProfileCredentialsService profileCredentialsService;
   private final ConcurrentHashMap<String, AWSCredentialsProvider> credentialProviderCache = new ConcurrentHashMap<>();
   private volatile AllProfiles allProfiles;
   private volatile long profileFileLastModified;

   public ProfilesConfigFile() throws SdkClientException {
      this(getCredentialProfilesFile());
   }

   public ProfilesConfigFile(String filePath) {
      this(new File(validateFilePath(filePath)));
   }

   public ProfilesConfigFile(String filePath, ProfileCredentialsService credentialsService) throws SdkClientException {
      this(new File(validateFilePath(filePath)), credentialsService);
   }

   private static String validateFilePath(String filePath) {
      if (filePath == null) {
         throw new IllegalArgumentException("Unable to load AWS profiles: specified file path is null.");
      } else {
         return filePath;
      }
   }

   public ProfilesConfigFile(File file) throws SdkClientException {
      this(file, STSProfileCredentialsServiceLoader.getInstance());
   }

   public ProfilesConfigFile(File file, ProfileCredentialsService credentialsService) throws SdkClientException {
      this.profileFile = ValidationUtils.assertNotNull(file, "profile file");
      this.profileCredentialsService = credentialsService;
      this.profileFileLastModified = file.lastModified();
      this.allProfiles = loadProfiles(this.profileFile);
   }

   public AWSCredentials getCredentials(String profileName) {
      AWSCredentialsProvider provider = this.credentialProviderCache.get(profileName);
      if (provider != null) {
         return provider.getCredentials();
      } else {
         BasicProfile profile = this.allProfiles.getProfile(profileName);
         if (profile == null) {
            throw new IllegalArgumentException("No AWS profile named '" + profileName + "'");
         } else {
            AWSCredentialsProvider newProvider = this.fromProfile(profile);
            this.credentialProviderCache.put(profileName, newProvider);
            return newProvider.getCredentials();
         }
      }
   }

   public void refresh() {
      if (this.profileFile.lastModified() > this.profileFileLastModified) {
         synchronized(this) {
            if (this.profileFile.lastModified() > this.profileFileLastModified) {
               this.allProfiles = loadProfiles(this.profileFile);
               this.profileFileLastModified = this.profileFile.lastModified();
            }
         }
      }

      this.credentialProviderCache.clear();
   }

   public BasicProfile getBasicProfile(String profile) {
      return this.allProfiles.getProfile(profile);
   }

   public Map<String, BasicProfile> getAllBasicProfiles() {
      return this.allProfiles.getProfiles();
   }

   @Deprecated
   public Map<String, Profile> getAllProfiles() {
      Map<String, Profile> legacyProfiles = new HashMap<>();

      for(Entry<String, BasicProfile> entry : this.getAllBasicProfiles().entrySet()) {
         String profileName = entry.getKey();
         legacyProfiles.put(
            profileName, new Profile(profileName, entry.getValue().getProperties(), new StaticCredentialsProvider(this.getCredentials(profileName)))
         );
      }

      return legacyProfiles;
   }

   private static File getCredentialProfilesFile() {
      return AwsProfileFileLocationProvider.DEFAULT_CREDENTIALS_LOCATION_PROVIDER.getLocation();
   }

   private static AllProfiles loadProfiles(File file) {
      return BasicProfileConfigLoader.INSTANCE.loadProfiles(file);
   }

   private AWSCredentialsProvider fromProfile(BasicProfile profile) {
      if (profile.isRoleBasedProfile()) {
         return new ProfileAssumeRoleCredentialsProvider(this.profileCredentialsService, this.allProfiles, profile);
      } else {
         return (AWSCredentialsProvider)(profile.isProcessBasedProfile()
            ? new ProfileProcessCredentialsProvider(profile)
            : new ProfileStaticCredentialsProvider(profile));
      }
   }
}
