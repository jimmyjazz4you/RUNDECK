package com.amazonaws.services.s3.internal.crypto.v2;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.internal.S3Direct;
import com.amazonaws.services.s3.internal.crypto.ContentCryptoScheme;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.S3ObjectId;

public class S3CryptoModuleAEStrict extends S3CryptoModuleAE {
   public S3CryptoModuleAEStrict(
      AWSKMS kms,
      S3Direct s3,
      AWSCredentialsProvider credentialsProvider,
      EncryptionMaterialsProvider encryptionMaterialsProvider,
      CryptoConfigurationV2 cryptoConfig
   ) {
      super(kms, s3, credentialsProvider, encryptionMaterialsProvider, cryptoConfig);
      if (cryptoConfig.getCryptoMode() != CryptoMode.StrictAuthenticatedEncryption) {
         throw new IllegalArgumentException();
      }
   }

   @Override
   protected final boolean isStrict() {
      return true;
   }

   @Override
   protected void securityCheck(ContentCryptoMaterial cekMaterial, S3ObjectId objectId, boolean isRangeGet) {
      if (!isRangeGet && !ContentCryptoScheme.AES_GCM.equals(cekMaterial.getContentCryptoScheme())) {
         throw new SecurityException(
            "S3 object [bucket: " + objectId.getBucket() + ", key: " + objectId.getKey() + "] not encrypted using authenticated encryption"
         );
      }
   }
}
