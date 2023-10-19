package com.amazonaws.auth;

public enum SignatureVersion {
   V1("1"),
   V2("2");

   private String value;

   private SignatureVersion(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return this.value;
   }
}
