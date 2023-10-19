package com.amazonaws.auth;

import com.amazonaws.SignableRequest;

public final class SignerAsRequestSigner implements RequestSigner {
   private final Signer signer;
   private final AWSCredentialsProvider credentialsProvider;

   public SignerAsRequestSigner(Signer signer, AWSCredentialsProvider credentialsProvider) {
      this.signer = signer;
      this.credentialsProvider = credentialsProvider;
   }

   @Override
   public void sign(SignableRequest<?> request) {
      this.signer.sign(request, this.credentialsProvider.getCredentials());
   }
}
