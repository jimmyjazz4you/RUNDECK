package com.amazonaws.arn;

import com.amazonaws.util.ValidationUtils;

public class ArnResource {
   private final String resourceType;
   private final String resource;
   private final String qualifier;

   private ArnResource(ArnResource.Builder b) {
      this.resourceType = b.resourceType;
      this.resource = ValidationUtils.assertStringNotEmpty(b.resource, "resource");
      this.qualifier = b.qualifier;
   }

   public String getResourceType() {
      return this.resourceType;
   }

   public String getResource() {
      return this.resource;
   }

   public String getQualifier() {
      return this.qualifier;
   }

   public static ArnResource.Builder builder() {
      return new ArnResource.Builder();
   }

   public static ArnResource fromString(String resource) {
      Integer resourceTypeBoundary = null;
      Integer resourceIdBoundary = null;

      for(int i = 0; i < resource.length(); ++i) {
         char ch = resource.charAt(i);
         if (ch == ':' || ch == '/') {
            resourceTypeBoundary = i;
            break;
         }
      }

      if (resourceTypeBoundary != null) {
         for(int i = resource.length() - 1; i > resourceTypeBoundary; --i) {
            char ch = resource.charAt(i);
            if (ch == ':') {
               resourceIdBoundary = i;
               break;
            }
         }
      }

      if (resourceTypeBoundary == null) {
         return builder().withResource(resource).build();
      } else if (resourceIdBoundary == null) {
         String resourceType = resource.substring(0, resourceTypeBoundary);
         String resourceId = resource.substring(resourceTypeBoundary + 1);
         return builder().withResourceType(resourceType).withResource(resourceId).build();
      } else {
         String resourceType = resource.substring(0, resourceTypeBoundary);
         String resourceId = resource.substring(resourceTypeBoundary + 1, resourceIdBoundary);
         String qualifier = resource.substring(resourceIdBoundary + 1);
         return builder().withResourceType(resourceType).withResource(resourceId).withQualifier(qualifier).build();
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      if (this.resourceType != null) {
         sb.append(this.resourceType);
         sb.append(":");
      }

      sb.append(this.resource);
      if (this.qualifier != null) {
         sb.append(":");
         sb.append(this.qualifier);
      }

      return sb.toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ArnResource that = (ArnResource)o;
         if (this.resourceType != null ? this.resourceType.equals(that.resourceType) : that.resourceType == null) {
            if (!this.resource.equals(that.resource)) {
               return false;
            } else {
               return this.qualifier != null ? this.qualifier.equals(that.qualifier) : that.qualifier == null;
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
      int result = this.resourceType != null ? this.resourceType.hashCode() : 0;
      result = 31 * result + this.resource.hashCode();
      return 31 * result + (this.qualifier != null ? this.qualifier.hashCode() : 0);
   }

   public static final class Builder {
      private String resourceType;
      private String resource;
      private String qualifier;

      private Builder() {
      }

      public void setResourceType(String resourceType) {
         this.resourceType = resourceType;
      }

      public ArnResource.Builder withResourceType(String resourceType) {
         this.setResourceType(resourceType);
         return this;
      }

      public void setResource(String resource) {
         this.resource = resource;
      }

      public ArnResource.Builder withResource(String resource) {
         this.setResource(resource);
         return this;
      }

      public void setQualifier(String qualifier) {
         this.qualifier = qualifier;
      }

      public ArnResource.Builder withQualifier(String qualifier) {
         this.setQualifier(qualifier);
         return this;
      }

      public ArnResource build() {
         return new ArnResource(this);
      }
   }
}
