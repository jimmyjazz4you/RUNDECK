package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.internal.SdkFilterInputStream;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.internal.S3Direct;
import com.amazonaws.services.s3.internal.crypto.ByteRangeCapturingInputStream;
import com.amazonaws.services.s3.internal.crypto.CipherLite;
import com.amazonaws.services.s3.internal.crypto.CipherLiteInputStream;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.UploadPartRequest;
import java.io.File;

class S3CryptoModuleEO extends S3CryptoModuleBase<MultipartUploadCbcContext> {
   S3CryptoModuleEO(
      AWSKMS kms,
      S3Direct s3,
      AWSCredentialsProvider credentialsProvider,
      EncryptionMaterialsProvider encryptionMaterialsProvider,
      CryptoConfiguration cryptoConfig
   ) {
      super(kms, s3, credentialsProvider, encryptionMaterialsProvider, cryptoConfig);
      if (cryptoConfig.getCryptoMode() != CryptoMode.EncryptionOnly) {
         throw new IllegalArgumentException();
      }
   }

   S3CryptoModuleEO(S3Direct s3, EncryptionMaterialsProvider encryptionMaterialsProvider, CryptoConfiguration cryptoConfig) {
      this(null, s3, new DefaultAWSCredentialsProviderChain(), encryptionMaterialsProvider, cryptoConfig);
   }

   S3CryptoModuleEO(AWSKMS kms, S3Direct s3, EncryptionMaterialsProvider encryptionMaterialsProvider, CryptoConfiguration cryptoConfig) {
      this(kms, s3, new DefaultAWSCredentialsProviderChain(), encryptionMaterialsProvider, cryptoConfig);
   }

   @Override
   public S3Object getObjectSecurely(GetObjectRequest getObjectRequest) {
      throw new IllegalStateException();
   }

   @Override
   public ObjectMetadata getObjectSecurely(GetObjectRequest getObjectRequest, File destinationFile) {
      throw new IllegalStateException();
   }

   final MultipartUploadCbcContext newUploadContext(InitiateMultipartUploadRequest req, ContentCryptoMaterial cekMaterial) {
      MultipartUploadCbcContext encryptedUploadContext = new MultipartUploadCbcContext(req.getBucketName(), req.getKey(), cekMaterial);
      byte[] iv = cekMaterial.getCipherLite().getIV();
      encryptedUploadContext.setNextInitializationVector(iv);
      return encryptedUploadContext;
   }

   final void updateUploadContext(MultipartUploadCbcContext uploadContext, SdkFilterInputStream is) {
      ByteRangeCapturingInputStream bis = (ByteRangeCapturingInputStream)is;
      uploadContext.setNextInitializationVector(bis.getBlock());
   }

   final ByteRangeCapturingInputStream wrapForMultipart(CipherLiteInputStream is, long partSize) {
      int blockSize = this.contentCryptoScheme.getBlockSizeInBytes();
      return new ByteRangeCapturingInputStream(is, partSize - (long)blockSize, partSize);
   }

   @Override
   final long computeLastPartSize(UploadPartRequest request) {
      long plaintextLength;
      if (request.getFile() != null) {
         if (request.getPartSize() > 0L) {
            plaintextLength = request.getPartSize();
         } else {
            plaintextLength = request.getFile().length();
         }
      } else {
         if (request.getInputStream() == null) {
            return -1L;
         }

         plaintextLength = request.getPartSize();
      }

      long cipherBlockSize = (long)this.contentCryptoScheme.getBlockSizeInBytes();
      long offset = cipherBlockSize - plaintextLength % cipherBlockSize;
      return plaintextLength + offset;
   }

   final CipherLite cipherLiteForNextPart(MultipartUploadCbcContext uploadContext) {
      CipherLite cipherLite = uploadContext.getCipherLite();
      byte[] nextIV = uploadContext.getNextInitializationVector();
      return cipherLite.createUsingIV(nextIV);
   }

   @Override
   protected final long ciphertextLength(long plaintextLength) {
      long cipherBlockSize = (long)this.contentCryptoScheme.getBlockSizeInBytes();
      long offset = cipherBlockSize - plaintextLength % cipherBlockSize;
      return plaintextLength + offset;
   }
}
