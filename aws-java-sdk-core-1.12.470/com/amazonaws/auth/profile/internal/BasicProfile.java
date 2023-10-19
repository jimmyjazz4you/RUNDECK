package com.amazonaws.auth.profile.internal;

import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkInternalApi;
import java.util.Collections;
import java.util.Map;

@Immutable
@SdkInternalApi
public class BasicProfile {
   private final String profileName;
   private final Map<String, String> properties;

   public BasicProfile(String profileName, Map<String, String> properties) {
      this.profileName = profileName;
      this.properties = properties;
   }

   public String getProfileName() {
      return this.profileName;
   }

   public Map<String, String> getProperties() {
      return Collections.unmodifiableMap(this.properties);
   }

   public String getPropertyValue(String propertyName) {
      return this.getProperties().get(propertyName);
   }

   public String getAwsAccessIdKey() {
      return this.getPropertyValue("aws_access_key_id");
   }

   public String getAwsSecretAccessKey() {
      return this.getPropertyValue("aws_secret_access_key");
   }

   public String getAwsSessionToken() {
      return this.getPropertyValue("aws_session_token");
   }

   public String getRoleArn() {
      return this.getPropertyValue("role_arn");
   }

   public String getRoleSourceProfile() {
      return this.getPropertyValue("source_profile");
   }

   public String getRoleSessionName() {
      return this.getPropertyValue("role_session_name");
   }

   public String getRoleExternalId() {
      return this.getPropertyValue("external_id");
   }

   public String getRegion() {
      return this.getPropertyValue("region");
   }

   public String getEndpointDiscovery() {
      return this.getPropertyValue("aws_enable_endpoint_discovery");
   }

   public String getCredentialProcess() {
      return this.getPropertyValue("credential_process");
   }

   public String getWebIdentityTokenFilePath() {
      return this.getPropertyValue("web_identity_token_file");
   }

   public boolean isRoleBasedProfile() {
      return this.getRoleArn() != null;
   }

   public boolean isProcessBasedProfile() {
      return this.getCredentialProcess() != null;
   }
}
