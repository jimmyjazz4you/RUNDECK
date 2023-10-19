package com.amazonaws.auth;

import com.amazonaws.SignableRequest;

public class NoOpSigner implements Signer {
   @Override
   public void sign(SignableRequest<?> request, AWSCredentials credentials) {
   }
}
