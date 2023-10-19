package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AccessControlList implements Serializable, S3RequesterChargedResult {
   private static final long serialVersionUID = 8095040648034788376L;
   private Set<Grant> grantSet;
   private List<Grant> grantList;
   private Owner owner = null;
   private boolean isRequesterCharged;

   public Owner getOwner() {
      return this.owner;
   }

   public AccessControlList withOwner(Owner owner) {
      this.owner = owner;
      return this;
   }

   public void setOwner(Owner owner) {
      this.owner = owner;
   }

   public void grantPermission(Grantee grantee, Permission permission) {
      this.getGrantsAsList().add(new Grant(grantee, permission));
   }

   public void grantAllPermissions(Grant... grantsVarArg) {
      for(Grant gap : grantsVarArg) {
         this.grantPermission(gap.getGrantee(), gap.getPermission());
      }
   }

   public void revokeAllPermissions(Grantee grantee) {
      ArrayList<Grant> grantsToRemove = new ArrayList<>();

      for(Grant gap : this.getGrantsAsList()) {
         if (gap.getGrantee().equals(grantee)) {
            grantsToRemove.add(gap);
         }
      }

      this.grantList.removeAll(grantsToRemove);
   }

   @Deprecated
   public Set<Grant> getGrants() {
      this.checkState();
      if (this.grantSet == null) {
         if (this.grantList == null) {
            this.grantSet = new HashSet<>();
         } else {
            this.grantSet = new HashSet<>(this.grantList);
            this.grantList = null;
         }
      }

      return this.grantSet;
   }

   private void checkState() {
      if (this.grantSet != null && this.grantList != null) {
         throw new IllegalStateException("Both grant set and grant list cannot be null");
      }
   }

   public List<Grant> getGrantsAsList() {
      this.checkState();
      if (this.grantList == null) {
         if (this.grantSet == null) {
            this.grantList = new LinkedList<>();
         } else {
            this.grantList = new LinkedList<>(this.grantSet);
            this.grantSet = null;
         }
      }

      return this.grantList;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      result = 31 * result + (this.owner == null ? 0 : this.owner.hashCode());
      result = 31 * result + (this.grantSet == null ? 0 : this.grantSet.hashCode());
      return 31 * result + (this.grantList == null ? 0 : this.grantList.hashCode());
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
         AccessControlList other = (AccessControlList)obj;
         if (this.owner == null) {
            if (other.owner != null) {
               return false;
            }
         } else if (!this.owner.equals(other.owner)) {
            return false;
         }

         if (this.grantSet == null) {
            if (other.grantSet != null) {
               return false;
            }
         } else if (!this.grantSet.equals(other.grantSet)) {
            return false;
         }

         if (this.grantList == null) {
            if (other.grantList != null) {
               return false;
            }
         } else if (!this.grantList.equals(other.grantList)) {
            return false;
         }

         return true;
      }
   }

   @Override
   public String toString() {
      return "AccessControlList [owner=" + this.owner + ", grants=" + this.getGrantsAsList() + "]";
   }

   @Override
   public boolean isRequesterCharged() {
      return this.isRequesterCharged;
   }

   @Override
   public void setRequesterCharged(boolean isRequesterCharged) {
      this.isRequesterCharged = isRequesterCharged;
   }
}
