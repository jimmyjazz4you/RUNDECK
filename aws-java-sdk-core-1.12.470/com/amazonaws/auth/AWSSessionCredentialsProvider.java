package com.amazonaws.auth;

public interface AWSSessionCredentialsProvider extends AWSCredentialsProvider {
   AWSSessionCredentials getCredentials();
}
