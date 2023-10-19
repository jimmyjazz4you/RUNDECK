package com.amazonaws.auth.profile.internal.securitytoken;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

@ThreadSafe
public class STSProfileCredentialsServiceProvider implements AWSCredentialsProvider {
   private static final String CLASS_NAME = "com.amazonaws.services.securitytoken.internal.STSProfileCredentialsService";
   private static volatile ProfileCredentialsService STS_CREDENTIALS_SERVICE;
   private final RoleInfo roleInfo;
   private volatile AWSCredentialsProvider profileCredentialsProvider;

   public STSProfileCredentialsServiceProvider(RoleInfo roleInfo) {
      this.roleInfo = roleInfo;
   }

   private AWSCredentialsProvider getProfileCredentialsProvider() {
      if (this.profileCredentialsProvider == null) {
         synchronized(STSProfileCredentialsServiceProvider.class) {
            if (this.profileCredentialsProvider == null) {
               this.profileCredentialsProvider = getProfileCredentialService().getAssumeRoleCredentialsProvider(this.roleInfo);
            }
         }
      }

      return this.profileCredentialsProvider;
   }

   private static synchronized ProfileCredentialsService getProfileCredentialService() {
      if (STS_CREDENTIALS_SERVICE == null) {
         try {
            STS_CREDENTIALS_SERVICE = (ProfileCredentialsService)Class.forName("com.amazonaws.services.securitytoken.internal.STSProfileCredentialsService")
               .newInstance();
         } catch (ClassNotFoundException var1) {
            throw new SdkClientException("To use assume role profiles the aws-java-sdk-sts module must be on the class path.", var1);
         } catch (InstantiationException var2) {
            throw new SdkClientException("Failed to instantiate com.amazonaws.services.securitytoken.internal.STSProfileCredentialsService", var2);
         } catch (IllegalAccessException var3) {
            throw new SdkClientException("Failed to instantiate com.amazonaws.services.securitytoken.internal.STSProfileCredentialsService", var3);
         }
      }

      return STS_CREDENTIALS_SERVICE;
   }

   @Override
   public AWSCredentials getCredentials() {
      return this.getProfileCredentialsProvider().getCredentials();
   }

   @Override
   public void refresh() {
      this.getProfileCredentialsProvider().refresh();
   }
}
