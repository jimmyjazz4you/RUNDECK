package com.amazonaws.arn;

import com.amazonaws.util.ValidationUtils;

public class Arn {
   private final String partition;
   private final String service;
   private final String region;
   private final String accountId;
   private final String resource;
   private final ArnResource arnResource;

   private Arn(Arn.Builder builder) {
      this.partition = ValidationUtils.assertStringNotEmpty(builder.partition, "partition");
      this.service = ValidationUtils.assertStringNotEmpty(builder.service, "service");
      this.region = builder.region;
      this.accountId = builder.accountId;
      this.resource = ValidationUtils.assertStringNotEmpty(builder.resource, "resource");
      this.arnResource = ArnResource.fromString(this.resource);
   }

   public String getPartition() {
      return this.partition;
   }

   public String getService() {
      return this.service;
   }

   public String getRegion() {
      return this.region;
   }

   public String getAccountId() {
      return this.accountId;
   }

   public ArnResource getResource() {
      return this.arnResource;
   }

   public String getResourceAsString() {
      return this.resource;
   }

   public Arn.Builder toBuilder() {
      return builder()
         .withPartition(this.getPartition())
         .withService(this.getService())
         .withAccountId(this.getAccountId())
         .withRegion(this.getRegion())
         .withResource(this.getResourceAsString());
   }

   public static Arn.Builder builder() {
      return new Arn.Builder();
   }

   public static Arn fromString(String arn) {
      int arnColonIndex = arn.indexOf(58);
      if (arnColonIndex >= 0 && "arn".equals(arn.substring(0, arnColonIndex))) {
         int partitionColonIndex = arn.indexOf(58, arnColonIndex + 1);
         if (partitionColonIndex < 0) {
            throw new IllegalArgumentException("Malformed ARN - no AWS partition specified");
         } else {
            String partition = arn.substring(arnColonIndex + 1, partitionColonIndex);
            int serviceColonIndex = arn.indexOf(58, partitionColonIndex + 1);
            if (serviceColonIndex < 0) {
               throw new IllegalArgumentException("Malformed ARN - no service specified");
            } else {
               String service = arn.substring(partitionColonIndex + 1, serviceColonIndex);
               int regionColonIndex = arn.indexOf(58, serviceColonIndex + 1);
               if (regionColonIndex < 0) {
                  throw new IllegalArgumentException("Malformed ARN - no AWS region partition specified");
               } else {
                  String region = arn.substring(serviceColonIndex + 1, regionColonIndex);
                  int accountColonIndex = arn.indexOf(58, regionColonIndex + 1);
                  if (accountColonIndex < 0) {
                     throw new IllegalArgumentException("Malformed ARN - no AWS account specified");
                  } else {
                     String accountId = arn.substring(regionColonIndex + 1, accountColonIndex);
                     String resource = arn.substring(accountColonIndex + 1);
                     if (resource.isEmpty()) {
                        throw new IllegalArgumentException("Malformed ARN - no resource specified");
                     } else {
                        return builder()
                           .withPartition(partition)
                           .withService(service)
                           .withRegion(region)
                           .withAccountId(accountId)
                           .withResource(resource)
                           .build();
                     }
                  }
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Malformed ARN - doesn't start with 'arn:'");
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("arn:");
      sb.append(this.partition);
      sb.append(":");
      sb.append(this.service);
      sb.append(":");
      sb.append(this.region);
      sb.append(":");
      sb.append(this.accountId);
      sb.append(":");
      sb.append(this.resource);
      return sb.toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Arn arn = (Arn)o;
         if (!this.partition.equals(arn.partition)) {
            return false;
         } else if (!this.service.equals(arn.service)) {
            return false;
         } else if (this.region != null ? this.region.equals(arn.region) : arn.region == null) {
            return (this.accountId != null ? this.accountId.equals(arn.accountId) : arn.accountId == null) ? this.resource.equals(arn.resource) : false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.partition.hashCode();
      result = 31 * result + this.service.hashCode();
      result = 31 * result + (this.region != null ? this.region.hashCode() : 0);
      result = 31 * result + (this.accountId != null ? this.accountId.hashCode() : 0);
      return 31 * result + this.resource.hashCode();
   }

   public static final class Builder {
      private String partition;
      private String service;
      private String region;
      private String accountId;
      private String resource;

      private Builder() {
      }

      public void setPartition(String partition) {
         this.partition = partition;
      }

      public Arn.Builder withPartition(String partition) {
         this.setPartition(partition);
         return this;
      }

      public void setService(String service) {
         this.service = service;
      }

      public Arn.Builder withService(String service) {
         this.setService(service);
         return this;
      }

      public void setRegion(String region) {
         this.region = region;
      }

      public Arn.Builder withRegion(String region) {
         this.setRegion(region);
         return this;
      }

      public void setAccountId(String accountId) {
         this.accountId = accountId;
      }

      public Arn.Builder withAccountId(String accountId) {
         this.setAccountId(accountId);
         return this;
      }

      public void setResource(String resource) {
         this.resource = resource;
      }

      public Arn.Builder withResource(String resource) {
         this.setResource(resource);
         return this;
      }

      public Arn build() {
         return new Arn(this);
      }
   }
}
