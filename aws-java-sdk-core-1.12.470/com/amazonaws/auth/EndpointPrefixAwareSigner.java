package com.amazonaws.auth;

public interface EndpointPrefixAwareSigner extends Signer {
   void setEndpointPrefix(String var1);
}
