package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.internal.S3Direct;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutInstructionFileRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.UploadObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class CryptoModuleDispatcher extends S3CryptoModule<MultipartUploadContext> {
   private final CryptoMode defaultCryptoMode;
   private final S3CryptoModuleEO eo;
   private final S3CryptoModuleAE ae;

   public CryptoModuleDispatcher(
      AWSKMS kms,
      S3Direct s3,
      AWSCredentialsProvider credentialsProvider,
      EncryptionMaterialsProvider encryptionMaterialsProvider,
      CryptoConfiguration cryptoConfig
   ) {
      cryptoConfig = cryptoConfig.clone();
      CryptoMode cryptoMode = cryptoConfig.getCryptoMode();
      if (cryptoMode == null) {
         cryptoMode = CryptoMode.EncryptionOnly;
         cryptoConfig.setCryptoMode(cryptoMode);
      }

      cryptoConfig = cryptoConfig.readOnly();
      this.defaultCryptoMode = cryptoConfig.getCryptoMode();
      switch(this.defaultCryptoMode) {
         case StrictAuthenticatedEncryption:
            this.ae = new S3CryptoModuleAEStrict(kms, s3, credentialsProvider, encryptionMaterialsProvider, cryptoConfig);
            this.eo = null;
            break;
         case AuthenticatedEncryption:
            this.ae = new S3CryptoModuleAE(kms, s3, credentialsProvider, encryptionMaterialsProvider, cryptoConfig);
            this.eo = null;
            break;
         case EncryptionOnly:
            this.eo = new S3CryptoModuleEO(kms, s3, credentialsProvider, encryptionMaterialsProvider, cryptoConfig);
            CryptoConfiguration aeConfig = cryptoConfig.clone();

            try {
               aeConfig.setCryptoMode(CryptoMode.AuthenticatedEncryption);
            } catch (UnsupportedOperationException var9) {
            }

            this.ae = new S3CryptoModuleAE(kms, s3, credentialsProvider, encryptionMaterialsProvider, aeConfig.readOnly());
            break;
         default:
            throw new IllegalStateException();
      }
   }

   @Override
   public PutObjectResult putObjectSecurely(PutObjectRequest putObjectRequest) {
      return this.defaultCryptoMode == CryptoMode.EncryptionOnly ? this.eo.putObjectSecurely(putObjectRequest) : this.ae.putObjectSecurely(putObjectRequest);
   }

   @Override
   public S3Object getObjectSecurely(GetObjectRequest req) {
      return this.ae.getObjectSecurely(req);
   }

   @Override
   public ObjectMetadata getObjectSecurely(GetObjectRequest req, File destinationFile) {
      return this.ae.getObjectSecurely(req, destinationFile);
   }

   @Override
   public CompleteMultipartUploadResult completeMultipartUploadSecurely(CompleteMultipartUploadRequest req) throws SdkClientException, AmazonServiceException {
      return this.defaultCryptoMode == CryptoMode.EncryptionOnly ? this.eo.completeMultipartUploadSecurely(req) : this.ae.completeMultipartUploadSecurely(req);
   }

   @Override
   public void abortMultipartUploadSecurely(AbortMultipartUploadRequest req) {
      if (this.defaultCryptoMode == CryptoMode.EncryptionOnly) {
         this.eo.abortMultipartUploadSecurely(req);
      } else {
         this.ae.abortMultipartUploadSecurely(req);
      }
   }

   @Override
   public InitiateMultipartUploadResult initiateMultipartUploadSecurely(InitiateMultipartUploadRequest req) throws SdkClientException, AmazonServiceException {
      return this.defaultCryptoMode == CryptoMode.EncryptionOnly ? this.eo.initiateMultipartUploadSecurely(req) : this.ae.initiateMultipartUploadSecurely(req);
   }

   @Override
   public UploadPartResult uploadPartSecurely(UploadPartRequest req) throws SdkClientException, AmazonServiceException {
      return this.defaultCryptoMode == CryptoMode.EncryptionOnly ? this.eo.uploadPartSecurely(req) : this.ae.uploadPartSecurely(req);
   }

   @Override
   public CopyPartResult copyPartSecurely(CopyPartRequest req) {
      return this.defaultCryptoMode == CryptoMode.EncryptionOnly ? this.eo.copyPartSecurely(req) : this.ae.copyPartSecurely(req);
   }

   @Override
   public PutObjectResult putInstructionFileSecurely(PutInstructionFileRequest req) {
      return this.defaultCryptoMode == CryptoMode.EncryptionOnly ? this.eo.putInstructionFileSecurely(req) : this.ae.putInstructionFileSecurely(req);
   }

   @Override
   public void putLocalObjectSecurely(UploadObjectRequest req, String uploadId, OutputStream os) throws IOException {
      if (this.defaultCryptoMode == CryptoMode.EncryptionOnly) {
         this.eo.putLocalObjectSecurely(req, uploadId, os);
      } else {
         this.ae.putLocalObjectSecurely(req, uploadId, os);
      }
   }
}
