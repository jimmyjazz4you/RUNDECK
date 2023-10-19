package com.amazonaws.internal.config;

import com.amazonaws.annotation.Immutable;

@Immutable
public class SignerConfig {
   private final String signerType;

   SignerConfig(String signerType) {
      this.signerType = signerType;
   }

   SignerConfig(SignerConfig from) {
      this.signerType = from.getSignerType();
   }

   public String getSignerType() {
      return this.signerType;
   }

   @Override
   public String toString() {
      return this.signerType;
   }
}
