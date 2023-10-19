package com.amazonaws.auth;

import com.amazonaws.SignableRequest;

public interface Signer {
   void sign(SignableRequest<?> var1, AWSCredentials var2);
}
