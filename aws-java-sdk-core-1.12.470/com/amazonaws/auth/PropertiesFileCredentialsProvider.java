package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import java.io.File;
import java.io.IOException;

public class PropertiesFileCredentialsProvider implements AWSCredentialsProvider {
   private final String credentialsFilePath;

   public PropertiesFileCredentialsProvider(String credentialsFilePath) {
      if (credentialsFilePath == null) {
         throw new IllegalArgumentException("Credentials file path cannot be null");
      } else {
         this.credentialsFilePath = credentialsFilePath;
      }
   }

   @Override
   public AWSCredentials getCredentials() {
      try {
         return new PropertiesCredentials(new File(this.credentialsFilePath));
      } catch (IOException var2) {
         throw new SdkClientException("Unable to load AWS credentials from the " + this.credentialsFilePath + " file", var2);
      }
   }

   @Override
   public void refresh() {
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + "(" + this.credentialsFilePath + ")";
   }
}
