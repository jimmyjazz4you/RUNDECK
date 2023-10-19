package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class PublicAccessBlockConfiguration implements Serializable, Cloneable {
   private Boolean blockPublicAcls;
   private Boolean ignorePublicAcls;
   private Boolean blockPublicPolicy;
   private Boolean restrictPublicBuckets;

   public Boolean getBlockPublicAcls() {
      return this.blockPublicAcls;
   }

   public void setBlockPublicAcls(Boolean blockPublicAcls) {
      this.blockPublicAcls = blockPublicAcls;
   }

   public PublicAccessBlockConfiguration withBlockPublicAcls(Boolean blockPublicAcls) {
      this.setBlockPublicAcls(blockPublicAcls);
      return this;
   }

   public Boolean getIgnorePublicAcls() {
      return this.ignorePublicAcls;
   }

   public void setIgnorePublicAcls(Boolean ignorePublicAcls) {
      this.ignorePublicAcls = ignorePublicAcls;
   }

   public PublicAccessBlockConfiguration withIgnorePublicAcls(Boolean ignorePublicAcls) {
      this.setIgnorePublicAcls(ignorePublicAcls);
      return this;
   }

   public Boolean getBlockPublicPolicy() {
      return this.blockPublicPolicy;
   }

   public void setBlockPublicPolicy(Boolean blockPublicPolicy) {
      this.blockPublicPolicy = blockPublicPolicy;
   }

   public PublicAccessBlockConfiguration withBlockPublicPolicy(Boolean blockPublicPolicy) {
      this.setBlockPublicPolicy(blockPublicPolicy);
      return this;
   }

   public Boolean getRestrictPublicBuckets() {
      return this.restrictPublicBuckets;
   }

   public void setRestrictPublicBuckets(Boolean restrictPublicBuckets) {
      this.restrictPublicBuckets = restrictPublicBuckets;
   }

   public PublicAccessBlockConfiguration withRestrictPublicBuckets(Boolean restrictPublicBuckets) {
      this.setRestrictPublicBuckets(restrictPublicBuckets);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         PublicAccessBlockConfiguration that = (PublicAccessBlockConfiguration)o;
         if (this.blockPublicAcls != null ? this.blockPublicAcls.equals(that.blockPublicAcls) : that.blockPublicAcls == null) {
            if (this.ignorePublicAcls != null ? this.ignorePublicAcls.equals(that.ignorePublicAcls) : that.ignorePublicAcls == null) {
               if (this.blockPublicPolicy != null ? this.blockPublicPolicy.equals(that.blockPublicPolicy) : that.blockPublicPolicy == null) {
                  return this.restrictPublicBuckets != null
                     ? this.restrictPublicBuckets.equals(that.restrictPublicBuckets)
                     : that.restrictPublicBuckets == null;
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
      int result = this.blockPublicAcls != null ? this.blockPublicAcls.hashCode() : 0;
      result = 31 * result + (this.ignorePublicAcls != null ? this.ignorePublicAcls.hashCode() : 0);
      result = 31 * result + (this.blockPublicPolicy != null ? this.blockPublicPolicy.hashCode() : 0);
      return 31 * result + (this.restrictPublicBuckets != null ? this.restrictPublicBuckets.hashCode() : 0);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getBlockPublicAcls() != null) {
         sb.append("BlockPublicAcls: ").append(this.getBlockPublicAcls()).append(",");
      }

      if (this.getIgnorePublicAcls() != null) {
         sb.append("IgnorePublicAcls: ").append(this.getIgnorePublicAcls()).append(",");
      }

      if (this.getBlockPublicPolicy() != null) {
         sb.append("BlockPublicPolicy: ").append(this.getBlockPublicPolicy()).append(",");
      }

      if (this.getRestrictPublicBuckets() != null) {
         sb.append("RestrictPublicBuckets: ").append(this.getRestrictPublicBuckets()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   public PublicAccessBlockConfiguration clone() {
      try {
         return (PublicAccessBlockConfiguration)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
