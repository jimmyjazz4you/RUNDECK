package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class AbortIncompleteMultipartUpload implements Serializable {
   private int daysAfterInitiation;

   public int getDaysAfterInitiation() {
      return this.daysAfterInitiation;
   }

   public void setDaysAfterInitiation(int daysAfterInitiation) {
      this.daysAfterInitiation = daysAfterInitiation;
   }

   public AbortIncompleteMultipartUpload withDaysAfterInitiation(int daysAfterInitiation) {
      this.setDaysAfterInitiation(daysAfterInitiation);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         AbortIncompleteMultipartUpload that = (AbortIncompleteMultipartUpload)o;
         return this.daysAfterInitiation == that.daysAfterInitiation;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.daysAfterInitiation;
   }

   protected AbortIncompleteMultipartUpload clone() throws CloneNotSupportedException {
      try {
         return (AbortIncompleteMultipartUpload)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
