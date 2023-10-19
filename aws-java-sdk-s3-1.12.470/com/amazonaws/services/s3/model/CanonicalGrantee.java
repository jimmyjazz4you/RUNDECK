package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class CanonicalGrantee implements Grantee, Serializable {
   private String id = null;
   private String displayName = null;

   @Override
   public String getTypeIdentifier() {
      return "id";
   }

   public CanonicalGrantee(String identifier) {
      this.setIdentifier(identifier);
   }

   @Override
   public void setIdentifier(String id) {
      this.id = id;
   }

   @Override
   public String getIdentifier() {
      return this.id;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof CanonicalGrantee) {
         CanonicalGrantee canonicalGrantee = (CanonicalGrantee)obj;
         return this.id.equals(canonicalGrantee.id);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }
}
