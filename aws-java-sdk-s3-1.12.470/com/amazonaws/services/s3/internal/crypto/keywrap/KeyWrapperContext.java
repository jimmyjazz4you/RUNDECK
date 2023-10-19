package com.amazonaws.services.s3.internal.crypto.keywrap;

import com.amazonaws.services.s3.internal.crypto.ContentCryptoScheme;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import java.security.Provider;
import java.security.SecureRandom;

public class KeyWrapperContext {
   private final byte[] cekSecured;
   private final EncryptionMaterials materials;
   private final ContentCryptoScheme contentCryptoScheme;
   private final KMSKeyWrapperContext kmsKeyWrapperContext;
   private final InternalKeyWrapAlgorithm internalKeyWrapAlgorithm;
   private final Provider cryptoProvider;
   private final SecureRandom secureRandom;

   private KeyWrapperContext(KeyWrapperContext.Builder b) {
      this.cekSecured = b.cekSecured;
      this.internalKeyWrapAlgorithm = b.internalKeyWrapAlgorithm;
      this.materials = b.materials;
      this.contentCryptoScheme = b.contentCryptoScheme;
      this.kmsKeyWrapperContext = b.kmsKeyWrapperContext;
      this.cryptoProvider = b.cryptoProvider;
      this.secureRandom = b.secureRandom;
   }

   public static KeyWrapperContext.Builder builder() {
      return new KeyWrapperContext.Builder();
   }

   public byte[] cekSecured() {
      return this.cekSecured;
   }

   public InternalKeyWrapAlgorithm internalKeyWrapAlgorithm() {
      return this.internalKeyWrapAlgorithm;
   }

   public EncryptionMaterials materials() {
      return this.materials;
   }

   public ContentCryptoScheme contentCryptoScheme() {
      return this.contentCryptoScheme;
   }

   public KMSKeyWrapperContext kmsKeyWrapperContext() {
      return this.kmsKeyWrapperContext;
   }

   public Provider cryptoProvider() {
      return this.cryptoProvider;
   }

   public SecureRandom secureRandom() {
      return this.secureRandom;
   }

   public static class Builder {
      private byte[] cekSecured;
      private EncryptionMaterials materials;
      private ContentCryptoScheme contentCryptoScheme;
      private KMSKeyWrapperContext kmsKeyWrapperContext;
      private InternalKeyWrapAlgorithm internalKeyWrapAlgorithm;
      private Provider cryptoProvider;
      private SecureRandom secureRandom;

      public KeyWrapperContext.Builder cekSecured(byte[] cekSecured) {
         this.cekSecured = cekSecured;
         return this;
      }

      public KeyWrapperContext.Builder internalKeyWrapAlgorithm(InternalKeyWrapAlgorithm keyWrapAlgo) {
         this.internalKeyWrapAlgorithm = keyWrapAlgo;
         return this;
      }

      public KeyWrapperContext.Builder materials(EncryptionMaterials materials) {
         this.materials = materials;
         return this;
      }

      public KeyWrapperContext.Builder contentCryptoScheme(ContentCryptoScheme contentCryptoScheme) {
         this.contentCryptoScheme = contentCryptoScheme;
         return this;
      }

      public KeyWrapperContext.Builder kmsKeyWrapperContext(KMSKeyWrapperContext kmsKeyWrapperContext) {
         this.kmsKeyWrapperContext = kmsKeyWrapperContext;
         return this;
      }

      public KeyWrapperContext.Builder cryptoProvider(Provider cryptoProvider) {
         this.cryptoProvider = cryptoProvider;
         return this;
      }

      public KeyWrapperContext.Builder secureRandom(SecureRandom secureRandom) {
         this.secureRandom = secureRandom;
         return this;
      }

      public KeyWrapperContext build() {
         return new KeyWrapperContext(this);
      }
   }
}
