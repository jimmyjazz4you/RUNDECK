package com.amazonaws.services.s3.internal.crypto.keywrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KeyWrapperFactory {
   private static final KeyWrapperFactory DEFAULT = builder()
      .addKeyWrapper(AesGcmKeyWrapperProvider.create())
      .addKeyWrapper(RsaOaepKeyWrapperProvider.createSha1())
      .addKeyWrapper(KMSKeyWrapperProvider.create())
      .build();
   private final Map<InternalKeyWrapAlgorithm, KeyWrapperProvider> keyWrapperProviderMap;

   private KeyWrapperFactory(KeyWrapperFactory.Builder b) {
      Map<InternalKeyWrapAlgorithm, KeyWrapperProvider> mutableKeyWrapperMap = new HashMap<>();

      for(KeyWrapperProvider keyWrapper : b.keyWrapperProviders) {
         mutableKeyWrapperMap.put(keyWrapper.algorithm(), keyWrapper);
      }

      this.keyWrapperProviderMap = Collections.unmodifiableMap(mutableKeyWrapperMap);
   }

   public static KeyWrapperFactory defaultInstance() {
      return DEFAULT;
   }

   public static KeyWrapperFactory.Builder builder() {
      return new KeyWrapperFactory.Builder();
   }

   public KeyWrapper createKeyWrapper(KeyWrapperContext context) {
      KeyWrapperProvider keyWrapperProvider = this.keyWrapperProviderMap.get(context.internalKeyWrapAlgorithm());
      if (keyWrapperProvider == null) {
         throw new SecurityException("A key wrapping algorithm could not be found for '" + context.internalKeyWrapAlgorithm() + "'");
      } else {
         return keyWrapperProvider.createKeyWrapper(context);
      }
   }

   public static class Builder {
      private Collection<KeyWrapperProvider> keyWrapperProviders;

      public KeyWrapperFactory.Builder addKeyWrapper(KeyWrapperProvider keyWrapperProvider) {
         if (this.keyWrapperProviders == null) {
            this.keyWrapperProviders = new ArrayList<>();
         }

         this.keyWrapperProviders.add(keyWrapperProvider);
         return this;
      }

      public KeyWrapperFactory build() {
         return new KeyWrapperFactory(this);
      }
   }
}
