package com.amazonaws.services.s3;

import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;

@Deprecated
@Immutable
@SdkInternalApi
public final class AmazonS3EncryptionClientParamsWrapper extends AmazonS3EncryptionClientParams {
   private final EncryptionMaterialsProvider encryptionMaterials;
   private final CryptoConfiguration cryptoConfiguration;
   private final AWSKMS kms;
   private final AwsSyncClientParams getClientParams;
   private final S3ClientOptions getS3ClientOptions;

   AmazonS3EncryptionClientParamsWrapper(
      AwsSyncClientParams getClientParams,
      S3ClientOptions getS3ClientOptions,
      EncryptionMaterialsProvider encryptionMaterials,
      CryptoConfiguration cryptoConfiguration,
      AWSKMS kms
   ) {
      this.encryptionMaterials = encryptionMaterials;
      this.cryptoConfiguration = cryptoConfiguration;
      this.kms = kms;
      this.getClientParams = getClientParams;
      this.getS3ClientOptions = getS3ClientOptions;
   }

   @Override
   EncryptionMaterialsProvider getEncryptionMaterials() {
      return this.encryptionMaterials;
   }

   @Override
   CryptoConfiguration getCryptoConfiguration() {
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
