package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class EmailAddressGrantee implements Grantee, Serializable {
   private String emailAddress = null;

   @Override
   public String getTypeIdentifier() {
      return "emailAddress";
   }

   public EmailAddressGrantee(String emailAddress) {
      this.setIdentifier(emailAddress);
   }

   @Override
   public void setIdentifier(String emailAddress) {
      this.emailAddress = emailAddress;
   }

   @Override
   public String getIdentifier() {
      return this.emailAddress;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      return 31 * result + (this.emailAddress == null ? 0 : this.emailAddress.hashCode());
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         EmailAddressGrantee other = (EmailAddressGrantee)obj;
         if (this.emailAddress == null) {
            if (other.emailAddress != null) {
               return false;
            }
         } else if (!this.emailAddress.equals(other.emailAddress)) {
            return false;
         }

         return true;
      }
   }

   @Override
   public String toString() {
      return this.emailAddress;
   }
}
