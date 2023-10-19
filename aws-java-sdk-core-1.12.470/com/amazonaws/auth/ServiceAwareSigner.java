package com.amazonaws.auth;

public interface ServiceAwareSigner extends Signer {
   void setServiceName(String var1);
}
