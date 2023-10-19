package com.amazonaws.retry.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.auth.profile.internal.BasicProfileConfigFileLoader;
import com.amazonaws.internal.config.InternalConfig;
import com.amazonaws.retry.RetryMode;

@SdkInternalApi
public final class RetryModeResolver {
   private static final String PROFILE_PROPERTY = "retry_mode";
   private static final RetryMode SDK_DEFAULT_RETRY_MODE = RetryMode.LEGACY;
   private final BasicProfileConfigFileLoader configFileLoader;
   private final InternalConfig internalConfig;
   private final RetryMode retryMode;

   public RetryModeResolver() {
      this(BasicProfileConfigFileLoader.INSTANCE, InternalConfig.Factory.getInternalConfig());
   }

   @SdkTestInternalApi
   RetryModeResolver(BasicProfileConfigFileLoader configFileLoader, InternalConfig internalConfig) {
      this.configFileLoader = configFileLoader;
      this.internalConfig = internalConfig;
      this.retryMode = this.resolveRetryMode();
   }

   public RetryMode retryMode() {
      return this.retryMode;
   }

   private RetryMode systemProperty() {
      return RetryMode.fromName(System.getProperty("com.amazonaws.sdk.retryMode"));
   }

   private RetryMode envVar() {
      return RetryMode.fromName(System.getenv("AWS_RETRY_MODE"));
   }

   private RetryMode internalDefault() {
      return RetryMode.fromName(this.internalConfig.getDefaultRetryMode());
   }

   private RetryMode resolveRetryMode() {
      RetryMode mode = this.envVar();
      if (mode != null) {
         return mode;
      } else {
         mode = this.systemProperty();
         if (mode != null) {
            return mode;
         } else {
            mode = this.profile();
            if (mode != null) {
               return mode;
            } else {
               mode = this.internalDefault();
               return mode != null ? mode : SDK_DEFAULT_RETRY_MODE;
            }
         }
      }
   }

   private RetryMode profile() {
      BasicProfile profile = this.configFileLoader.getProfile();
      if (profile == null) {
         return null;
      } else {
         String val = profile.getPropertyValue("retry_mode");
         return RetryMode.fromName(val);
      }
   }
}
