package com.amazonaws.services.s3.internal.crypto.keywrap;

import java.security.Key;

public interface KeyWrapper {
   byte[] unwrapCek(byte[] var1, Key var2);

   byte[] wrapCek(byte[] var1, Key var2);
}
