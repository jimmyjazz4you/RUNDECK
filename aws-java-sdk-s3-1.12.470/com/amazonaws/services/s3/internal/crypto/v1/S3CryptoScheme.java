package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.services.s3.internal.crypto.ContentCryptoScheme;
import com.amazonaws.services.s3.model.CryptoMode;

public final class S3CryptoScheme {
   static final String AES = "AES";
   static final String RSA = "RSA";
   private final S3KeyWrapScheme kwScheme;
   private final ContentCryptoScheme contentCryptoScheme;

   private S3CryptoScheme(ContentCryptoScheme contentCryptoScheme, S3KeyWrapScheme kwScheme) {
      this.contentCryptoScheme = contentCryptoScheme;
      this.kwScheme = kwScheme;
   }

   public ContentCryptoScheme getContentCryptoScheme() {
      return this.contentCryptoScheme;
   }

   public S3KeyWrapScheme getKeyWrapScheme() {
      return this.kwScheme;
   }

   public static boolean isAesGcm(String cipherAlgorithm) {
      return ContentCryptoScheme.AES_GCM.getCipherAlgorithm().equals(cipherAlgorithm);
   }

   public static S3CryptoScheme from(CryptoMode mode) {
      switch(mode) {
         case EncryptionOnly:
            return new S3CryptoScheme(ContentCryptoScheme.AES_CBC, S3KeyWrapScheme.NONE);
         case AuthenticatedEncryption:
         case StrictAuthenticatedEncryption:
            return new S3CryptoScheme(ContentCryptoScheme.AES_GCM, new S3KeyWrapScheme());
         default:
            throw new IllegalStateException();
      }
   }
}
