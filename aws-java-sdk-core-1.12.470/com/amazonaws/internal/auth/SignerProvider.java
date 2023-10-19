package com.amazonaws.internal.auth;

import com.amazonaws.auth.Signer;

public abstract class SignerProvider {
   public abstract Signer getSigner(SignerProviderContext var1);
}
