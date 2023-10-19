package com.amazonaws.services.s3.internal.crypto.keywrap;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.amazonaws.services.s3.model.KMSEncryptionMaterials;
import com.amazonaws.util.BinaryUtils;
import java.nio.ByteBuffer;
import java.security.Key;

public final class KMSKeyWrapper implements KeyWrapper {
   private final KMSEncryptionMaterials encryptionMaterials;
   private final KMSKeyWrapperContext kmsKeyWrapperContext;

   private KMSKeyWrapper(KMSKeyWrapper.Builder b) {
      this.encryptionMaterials = this.validateNotNull(b.encryptionMaterials, "encryptionMaterials");
      this.kmsKeyWrapperContext = this.validateNotNull(b.kmsKeyWrapperContext, "kmsKeyWrapperContext");
   }

   public static KMSKeyWrapper.Builder builder() {
      return new KMSKeyWrapper.Builder();
   }

   public KMSEncryptionMaterials encryptionMaterials() {
      return this.encryptionMaterials;
   }

   public KMSKeyWrapperContext kmsKeyWrapperContext() {
      return this.kmsKeyWrapperContext;
   }

   @Override
   public byte[] unwrapCek(byte[] encryptedCek, Key key) {
      DecryptRequest kmsreq = new DecryptRequest()
         .withKeyId(this.encryptionMaterials.getCustomerMasterKeyId())
         .withEncryptionContext(this.kmsKeyWrapperContext.kmsMaterialsDescription())
         .withCiphertextBlob(ByteBuffer.wrap(encryptedCek));
      DecryptResult result = this.kmsKeyWrapperContext.kms().decrypt(kmsreq);
      return BinaryUtils.copyAllBytesFrom(result.getPlaintext());
   }

   @Override
   public byte[] wrapCek(byte[] plaintextCek, Key key) {
      EncryptRequest encryptRequest = new EncryptRequest()
         .withEncryptionContext(this.kmsKeyWrapperContext.kmsMaterialsDescription())
         .withKeyId(this.encryptionMaterials.getCustomerMasterKeyId())
         .withPlaintext(ByteBuffer.wrap(plaintextCek));
      if (this.kmsKeyWrapperContext.originalRequest() != null) {
         AmazonWebServiceRequest originalRequest = this.kmsKeyWrapperContext.originalRequest();
         encryptRequest.withGeneralProgressListener(originalRequest.getGeneralProgressListener())
            .withRequestMetricCollector(originalRequest.getRequestMetricCollector());
      }

      EncryptResult encryptResult = this.kmsKeyWrapperContext.kms().encrypt(encryptRequest);
      return BinaryUtils.copyAllBytesFrom(encryptResult.getCiphertextBlob());
   }

   private <T> T validateNotNull(T obj, String propertyName) {
      if (obj == null) {
         throw new NullPointerException("Error initializing KMSKeyWrapper: '" + propertyName + "' cannot be null");
      } else {
         return obj;
      }
   }

   public static final class Builder {
      private KMSEncryptionMaterials encryptionMaterials;
      private KMSKeyWrapperContext kmsKeyWrapperContext;

      private Builder() {
      }

      public KMSKeyWrapper.Builder encryptionMaterials(KMSEncryptionMaterials encryptionMaterials) {
         this.encryptionMaterials = encryptionMaterials;
         return this;
      }

      public KMSKeyWrapper.Builder kmsKeyWrapperContext(KMSKeyWrapperContext kmsKeyWrapperContext) {
         this.kmsKeyWrapperContext = kmsKeyWrapperContext;
         return this;
      }

      public KMSKeyWrapper build() {
         return new KMSKeyWrapper(this);
      }
   }
}
