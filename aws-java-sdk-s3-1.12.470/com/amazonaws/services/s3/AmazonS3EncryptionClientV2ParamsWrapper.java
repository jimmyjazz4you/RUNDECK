package com.amazonaws.services.s3;

import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;

@Immutable
@SdkInternalApi
public final class AmazonS3EncryptionClientV2ParamsWrapper extends AmazonS3EncryptionClientV2Params {
   private final EncryptionMaterialsProvider encryptionMaterials;
   private final CryptoConfigurationV2 cryptoConfiguration;
   private final AWSKMS kms;
   private final AwsSyncClientParams getClientParams;
   private final S3ClientOptions getS3ClientOptions;

   AmazonS3EncryptionClientV2ParamsWrapper(
      AwsSyncClientParams getClientParams,
      S3ClientOptions getS3ClientOptions,
      EncryptionMaterialsProvider encryptionMaterials,
      CryptoConfigurationV2 cryptoConfiguration,
      AWSKMS kms
   ) {
      this.encryptionMaterials = encryptionMaterials;
      this.cryptoConfiguration = cryptoConfiguration;
      this.kms = kms;
      this.getClientParams = getClientParams;
      this.getS3ClientOptions = getS3ClientOptions;
   }

   @Override
   EncryptionMaterialsProvider getEncryptionMaterialsProvider() {
      return this.encryptionMaterials;
   }

   @Override
   CryptoConfigurationV2 getCryptoConfiguration() {
      return this.cryptoConfiguration;
   }

   @Override
   AWSKMS getKmsClient() {
      return this.kms;
   }

   @Override
   public AwsSyncClientParams getClientParams() {
      return this.getClientParams;
   }

   @Override
   public S3ClientOptions getS3ClientOptions() {
      return this.getS3ClientOptions;
   }
}
