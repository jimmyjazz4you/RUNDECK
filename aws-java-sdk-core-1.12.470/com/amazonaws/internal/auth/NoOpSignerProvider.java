package com.amazonaws.internal.auth;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.NoOpSigner;
import com.amazonaws.auth.Signer;

@SdkInternalApi
public class NoOpSignerProvider extends SignerProvider {
   private Signer signer = new NoOpSigner();

   @Override
   public Signer getSigner(SignerProviderContext context) {
      return this.signer;
   }
}
