package com.amazonaws.services.s3;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;

@Deprecated
abstract class AmazonS3EncryptionClientParams extends AmazonS3ClientParams {
   abstract EncryptionMaterialsProvider getEncryptionMaterials();

   abstract CryptoConfiguration getCryptoConfiguration();

   abstract AWSKMS getKmsClient();
}
