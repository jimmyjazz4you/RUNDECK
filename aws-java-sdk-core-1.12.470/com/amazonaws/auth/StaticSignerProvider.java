package com.amazonaws.auth;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.internal.auth.SignerProviderContext;

@SdkProtectedApi
public class StaticSignerProvider extends SignerProvider {
   private final Signer signer;

   public StaticSignerProvider(Signer signer) {
      this.signer = signer;
   }

   @Override
   public Signer getSigner(SignerProviderContext context) {
      return this.signer;
   }
}
