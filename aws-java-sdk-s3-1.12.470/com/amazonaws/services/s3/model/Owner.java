package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class Owner implements Serializable {
   private static final long serialVersionUID = -8916731456944569115L;
   private String displayName;
   private String id;

   public Owner() {
   }

   public Owner(String id, String displayName) {
      this.id = id;
      this.displayName = displayName;
   }

   @Override
   public String toString() {
      return "S3Owner [name=" + this.getDisplayName() + ",id=" + this.getId() + "]";
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String name) {
      this.displayName = name;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof Owner)) {
         return false;
      } else {
         Owner otherOwner = (Owner)obj;
         String otherOwnerId = otherOwner.getId();
         String otherOwnerName = otherOwner.getDisplayName();
         String thisOwnerId = this.getId();
         String thisOwnerName = this.getDisplayName();
         if (otherOwnerId == null) {
            otherOwnerId = "";
         }

         if (otherOwnerName == null) {
            otherOwnerName = "";
         }

         if (thisOwnerId == null) {
            thisOwnerId = "";
         }

         if (thisOwnerName == null) {
            thisOwnerName = "";
         }

         return otherOwnerId.equals(thisOwnerId) && otherOwnerName.equals(thisOwnerName);
      }
   }

   @Override
   public int hashCode() {
      return this.id != null ? this.id.hashCode() : 0;
   }
}
