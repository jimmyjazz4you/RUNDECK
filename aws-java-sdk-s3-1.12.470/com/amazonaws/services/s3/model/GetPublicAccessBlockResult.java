package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class GetPublicAccessBlockResult implements Serializable, Cloneable {
   private PublicAccessBlockConfiguration publicAccessBlockConfiguration;

   public PublicAccessBlockConfiguration getPublicAccessBlockConfiguration() {
      return this.publicAccessBlockConfiguration;
   }

   public void setPublicAccessBlockConfiguration(PublicAccessBlockConfiguration publicAccessBlockConfiguration) {
      this.publicAccessBlockConfiguration = publicAccessBlockConfiguration;
   }

   public GetPublicAccessBlockResult withPublicAccessBlockConfiguration(PublicAccessBlockConfiguration publicAccessBlockConfiguration) {
      this.setPublicAccessBlockConfiguration(publicAccessBlockConfiguration);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         GetPublicAccessBlockResult that = (GetPublicAccessBlockResult)o;
         return this.publicAccessBlockConfiguration != null
            ? this.publicAccessBlockConfiguration.equals(that.publicAccessBlockConfiguration)
            : that.publicAccessBlockConfiguration == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.publicAccessBlockConfiguration != null ? this.publicAccessBlockConfiguration.hashCode() : 0;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getPublicAccessBlockConfiguration() != null) {
         sb.append("PublicAccessBlockConfiguration: ").append(this.getPublicAccessBlockConfiguration()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   public GetPublicAccessBlockResult clone() {
      try {
         return (GetPublicAccessBlockResult)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
