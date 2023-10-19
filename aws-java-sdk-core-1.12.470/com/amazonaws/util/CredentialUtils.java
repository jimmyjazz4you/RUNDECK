package com.amazonaws.util;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.RequestConfig;
import com.amazonaws.auth.AWSCredentialsProvider;

public class CredentialUtils {
   public static AWSCredentialsProvider getCredentialsProvider(AmazonWebServiceRequest req, AWSCredentialsProvider base) {
      return req != null && req.getRequestCredentialsProvider() != null ? req.getRequestCredentialsProvider() : base;
   }

   public static AWSCredentialsProvider getCredentialsProvider(RequestConfig requestConfig, AWSCredentialsProvider base) {
      return requestConfig.getCredentialsProvider() != null ? requestConfig.getCredentialsProvider() : base;
   }
}
