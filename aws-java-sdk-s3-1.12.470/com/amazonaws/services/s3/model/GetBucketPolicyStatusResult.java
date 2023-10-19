package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class GetBucketPolicyStatusResult implements Serializable, Cloneable {
   private PolicyStatus policyStatus;

   public PolicyStatus getPolicyStatus() {
      return this.policyStatus;
   }

   public void setPolicyStatus(PolicyStatus policyStatus) {
      this.policyStatus = policyStatus;
   }

   public GetBucketPolicyStatusResult withPolicyStatus(PolicyStatus policyStatus) {
      this.setPolicyStatus(policyStatus);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         GetBucketPolicyStatusResult that = (GetBucketPolicyStatusResult)o;
         return this.policyStatus != null ? this.policyStatus.equals(that.policyStatus) : that.policyStatus == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.policyStatus != null ? this.policyStatus.hashCode() : 0;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getPolicyStatus() != null) {
         sb.append("PolicyStatus: ").append(this.getPolicyStatus()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   public GetBucketPolicyStatusResult clone() {
      try {
         return (GetBucketPolicyStatusResult)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
