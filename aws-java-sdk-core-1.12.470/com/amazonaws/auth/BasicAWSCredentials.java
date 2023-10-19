package com.amazonaws.auth;

public class BasicAWSCredentials implements AWSCredentials {
   private final String accessKey;
   private final String secretKey;

   public BasicAWSCredentials(String accessKey, String secretKey) {
      if (accessKey == null) {
         throw new IllegalArgumentException("Access key cannot be null.");
      } else if (secretKey == null) {
         throw new IllegalArgumentException("Secret key cannot be null.");
      } else {
         this.accessKey = accessKey;
         this.secretKey = secretKey;
      }
   }

   @Override
   public String getAWSAccessKeyId() {
      return this.accessKey;
   }

   @Override
   public String getAWSSecretKey() {
      return this.secretKey;
   }
}
