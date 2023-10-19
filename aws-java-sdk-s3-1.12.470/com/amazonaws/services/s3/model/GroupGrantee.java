package com.amazonaws.services.s3.model;

public enum GroupGrantee implements Grantee {
   AllUsers("http://acs.amazonaws.com/groups/global/AllUsers"),
   AuthenticatedUsers("http://acs.amazonaws.com/groups/global/AuthenticatedUsers"),
   LogDelivery("http://acs.amazonaws.com/groups/s3/LogDelivery");

   private String groupUri;

   @Override
   public String getTypeIdentifier() {
      return "uri";
   }

   private GroupGrantee(String groupUri) {
      this.groupUri = groupUri;
   }

   @Override
   public String getIdentifier() {
      return this.groupUri;
   }

   @Override
   public void setIdentifier(String id) {
      throw new UnsupportedOperationException("Group grantees have preset identifiers that cannot be modified.");
   }

   @Override
   public String toString() {
      return "GroupGrantee [" + this.groupUri + "]";
   }

   public static GroupGrantee parseGroupGrantee(String groupUri) {
      for(GroupGrantee grantee : values()) {
         if (grantee.groupUri.equals(groupUri)) {
            return grantee;
         }
      }

      return null;
   }
}
