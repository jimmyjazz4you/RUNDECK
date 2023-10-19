package com.amazonaws.auth.profile.internal.securitytoken;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentialsProvider;

@SdkInternalApi
public class STSProfileCredentialsServiceLoader implements ProfileCredentialsService {
   private static final STSProfileCredentialsServiceLoader INSTANCE = new STSProfileCredentialsServiceLoader();

   private STSProfileCredentialsServiceLoader() {
   }

   @Override
   public AWSCredentialsProvider getAssumeRoleCredentialsProvider(RoleInfo targetRoleInfo) {
      return new STSProfileCredentialsServiceProvider(targetRoleInfo);
   }

   public static STSProfileCredentialsServiceLoader getInstance() {
      return INSTANCE;
   }
}
