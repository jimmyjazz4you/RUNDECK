package com.amazonaws.auth;

public class BasicSessionCredentials implements AWSSessionCredentials {
   private final String awsAccessKey;
   private final String awsSecretKey;
   private final String sessionToken;

   public BasicSessionCredentials(String awsAccessKey, String awsSecretKey, String sessionToken) {
      this.awsAccessKey = awsAccessKey;
      this.awsSecretKey = awsSecretKey;
      this.sessionToken = sessionToken;
   }

   @Override
   public String getAWSAccessKeyId() {
      return this.awsAccessKey;
   }

   @Override
   public String getAWSSecretKey() {
      return this.awsSecretKey;
   }

   @Override
   public String getSessionToken() {
      return this.sessionToken;
   }
}
