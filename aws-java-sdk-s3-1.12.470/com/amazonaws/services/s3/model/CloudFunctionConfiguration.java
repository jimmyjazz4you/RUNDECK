package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.EnumSet;

@Deprecated
public class CloudFunctionConfiguration extends NotificationConfiguration implements Serializable {
   private final String invocationRoleARN;
   private final String cloudFunctionARN;

   public CloudFunctionConfiguration(String invocationRole, String function, EnumSet<S3Event> events) {
      super(events);
      this.invocationRoleARN = invocationRole;
      this.cloudFunctionARN = function;
   }

   public CloudFunctionConfiguration(String invocationRole, String function, String... events) {
      super(events);
      this.invocationRoleARN = invocationRole;
      this.cloudFunctionARN = function;
   }

   public String getInvocationRoleARN() {
      return this.invocationRoleARN;
   }

   public String getCloudFunctionARN() {
      return this.cloudFunctionARN;
   }
}
