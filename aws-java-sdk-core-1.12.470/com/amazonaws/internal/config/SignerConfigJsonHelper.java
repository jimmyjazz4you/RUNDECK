package com.amazonaws.internal.config;

public class SignerConfigJsonHelper implements Builder<SignerConfig> {
   private String signerType;

   public SignerConfigJsonHelper() {
   }

   public SignerConfigJsonHelper(String signerType) {
      this.signerType = signerType;
   }

   public String getSignerType() {
      return this.signerType;
   }

   public void setSignerType(String signerType) {
      this.signerType = signerType;
   }

   public SignerConfig build() {
      return new SignerConfig(this.signerType);
   }
}
