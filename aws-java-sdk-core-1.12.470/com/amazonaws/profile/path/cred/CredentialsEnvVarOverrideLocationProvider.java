package com.amazonaws.profile.path.cred;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;
import java.io.File;

@SdkInternalApi
public class CredentialsEnvVarOverrideLocationProvider implements AwsProfileFileLocationProvider {
   private static final String CREDENTIAL_PROFILES_FILE_ENVIRONMENT_VARIABLE = "AWS_CREDENTIAL_PROFILES_FILE";

   @Override
   public File getLocation() {
      String credentialProfilesFileOverride = System.getenv("AWS_CREDENTIAL_PROFILES_FILE");
      return credentialProfilesFileOverride == null ? null : new File(credentialProfilesFileOverride);
   }
}
