package com.amazonaws.auth.policy;

public class Resource {
   private final String resource;
   private boolean isNotType;

   public Resource(String resource) {
      this.resource = resource;
   }

   public String getId() {
      return this.resource;
   }

   public boolean isNotType() {
      return this.isNotType;
   }

   public Resource withIsNotType(boolean isNotType) {
      this.isNotType = isNotType;
      return this;
   }
}
