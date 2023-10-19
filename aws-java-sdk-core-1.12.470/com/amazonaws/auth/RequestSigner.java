package com.amazonaws.auth;

import com.amazonaws.SignableRequest;

public interface RequestSigner {
   void sign(SignableRequest<?> var1);
}
