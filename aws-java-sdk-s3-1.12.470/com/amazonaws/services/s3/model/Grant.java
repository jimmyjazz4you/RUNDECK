package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class Grant implements Serializable {
   private Grantee grantee = null;
   private Permission permission = null;

   public Grant(Grantee grantee, Permission permission) {
      this.grantee = grantee;
      this.permission = permission;
   }

   public Grantee getGrantee() {
      return this.grantee;
   }

   public Permission getPermission() {
      return this.permission;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      result = 31 * result + (this.grantee == null ? 0 : this.grantee.hashCode());
      return 31 * result + (this.permission == null ? 0 : this.permission.hashCode());
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
         Grant other = (Grant)obj;
         if (this.grantee == null) {
            if (other.grantee != null) {
               return false;
            }
         } else if (!this.grantee.equals(other.grantee)) {
            return false;
         }

         return this.permission == other.permission;
      }
   }

   @Override
   public String toString() {
      return "Grant [grantee=" + this.grantee + ", permission=" + this.permission + "]";
   }
}
