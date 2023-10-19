package com.amazonaws.services.s3;

import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;

@Deprecated
public final class AmazonS3EncryptionClientBuilder extends AmazonS3Builder<AmazonS3EncryptionClientBuilder, AmazonS3Encryption> {
   private EncryptionMaterialsProvider encryptionMaterials;
   private CryptoConfiguration cryptoConfig;
   private AWSKMS kms;

   public static AmazonS3EncryptionClientBuilder standard() {
      return new AmazonS3EncryptionClientBuilder();
   }

   public static AmazonS3Encryption defaultClient() {
      return (AmazonS3Encryption)standard().build();
   }

   public void setEncryptionMaterials(EncryptionMaterialsProvider encryptionMaterials) {
      this.encryptionMaterials = encryptionMaterials;
   }

   public AmazonS3EncryptionClientBuilder withEncryptionMaterials(EncryptionMaterialsProvider encryptionMaterials) {
      this.setEncryptionMaterials(encryptionMaterials);
      return this;
   }

   public void setCryptoConfiguration(CryptoConfiguration cryptoConfig) {
      this.cryptoConfig = cryptoConfig;
   }

   public AmazonS3EncryptionClientBuilder withCryptoConfiguration(CryptoConfiguration cryptoConfig) {
      this.setCryptoConfiguration(cryptoConfig);
      return this;
   }

   public void setKms(AWSKMS kms) {
      this.kms = kms;
   }

   public AmazonS3EncryptionClientBuilder withKmsClient(AWSKMS kms) {
      this.setKms(kms);
      return this;
   }

   protected AmazonS3Encryption build(AwsSyncClientParams clientParams) {
      return new AmazonS3EncryptionClient(
         new AmazonS3EncryptionClientParamsWrapper(
            clientParams,
            this.resolveS3ClientOptions(),
            this.encryptionMaterials,
            this.cryptoConfig != null ? this.cryptoConfig : new CryptoConfiguration(),
            this.kms
         )
      );
   }
}
