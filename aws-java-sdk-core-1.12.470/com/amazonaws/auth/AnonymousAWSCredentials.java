package com.amazonaws.auth;

public class AnonymousAWSCredentials implements AWSCredentials {
   @Override
   public String getAWSAccessKeyId() {
      return null;
   }

   @Override
   public String getAWSSecretKey() {
      return null;
   }
}
