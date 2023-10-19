package com.amazonaws.services.s3.internal.crypto.keywrap;

public interface KeyWrapperProvider {
   InternalKeyWrapAlgorithm algorithm();

   KeyWrapper createKeyWrapper(KeyWrapperContext var1);
}
