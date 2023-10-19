package com.amazonaws.auth.profile.internal.securitytoken;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.auth.AWSCredentialsProvider;

@SdkProtectedApi
public interface ProfileCredentialsService {
   AWSCredentialsProvider getAssumeRoleCredentialsProvider(RoleInfo var1);
}
