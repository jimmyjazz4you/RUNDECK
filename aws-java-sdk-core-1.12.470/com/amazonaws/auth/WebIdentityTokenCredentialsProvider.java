package com.amazonaws.auth;

import com.amazonaws.auth.profile.internal.securitytoken.RoleInfo;
import com.amazonaws.auth.profile.internal.securitytoken.STSProfileCredentialsServiceLoader;

public class WebIdentityTokenCredentialsProvider implements AWSCredentialsProvider {
   private final AWSCredentialsProvider credentialsProvider;
   private final RuntimeException loadException;

   public WebIdentityTokenCredentialsProvider() {
      this(new WebIdentityTokenCredentialsProvider.BuilderImpl());
   }

   private WebIdentityTokenCredentialsProvider(WebIdentityTokenCredentialsProvider.BuilderImpl builder) {
      AWSCredentialsProvider credentialsProvider = null;
      RuntimeException loadException = null;

      try {
         String webIdentityTokenFile = builder.webIdentityTokenFile != null ? builder.webIdentityTokenFile : System.getenv("AWS_WEB_IDENTITY_TOKEN_FILE");
         String roleArn = builder.roleArn != null ? builder.roleArn : System.getenv("AWS_ROLE_ARN");
         String roleSessionName = builder.roleSessionName != null ? builder.roleSessionName : System.getenv("AWS_ROLE_SESSION_NAME");
         if (roleSessionName == null) {
            roleSessionName = "aws-sdk-java-" + System.currentTimeMillis();
         }

         RoleInfo roleInfo = new RoleInfo().withRoleArn(roleArn).withRoleSessionName(roleSessionName).withWebIdentityTokenFilePath(webIdentityTokenFile);
         credentialsProvider = STSProfileCredentialsServiceLoader.getInstance().getAssumeRoleCredentialsProvider(roleInfo);
      } catch (RuntimeException var8) {
         loadException = var8;
      }

      this.loadException = loadException;
      this.credentialsProvider = credentialsProvider;
   }

   @Override
   public AWSCredentials getCredentials() {
      if (this.loadException != null) {
         throw this.loadException;
      } else {
         return this.credentialsProvider.getCredentials();
      }
   }

   @Override
   public void refresh() {
   }

   public static WebIdentityTokenCredentialsProvider create() {
      return builder().build();
   }

   public static WebIdentityTokenCredentialsProvider.Builder builder() {
      return new WebIdentityTokenCredentialsProvider.BuilderImpl();
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName();
   }

   public interface Builder {
      WebIdentityTokenCredentialsProvider.Builder roleArn(String var1);

      WebIdentityTokenCredentialsProvider.Builder roleSessionName(String var1);

      WebIdentityTokenCredentialsProvider.Builder webIdentityTokenFile(String var1);

      WebIdentityTokenCredentialsProvider build();
   }

   static final class BuilderImpl implements WebIdentityTokenCredentialsProvider.Builder {
      private String roleArn;
      private String roleSessionName;
      private String webIdentityTokenFile;

      @Override
      public WebIdentityTokenCredentialsProvider.Builder roleArn(String roleArn) {
         this.roleArn = roleArn;
         return this;
      }

      public void setRoleArn(String roleArn) {
         this.roleArn(roleArn);
      }

      @Override
      public WebIdentityTokenCredentialsProvider.Builder roleSessionName(String roleSessionName) {
         this.roleSessionName = roleSessionName;
         return this;
      }

      public void setRoleSessionName(String roleSessionName) {
         this.roleSessionName(roleSessionName);
      }

      @Override
      public WebIdentityTokenCredentialsProvider.Builder webIdentityTokenFile(String webIdentityTokenFile) {
         this.webIdentityTokenFile = webIdentityTokenFile;
         return this;
      }

      public void setWebIdentityTokenFile(String webIdentityTokenFile) {
         this.webIdentityTokenFile(webIdentityTokenFile);
      }

      @Override
      public WebIdentityTokenCredentialsProvider build() {
         return new WebIdentityTokenCredentialsProvider(this);
      }
   }
}
