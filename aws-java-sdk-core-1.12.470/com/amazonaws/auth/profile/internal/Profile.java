package com.amazonaws.auth.profile.internal;

import com.amazonaws.annotation.Immutable;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.profile.internal.securitytoken.RoleInfo;
import com.amazonaws.internal.StaticCredentialsProvider;
import java.util.LinkedHashMap;
import java.util.Map;

@Deprecated
@Immutable
public class Profile {
   private final String profileName;
   private final Map<String, String> properties;
   private final AWSCredentialsProvider awsCredentials;

   public Profile(String profileName, AWSCredentials awsCredentials) {
      Map<String, String> properties = new LinkedHashMap<>();
      properties.put("aws_access_key_id", awsCredentials.getAWSAccessKeyId());
      properties.put("aws_secret_access_key", awsCredentials.getAWSSecretKey());
      if (awsCredentials instanceof AWSSessionCredentials) {
         AWSSessionCredentials sessionCred = (AWSSessionCredentials)awsCredentials;
         properties.put("aws_session_token", sessionCred.getSessionToken());
      }

      this.profileName = profileName;
      this.properties = properties;
      this.awsCredentials = new StaticCredentialsProvider(awsCredentials);
   }

   public Profile(String profileName, String sourceProfile, AWSCredentialsProvider awsCredentials, RoleInfo roleInfo) {
      Map<String, String> properties = new LinkedHashMap<>();
      properties.put("source_profile", sourceProfile);
      properties.put("role_arn", roleInfo.getRoleArn());
      if (roleInfo.getRoleSessionName() != null) {
         properties.put("role_session_name", roleInfo.getRoleSessionName());
      }

      if (roleInfo.getExternalId() != null) {
         properties.put("external_id", roleInfo.getExternalId());
      }

      this.profileName = profileName;
      this.properties = properties;
      this.awsCredentials = awsCredentials;
   }

   public Profile(String profileName, Map<String, String> properties, AWSCredentialsProvider awsCredentials) {
      this.profileName = profileName;
      this.properties = properties;
      this.awsCredentials = awsCredentials;
   }

   public String getProfileName() {
      return this.profileName;
   }

   public AWSCredentials getCredentials() {
      return this.awsCredentials.getCredentials();
   }

   public Map<String, String> getProperties() {
      return new LinkedHashMap<>(this.properties);
   }

   public String getPropertyValue(String propertyName) {
      return this.getProperties().get(propertyName);
   }
}
