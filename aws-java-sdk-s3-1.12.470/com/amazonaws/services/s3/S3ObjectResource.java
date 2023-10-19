package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.ValidationUtils;

@SdkInternalApi
public final class S3ObjectResource implements S3Resource {
   private static final S3ResourceType S3_RESOURCE_TYPE = S3ResourceType.OBJECT;
   private final S3Resource parentS3Resource;
   private final String key;

   private S3ObjectResource(S3ObjectResource.Builder b) {
      this.parentS3Resource = this.validateParentS3Resource(b.parentS3Resource);
      this.key = ValidationUtils.assertStringNotEmpty(b.key, "key");
   }

   public static S3ObjectResource.Builder builder() {
      return new S3ObjectResource.Builder();
   }

   @Override
   public String getType() {
      return S3_RESOURCE_TYPE.toString();
   }

   public String getPartition() {
      return this.parentS3Resource.getPartition();
   }

   public String getRegion() {
      return this.parentS3Resource.getRegion();
   }

   public String getAccountId() {
      return this.parentS3Resource.getAccountId();
   }

   public String getKey() {
      return this.key;
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
         S3ObjectResource that = (S3ObjectResource)o;
         if (this.parentS3Resource != null ? this.parentS3Resource.equals(that.parentS3Resource) : that.parentS3Resource == null) {
            return this.key != null ? this.key.equals(that.key) : that.key == null;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.parentS3Resource != null ? this.parentS3Resource.hashCode() : 0;
      return 31 * result + (this.key != null ? this.key.hashCode() : 0);
   }

   private S3Resource validateParentS3Resource(S3Resource parentS3Resource) {
      ValidationUtils.assertNotNull(parentS3Resource, "parentS3Resource");
      if (!S3ResourceType.ACCESS_POINT.toString().equals(parentS3Resource.getType()) && !S3ResourceType.BUCKET.toString().equals(parentS3Resource.getType())) {
         throw new IllegalArgumentException(
            "Invalid 'parentS3Resource' type. An S3 object resource must be associated with either a bucket or access-point parent resource."
         );
      } else {
         return parentS3Resource;
      }
   }

   public static final class Builder {
      private S3Resource parentS3Resource;
      private String key;

      public void setParentS3Resource(S3Resource parentS3Resource) {
         this.parentS3Resource = parentS3Resource;
      }

      public S3ObjectResource.Builder withParentS3Resource(S3Resource parentS3Resource) {
         this.setParentS3Resource(parentS3Resource);
         return this;
      }

      public void setKey(String key) {
         this.key = key;
      }

      public S3ObjectResource.Builder withKey(String key) {
         this.setKey(key);
         return this;
      }

      public S3ObjectResource build() {
         return new S3ObjectResource(this);
      }
   }
}
