package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class OutputLocation implements Serializable, Cloneable {
   private S3Location s3;

   public S3Location getS3() {
      return this.s3;
   }

   public void setS3(S3Location s3) {
      this.s3 = s3;
   }

   public OutputLocation withS3(S3Location s3) {
      this.setS3(s3);
      return this;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof OutputLocation) {
         OutputLocation other = (OutputLocation)obj;
         if (other.getS3() == null ^ this.getS3() == null) {
            return false;
         } else {
            return other.getS3() == null || other.getS3().equals(this.getS3());
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getS3() == null ? 0 : this.getS3().hashCode());
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getS3() != null) {
         sb.append("S3: ").append(this.getS3());
      }

      sb.append("}");
      return sb.toString();
   }

   public OutputLocation clone() {
      try {
         return (OutputLocation)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
