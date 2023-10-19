package com.amazonaws.services.s3.internal.crypto.keywrap;

import java.security.spec.MGF1ParameterSpec;

public enum Mgf1Scheme {
   MGF1_SHA1("SHA-1"),
   MGF1_SHA256("SHA-256");

   private final String mgf1ParameterSpec;

   private Mgf1Scheme(String mgf1ParameterSpec) {
      this.mgf1ParameterSpec = mgf1ParameterSpec;
   }

   public MGF1ParameterSpec getMgf1ParameterSpec() {
      return new MGF1ParameterSpec(this.mgf1ParameterSpec);
   }
}
