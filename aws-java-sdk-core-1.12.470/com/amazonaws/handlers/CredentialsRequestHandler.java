package com.amazonaws.handlers;

import com.amazonaws.auth.AWSCredentials;

@Deprecated
public abstract class CredentialsRequestHandler extends RequestHandler2 {
   protected AWSCredentials awsCredentials;

   public void setCredentials(AWSCredentials awsCredentials) {
      this.awsCredentials = awsCredentials;
   }
}
