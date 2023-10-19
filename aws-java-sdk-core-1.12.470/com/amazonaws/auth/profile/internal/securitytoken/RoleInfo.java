package com.amazonaws.auth.profile.internal.securitytoken;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.internal.StaticCredentialsProvider;

@SdkProtectedApi
public class RoleInfo implements Cloneable {
   private String roleArn;
   private String roleSessionName;
   private String externalId;
   private String webIdentityTokenFilePath;
   private AWSCredentialsProvider longLivedCredentialsProvider;

   public void setRoleArn(String roleArn) {
      this.roleArn = roleArn;
   }

   public String getRoleArn() {
      return this.roleArn;
   }

   public RoleInfo withRoleArn(String roleArn) {
      this.setRoleArn(roleArn);
      return this;
   }

   public void setRoleSessionName(String roleSessionName) {
      this.roleSessionName = roleSessionName;
   }

   public String getRoleSessionName() {
      return this.roleSessionName;
   }

   public RoleInfo withRoleSessionName(String roleSessionName) {
      this.setRoleSessionName(roleSessionName);
      return this;
   }

   public void setExternalId(String externalId) {
      this.externalId = externalId;
   }

   public String getExternalId() {
      return this.externalId;
   }

   public RoleInfo withExternalId(String externalId) {
      this.setExternalId(externalId);
      return this;
   }

   public void setWebIdentityTokenFilePath(String webIdentityTokenFilePath) {
      this.webIdentityTokenFilePath = webIdentityTokenFilePath;
   }

   public String getWebIdentityTokenFilePath() {
      return this.webIdentityTokenFilePath;
   }

   public RoleInfo withWebIdentityTokenFilePath(String webIdentityTokenFilePath) {
      this.setWebIdentityTokenFilePath(webIdentityTokenFilePath);
      return this;
   }

   public void setLongLivedCredentialsProvider(AWSCredentialsProvider longLivedCredentialsProvider) {
      this.longLivedCredentialsProvider = longLivedCredentialsProvider;
   }

   public AWSCredentialsProvider getLongLivedCredentialsProvider() {
      return this.longLivedCredentialsProvider;
   }

   public RoleInfo withLongLivedCredentialsProvider(AWSCredentialsProvider longLivedCredentialsProvider) {
      this.setLongLivedCredentialsProvider(longLivedCredentialsProvider);
      return this;
   }

   public RoleInfo withLongLivedCredentials(AWSCredentials longLivedCredentials) {
      this.setLongLivedCredentialsProvider(new StaticCredentialsProvider(longLivedCredentials));
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getRoleArn() != null) {
         sb.append("RoleArn: " + this.getRoleArn() + ",");
      }

      if (this.getRoleSessionName() != null) {
         sb.append("RoleSessionName: " + this.getRoleSessionName() + ",");
      }

      if (this.getExternalId() != null) {
         sb.append("ExternalId: " + this.getExternalId() + ",");
      }

      sb.append("}");
      return sb.toString();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof RoleInfo)) {
         return false;
      } else {
         RoleInfo other = (RoleInfo)obj;
         if (other.getRoleArn() == null ^ this.getRoleArn() == null) {
            return false;
         } else if (other.getRoleArn() != null && !other.getRoleArn().equals(this.getRoleArn())) {
            return false;
         } else if (other.getRoleSessionName() == null ^ this.getRoleSessionName() == null) {
            return false;
         } else if (other.getRoleSessionName() != null && !other.getRoleSessionName().equals(this.getRoleSessionName())) {
            return false;
         } else if (other.getExternalId() == null ^ this.getExternalId() == null) {
            return false;
         } else if (other.getExternalId() != null && !other.getExternalId().equals(this.getExternalId())) {
            return false;
         } else {
            return other.getLongLivedCredentialsProvider() == this.getLongLivedCredentialsProvider();
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getRoleArn() == null ? 0 : this.getRoleArn().hashCode());
      hashCode = 31 * hashCode + (this.getRoleSessionName() == null ? 0 : this.getRoleSessionName().hashCode());
      hashCode = 31 * hashCode + (this.getExternalId() == null ? 0 : this.getExternalId().hashCode());
      return 31 * hashCode + (this.getLongLivedCredentialsProvider() == null ? 0 : this.getLongLivedCredentialsProvider().hashCode());
   }

   public RoleInfo clone() {
      try {
         return (RoleInfo)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
