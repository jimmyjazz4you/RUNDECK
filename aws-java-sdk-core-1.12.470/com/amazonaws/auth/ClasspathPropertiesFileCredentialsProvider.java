package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import java.io.IOException;
import java.io.InputStream;

public class ClasspathPropertiesFileCredentialsProvider implements AWSCredentialsProvider {
   private static String DEFAULT_PROPERTIES_FILE = "AwsCredentials.properties";
   private final String credentialsFilePath;

   public ClasspathPropertiesFileCredentialsProvider() {
      this(DEFAULT_PROPERTIES_FILE);
   }

   public ClasspathPropertiesFileCredentialsProvider(String credentialsFilePath) {
      if (credentialsFilePath == null) {
         throw new IllegalArgumentException("Credentials file path cannot be null");
      } else {
         if (!credentialsFilePath.startsWith("/")) {
            this.credentialsFilePath = "/" + credentialsFilePath;
         } else {
            this.credentialsFilePath = credentialsFilePath;
         }
      }
   }

   @Override
   public AWSCredentials getCredentials() {
      InputStream inputStream = this.getClass().getResourceAsStream(this.credentialsFilePath);
      if (inputStream == null) {
         throw new SdkClientException("Unable to load AWS credentials from the " + this.credentialsFilePath + " file on the classpath");
      } else {
         try {
            return new PropertiesCredentials(inputStream);
         } catch (IOException var3) {
            throw new SdkClientException("Unable to load AWS credentials from the " + this.credentialsFilePath + " file on the classpath", var3);
         }
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
