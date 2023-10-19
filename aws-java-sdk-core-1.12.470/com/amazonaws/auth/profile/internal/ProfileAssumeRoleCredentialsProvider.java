package com.amazonaws.auth.profile.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.internal.securitytoken.ProfileCredentialsService;
import com.amazonaws.auth.profile.internal.securitytoken.RoleInfo;
import com.amazonaws.util.StringUtils;

@SdkInternalApi
@Immutable
public class ProfileAssumeRoleCredentialsProvider implements AWSCredentialsProvider {
   private final AllProfiles allProfiles;
   private final BasicProfile profile;
   private final ProfileCredentialsService profileCredentialsService;
   private final AWSCredentialsProvider assumeRoleCredentialsProvider;

   public ProfileAssumeRoleCredentialsProvider(ProfileCredentialsService profileCredentialsService, AllProfiles allProfiles, BasicProfile profile) {
      this.allProfiles = allProfiles;
      this.profile = profile;
      this.profileCredentialsService = profileCredentialsService;
      this.assumeRoleCredentialsProvider = this.fromAssumeRole();
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.assumeRoleCredentialsProvider.getCredentials();
   }

   @Override
   public void refresh() {
   }

   private AWSCredentialsProvider fromAssumeRole() {
      if (StringUtils.isNullOrEmpty(this.profile.getRoleSourceProfile())) {
         throw new SdkClientException(
            String.format("Unable to load credentials from profile [%s]: Source profile name is not specified", this.profile.getProfileName())
         );
      } else {
         BasicProfile sourceProfile = this.allProfiles.getProfile(this.profile.getRoleSourceProfile());
         if (sourceProfile == null) {
            throw new SdkClientException(
               String.format(
                  "Unable to load source profile [%s]: Source profile was not found [%s]", this.profile.getProfileName(), this.profile.getRoleSourceProfile()
               )
            );
         } else {
            AWSCredentials sourceCredentials = new ProfileStaticCredentialsProvider(sourceProfile).getCredentials();
            String roleSessionName = this.profile.getRoleSessionName() == null
               ? "aws-sdk-java-" + System.currentTimeMillis()
               : this.profile.getRoleSessionName();
            RoleInfo roleInfo = new RoleInfo()
               .withRoleArn(this.profile.getRoleArn())
               .withRoleSessionName(roleSessionName)
               .withExternalId(this.profile.getRoleExternalId())
               .withLongLivedCredentials(sourceCredentials)
               .withWebIdentityTokenFilePath(this.profile.getWebIdentityTokenFilePath());
            return this.profileCredentialsService.getAssumeRoleCredentialsProvider(roleInfo);
         }
      }
   }
}
