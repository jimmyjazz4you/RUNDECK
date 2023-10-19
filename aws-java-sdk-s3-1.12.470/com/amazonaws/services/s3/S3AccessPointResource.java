package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.ValidationUtils;

@SdkInternalApi
public final class S3AccessPointResource implements S3Resource {
   private static final S3ResourceType S3_RESOURCE_TYPE = S3ResourceType.ACCESS_POINT;
   private final String partition;
   private final String region;
   private final String accountId;
   private final String accessPointName;
   private final S3Resource parentS3Resource;

   private S3AccessPointResource(S3AccessPointResource.Builder b) {
      this.accessPointName = ValidationUtils.assertStringNotEmpty(b.accessPointName, "accessPointName");
      if (b.parentS3Resource == null) {
         this.parentS3Resource = null;
         this.partition = ValidationUtils.assertStringNotEmpty(b.partition, "partition");
         this.region = ValidationUtils.assertStringNotEmpty(b.region, "region");
         this.accountId = ValidationUtils.assertStringNotEmpty(b.accountId, "accountId");
      } else {
         this.parentS3Resource = this.validateParentS3Resource(b.parentS3Resource);
         ValidationUtils.assertAllAreNull("partition cannot be set on builder if it has parent resource", new Object[]{b.partition});
         ValidationUtils.assertAllAreNull("region cannot be set on builder if it has parent resource", new Object[]{b.region});
         ValidationUtils.assertAllAreNull("accountId cannot be set on builder if it has parent resource", new Object[]{b.accountId});
         this.partition = this.parentS3Resource.getPartition();
         this.region = this.parentS3Resource.getRegion();
         this.accountId = this.parentS3Resource.getAccountId();
      }
   }

   private S3Resource validateParentS3Resource(S3Resource parentS3Resource) {
      String parentResourceType = parentS3Resource.getType();
      if (!S3ResourceType.OUTPOST.toString().equals(parentResourceType) && !S3ResourceType.OBJECT_LAMBDAS.toString().equals(parentResourceType)) {
         throw new IllegalArgumentException(
            "Invalid 'parentS3Resource' type. An S3 access point resource must be associated with an outpost or object lambdas parent resource."
         );
      } else {
         return parentS3Resource;
      }
   }

   public static S3AccessPointResource.Builder builder() {
      return new S3AccessPointResource.Builder();
   }

   @Override
   public String getType() {
      return S3_RESOURCE_TYPE.toString();
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
   public S3Resource getParentS3Resource() {
      return this.parentS3Resource;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         S3AccessPointResource that = (S3AccessPointResource)o;
         if (this.partition != null ? this.partition.equals(that.partition) : that.partition == null) {
            if (this.region != null ? this.region.equals(that.region) : that.region == null) {
               if (this.accountId != null ? this.accountId.equals(that.accountId) : that.accountId == null) {
                  return (this.parentS3Resource != null ? this.parentS3Resource.equals(that.parentS3Resource) : that.parentS3Resource == null)
                     ? this.accessPointName.equals(that.accessPointName)
                     : false;
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
      result = 31 * result + this.accessPointName.hashCode();
      return 31 * result + (this.parentS3Resource != null ? this.parentS3Resource.hashCode() : 0);
   }

   public static final class Builder {
      private String partition;
      private String region;
      private String accountId;
      private String accessPointName;
      private S3Resource parentS3Resource;

      public void setPartition(String partition) {
         this.partition = partition;
      }

      public S3AccessPointResource.Builder withPartition(String partition) {
         this.setPartition(partition);
         return this;
      }

      public void setRegion(String region) {
         this.region = region;
      }

      public S3AccessPointResource.Builder withRegion(String region) {
         this.setRegion(region);
         return this;
      }

      public void setAccountId(String accountId) {
         this.accountId = accountId;
      }

      public S3AccessPointResource.Builder withAccountId(String accountId) {
         this.setAccountId(accountId);
         return this;
      }

      public void setAccessPointName(String accessPointName) {
         this.accessPointName = accessPointName;
      }

      public S3AccessPointResource.Builder withAccessPointName(String accessPointName) {
         this.setAccessPointName(accessPointName);
         return this;
      }

      public S3AccessPointResource.Builder withParentS3Resource(S3Resource parentS3Resource) {
         this.parentS3Resource = parentS3Resource;
         return this;
      }

      public S3AccessPointResource build() {
         return new S3AccessPointResource(this);
      }
   }
}
