package com.amazonaws.auth.profile.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.util.StringUtils;

@SdkInternalApi
@Immutable
public class ProfileStaticCredentialsProvider implements AWSCredentialsProvider {
   private final BasicProfile profile;
   private final AWSCredentialsProvider credentialsProvider;

   public ProfileStaticCredentialsProvider(BasicProfile profile) {
      this.profile = profile;
      this.credentialsProvider = new StaticCredentialsProvider(this.fromStaticCredentials());
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.credentialsProvider.getCredentials();
   }

   @Override
   public void refresh() {
   }

   private AWSCredentials fromStaticCredentials() {
      if (StringUtils.isNullOrEmpty(this.profile.getAwsAccessIdKey())) {
         throw new SdkClientException(
            String.format("Unable to load credentials into profile [%s]: AWS Access Key ID is not specified.", this.profile.getProfileName())
         );
      } else if (StringUtils.isNullOrEmpty(this.profile.getAwsSecretAccessKey())) {
         throw new SdkClientException(
            String.format("Unable to load credentials into profile [%s]: AWS Secret Access Key is not specified.", this.profile.getAwsSecretAccessKey())
         );
      } else if (this.profile.getAwsSessionToken() == null) {
         return new BasicAWSCredentials(this.profile.getAwsAccessIdKey(), this.profile.getAwsSecretAccessKey());
      } else if (this.profile.getAwsSessionToken().isEmpty()) {
         throw new SdkClientException(String.format("Unable to load credentials into profile [%s]: AWS Session Token is empty.", this.profile.getProfileName()));
      } else {
         return new BasicSessionCredentials(this.profile.getAwsAccessIdKey(), this.profile.getAwsSecretAccessKey(), this.profile.getAwsSessionToken());
      }
   }
}
