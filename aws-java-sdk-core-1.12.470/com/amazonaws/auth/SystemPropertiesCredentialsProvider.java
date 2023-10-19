package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.util.StringUtils;

public class SystemPropertiesCredentialsProvider implements AWSCredentialsProvider {
   @Override
   public AWSCredentials getCredentials() {
      String accessKey = StringUtils.trim(System.getProperty("aws.accessKeyId"));
      String secretKey = StringUtils.trim(System.getProperty("aws.secretKey"));
      String sessionToken = StringUtils.trim(System.getProperty("aws.sessionToken"));
      if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
         throw new SdkClientException("Unable to load AWS credentials from Java system properties (aws.accessKeyId and aws.secretKey)");
      } else {
         return (AWSCredentials)(StringUtils.isNullOrEmpty(sessionToken)
            ? new BasicAWSCredentials(accessKey, secretKey)
            : new BasicSessionCredentials(accessKey, secretKey, sessionToken));
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
