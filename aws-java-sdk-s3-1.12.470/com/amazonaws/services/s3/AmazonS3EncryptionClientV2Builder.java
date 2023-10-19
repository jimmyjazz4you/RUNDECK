package com.amazonaws.services.s3;

import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;

public final class AmazonS3EncryptionClientV2Builder extends AmazonS3Builder<AmazonS3EncryptionClientV2Builder, AmazonS3EncryptionV2> {
   private EncryptionMaterialsProvider encryptionMaterialsProvider;
   private CryptoConfigurationV2 cryptoConfig;
   private AWSKMS kmsClient;

   public static AmazonS3EncryptionClientV2Builder standard() {
      return new AmazonS3EncryptionClientV2Builder();
   }

   public void setEncryptionMaterialsProvider(EncryptionMaterialsProvider encryptionMaterialsProvider) {
      this.encryptionMaterialsProvider = encryptionMaterialsProvider;
   }

   public AmazonS3EncryptionClientV2Builder withEncryptionMaterialsProvider(EncryptionMaterialsProvider encryptionMaterialsProvider) {
      this.setEncryptionMaterialsProvider(encryptionMaterialsProvider);
      return this;
   }

   public void setCryptoConfiguration(CryptoConfigurationV2 cryptoConfig) {
      this.cryptoConfig = cryptoConfig;
   }

   public AmazonS3EncryptionClientV2Builder withCryptoConfiguration(CryptoConfigurationV2 cryptoConfig) {
      this.setCryptoConfiguration(cryptoConfig);
      return this;
   }

   public void setKmsClient(AWSKMS kmsClient) {
      this.kmsClient = kmsClient;
   }

   public AmazonS3EncryptionClientV2Builder withKmsClient(AWSKMS kmsClient) {
      this.setKmsClient(kmsClient);
      return this;
   }

   protected AmazonS3EncryptionV2 build(AwsSyncClientParams clientParams) {
      return new AmazonS3EncryptionClientV2(
         new AmazonS3EncryptionClientV2ParamsWrapper(
            clientParams,
            this.resolveS3ClientOptions(),
            this.encryptionMaterialsProvider,
            this.cryptoConfig != null ? this.cryptoConfig : new CryptoConfigurationV2(),
            this.kmsClient
         )
      );
   }
}
