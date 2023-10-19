package com.amazonaws.services.s3.internal.crypto.keywrap;

import com.amazonaws.services.s3.internal.crypto.CryptoUtils;

public final class RsaOaepKeyWrapperProvider implements KeyWrapperProvider {
   private static final RsaOaepKeyWrapperProvider DEFAULT_SHA1 = new RsaOaepKeyWrapperProvider(InternalKeyWrapAlgorithm.RSA_OAEP_SHA1);
   private final InternalKeyWrapAlgorithm cryptoKeyWrapAlgorithm;

   private RsaOaepKeyWrapperProvider(InternalKeyWrapAlgorithm cryptoKeyWrapAlgorithm) {
      this.cryptoKeyWrapAlgorithm = cryptoKeyWrapAlgorithm;
   }

   public static RsaOaepKeyWrapperProvider createSha1() {
      return DEFAULT_SHA1;
   }

   @Override
   public InternalKeyWrapAlgorithm algorithm() {
      return this.cryptoKeyWrapAlgorithm;
   }

   @Override
   public KeyWrapper createKeyWrapper(KeyWrapperContext keyWrapperContext) {
      String remappedCekAlgorithm = CryptoUtils.normalizeContentAlgorithmForValidation(keyWrapperContext.contentCryptoScheme().getCipherAlgorithm());
      return RsaOaepKeyWrapper.builder()
         .cipherProvider(CipherProvider.create(RsaOaepKeyWrapper.cipherAlgorithm(), keyWrapperContext.cryptoProvider()))
         .cryptoKeyWrapAlgorithm(this.cryptoKeyWrapAlgorithm)
         .cekAlgorithm(remappedCekAlgorithm)
         .build();
   }
}
