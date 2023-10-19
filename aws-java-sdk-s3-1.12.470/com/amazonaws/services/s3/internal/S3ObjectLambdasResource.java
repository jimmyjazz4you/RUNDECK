package com.amazonaws.services.s3.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.S3Resource;
import com.amazonaws.services.s3.S3ResourceType;
import com.amazonaws.util.ValidationUtils;

@SdkInternalApi
public final class S3ObjectLambdasResource implements S3Resource {
   private final String partition;
   private final String region;
   private final String accountId;
   private final String accessPointName;

   private S3ObjectLambdasResource(S3ObjectLambdasResource.Builder b) {
      this.partition = ValidationUtils.assertStringNotEmpty(b.partition, "partition");
      this.region = ValidationUtils.assertStringNotEmpty(b.region, "region");
      this.accountId = ValidationUtils.assertStringNotEmpty(b.accountId, "accountId");
      this.accessPointName = ValidationUtils.assertStringNotEmpty(b.accessPointName, "accessPointName");
   }

   @Override
   public String getType() {
      return S3ResourceType.OBJECT_LAMBDAS.toString();
   }

   @Override
   public S3Resource getParentS3Resource() {
      return null;
   }

   public String getPartition() {
      return this.partition;
   }

   public String getRegion() {
      return this.region;
   }

   public String getAccountId() {
      return this.accountId;
   }

   public String getAccessPointName() {
      return this.accessPointName;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         S3ObjectLambdasResource that = (S3ObjectLambdasResource)o;
         if (this.partition != null ? this.partition.equals(that.partition) : that.partition == null) {
            if (this.region != null ? this.region.equals(that.region) : that.region == null) {
               if (this.accountId != null ? this.accountId.equals(that.accountId) : that.accountId == null) {
                  return this.accessPointName != null ? this.accessPointName.equals(that.accessPointName) : that.accessPointName == null;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.partition != null ? this.partition.hashCode() : 0;
      result = 31 * result + (this.region != null ? this.region.hashCode() : 0);
      result = 31 * result + (this.accountId != null ? this.accountId.hashCode() : 0);
      return 31 * result + (this.accessPointName != null ? this.accessPointName.hashCode() : 0);
   }

   public static S3ObjectLambdasResource.Builder builder() {
      return new S3ObjectLambdasResource.Builder();
   }

   public static final class Builder {
      private String partition;
      private String region;
      private String accountId;
      private String accessPointName;

      private Builder() {
      }

      public S3ObjectLambdasResource.Builder withPartition(String partition) {
         this.partition = partition;
         return this;
      }

      public S3ObjectLambdasResource.Builder withRegion(String region) {
         this.region = region;
         return this;
      }

      public S3ObjectLambdasResource.Builder withAccountId(String accountId) {
         this.accountId = accountId;
         return this;
      }

      public S3ObjectLambdasResource.Builder withAccessPointName(String accessPointName) {
         this.accessPointName = accessPointName;
         return this;
      }

      public S3ObjectLambdasResource build() {
         return new S3ObjectLambdasResource(this);
      }
   }
}
