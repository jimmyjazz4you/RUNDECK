package com.amazonaws.services.s3.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.S3Resource;
import com.amazonaws.services.s3.S3ResourceType;
import com.amazonaws.util.ValidationUtils;

@SdkInternalApi
public final class S3OutpostResource implements S3Resource {
   private final String partition;
   private final String region;
   private final String accountId;
   private final String outpostId;

   private S3OutpostResource(S3OutpostResource.Builder b) {
      this.partition = ValidationUtils.assertStringNotEmpty(b.partition, "partition");
      this.region = ValidationUtils.assertStringNotEmpty(b.region, "region");
      this.accountId = ValidationUtils.assertStringNotEmpty(b.accountId, "accountId");
      this.outpostId = ValidationUtils.assertStringNotEmpty(b.outpostId, "outpostId");
   }

   public static S3OutpostResource.Builder builder() {
      return new S3OutpostResource.Builder();
   }

   @Override
   public String getType() {
      return S3ResourceType.OUTPOST.toString();
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

   public String getOutpostId() {
      return this.outpostId;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         S3OutpostResource that = (S3OutpostResource)o;
         if (this.partition != null ? this.partition.equals(that.partition) : that.partition == null) {
            if (this.region != null ? this.region.equals(that.region) : that.region == null) {
               return (this.accountId != null ? this.accountId.equals(that.accountId) : that.accountId == null) ? this.outpostId.equals(that.outpostId) : false;
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
      return 31 * result + this.outpostId.hashCode();
   }

   public static final class Builder {
      private String outpostId;
      private String partition;
      private String region;
      private String accountId;

      private Builder() {
      }

      public S3OutpostResource.Builder withPartition(String partition) {
         this.partition = partition;
         return this;
      }

      public S3OutpostResource.Builder withRegion(String region) {
         this.region = region;
         return this;
      }

      public S3OutpostResource.Builder withAccountId(String accountId) {
         this.accountId = accountId;
         return this;
      }

      public S3OutpostResource.Builder withOutpostId(String outpostId) {
         this.outpostId = outpostId;
         return this;
      }

      public S3OutpostResource build() {
         return new S3OutpostResource(this);
      }
   }
}
