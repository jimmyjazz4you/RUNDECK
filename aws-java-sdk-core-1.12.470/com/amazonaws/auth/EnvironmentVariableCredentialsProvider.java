package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.util.StringUtils;

public class EnvironmentVariableCredentialsProvider implements AWSCredentialsProvider {
   @Override
   public AWSCredentials getCredentials() {
      String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
      if (accessKey == null) {
         accessKey = System.getenv("AWS_ACCESS_KEY");
      }

      String secretKey = System.getenv("AWS_SECRET_KEY");
      if (secretKey == null) {
         secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
      }

      accessKey = StringUtils.trim(accessKey);
      secretKey = StringUtils.trim(secretKey);
      if (!StringUtils.isNullOrEmpty(accessKey) && !StringUtils.isNullOrEmpty(secretKey)) {
         String sessionToken = StringUtils.trim(System.getenv("AWS_SESSION_TOKEN"));
         return (AWSCredentials)(StringUtils.isNullOrEmpty(sessionToken)
            ? new BasicAWSCredentials(accessKey, secretKey)
            : new BasicSessionCredentials(accessKey, secretKey, sessionToken));
      } else {
         throw new SdkClientException(
            "Unable to load AWS credentials from environment variables (AWS_ACCESS_KEY_ID (or AWS_ACCESS_KEY) and AWS_SECRET_KEY (or AWS_SECRET_ACCESS_KEY))"
         );
      }
   }

   @Override
   public void refresh() {
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName();
   }
}
