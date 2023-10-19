package com.amazonaws.services.s3;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;

abstract class AmazonS3EncryptionClientV2Params extends AmazonS3ClientParams {
   abstract EncryptionMaterialsProvider getEncryptionMaterialsProvider();

   abstract CryptoConfigurationV2 getCryptoConfiguration();

   abstract AWSKMS getKmsClient();
}
