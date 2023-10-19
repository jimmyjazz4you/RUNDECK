package com.amazonaws.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesCredentials implements AWSCredentials {
   private final String accessKey;
   private final String secretAccessKey;

   public PropertiesCredentials(File file) throws FileNotFoundException, IOException, IllegalArgumentException {
      if (!file.exists()) {
         throw new FileNotFoundException("File doesn't exist:  " + file.getAbsolutePath());
      } else {
         FileInputStream stream = new FileInputStream(file);

         try {
            Properties accountProperties = new Properties();
            accountProperties.load(stream);
            if (accountProperties.getProperty("accessKey") == null || accountProperties.getProperty("secretKey") == null) {
               throw new IllegalArgumentException(
                  "The specified file (" + file.getAbsolutePath() + ") doesn't contain the expected properties 'accessKey' and 'secretKey'."
               );
            }

            this.accessKey = accountProperties.getProperty("accessKey");
            this.secretAccessKey = accountProperties.getProperty("secretKey");
         } finally {
            try {
               stream.close();
            } catch (IOException var9) {
            }
         }
      }
   }

   public PropertiesCredentials(InputStream inputStream) throws IOException {
      Properties accountProperties = new Properties();

      try {
         accountProperties.load(inputStream);
      } finally {
         try {
            inputStream.close();
         } catch (Exception var9) {
         }
      }

      if (accountProperties.getProperty("accessKey") != null && accountProperties.getProperty("secretKey") != null) {
         this.accessKey = accountProperties.getProperty("accessKey");
         this.secretAccessKey = accountProperties.getProperty("secretKey");
      } else {
         throw new IllegalArgumentException("The specified properties data doesn't contain the expected properties 'accessKey' and 'secretKey'.");
      }
   }

   @Override
   public String getAWSAccessKeyId() {
      return this.accessKey;
   }

   @Override
   public String getAWSSecretKey() {
      return this.secretAccessKey;
   }
}
