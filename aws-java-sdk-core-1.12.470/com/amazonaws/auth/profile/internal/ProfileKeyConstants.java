package com.amazonaws.auth.profile.internal;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public class ProfileKeyConstants {
   public static final String AWS_ACCESS_KEY_ID = "aws_access_key_id";
   public static final String AWS_SECRET_ACCESS_KEY = "aws_secret_access_key";
   public static final String AWS_SESSION_TOKEN = "aws_session_token";
   public static final String ROLE_ARN = "role_arn";
   public static final String ROLE_SESSION_NAME = "role_session_name";
   public static final String EXTERNAL_ID = "external_id";
   public static final String SOURCE_PROFILE = "source_profile";
   public static final String REGION = "region";
   public static final String ENDPOINT_DISCOVERY = "aws_enable_endpoint_discovery";
   public static final String CREDENTIAL_PROCESS = "credential_process";
   public static final String WEB_IDENTITY_TOKEN = "web_identity_token_file";
}
