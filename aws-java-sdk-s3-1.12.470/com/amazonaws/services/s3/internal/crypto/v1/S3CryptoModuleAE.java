package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.internal.SdkFilterInputStream;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.s3.internal.S3Direct;
import com.amazonaws.services.s3.internal.crypto.AdjustedRangeInputStream;
import com.amazonaws.services.s3.internal.crypto.CipherLite;
import com.amazonaws.services.s3.internal.crypto.CipherLiteInputStream;
import com.amazonaws.services.s3.internal.crypto.ContentCryptoScheme;
import com.amazonaws.services.s3.internal.crypto.CryptoRuntime;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.EncryptedGetObjectRequest;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.ExtraMaterialsDescription;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectId;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.json.Jackson;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

class S3CryptoModuleAE extends S3CryptoModuleBase<MultipartUploadCryptoContext> {
   S3CryptoModuleAE(
      AWSKMS kms,
      S3Direct s3,
      AWSCredentialsProvider credentialsProvider,
      EncryptionMaterialsProvider encryptionMaterialsProvider,
      CryptoConfiguration cryptoConfig
   ) {
      super(kms, s3, credentialsProvider, encryptionMaterialsProvider, cryptoConfig);
      CryptoMode mode = cryptoConfig.getCryptoMode();
      if (mode != CryptoMode.StrictAuthenticatedEncryption && mode != CryptoMode.AuthenticatedEncryption) {
         throw new IllegalArgumentException();
      }
   }

   S3CryptoModuleAE(S3Direct s3, EncryptionMaterialsProvider encryptionMaterialsProvider, CryptoConfiguration cryptoConfig) {
      this(null, s3, new DefaultAWSCredentialsProviderChain(), encryptionMaterialsProvider, cryptoConfig);
   }

   S3CryptoModuleAE(AWSKMS kms, S3Direct s3, EncryptionMaterialsProvider encryptionMaterialsProvider, CryptoConfiguration cryptoConfig) {
      this(kms, s3, new DefaultAWSCredentialsProviderChain(), encryptionMaterialsProvider, cryptoConfig);
   }

   protected boolean isStrict() {
      return false;
   }

   @Override
   public S3Object getObjectSecurely(GetObjectRequest req) {
      long[] desiredRange = req.getRange();
      if (!this.isStrict() || desiredRange == null && req.getPartNumber() == null) {
         long[] adjustedCryptoRange = getAdjustedCryptoRange(desiredRange);
         if (adjustedCryptoRange != null) {
            req.setRange(adjustedCryptoRange[0], adjustedCryptoRange[1]);
         }

         S3Object retrieved = this.s3.getObject(req);
         if (retrieved == null) {
            return null;
         } else {
            String suffix = null;
            if (req instanceof EncryptedGetObjectRequest) {
               EncryptedGetObjectRequest ereq = (EncryptedGetObjectRequest)req;
               suffix = ereq.getInstructionFileSuffix();
            }

            try {
               return suffix != null && !suffix.trim().isEmpty()
                  ? this.decipherWithInstFileSuffix(req, desiredRange, adjustedCryptoRange, retrieved, suffix)
                  : this.decipher(req, desiredRange, adjustedCryptoRange, retrieved);
            } catch (RuntimeException var7) {
               IOUtils.closeQuietly(retrieved, this.log);
               throw var7;
            } catch (Error var8) {
               IOUtils.closeQuietly(retrieved, this.log);
               throw var8;
            }
         }
      } else {
         throw new SecurityException("Range get and getting a part are not allowed in strict crypto mode");
      }
   }

   private S3Object decipher(GetObjectRequest req, long[] desiredRange, long[] cryptoRange, S3Object retrieved) {
      S3ObjectWrapper wrapped = new S3ObjectWrapper(retrieved, req.getS3ObjectId());
      if (wrapped.hasEncryptionInfo()) {
         return this.decipherWithMetadata(req, desiredRange, cryptoRange, wrapped);
      } else {
         S3ObjectWrapper ifile = this.fetchInstructionFile(req.getS3ObjectId(), null);
         if (ifile != null) {
            S3Object var11;
            try {
               var11 = this.decipherWithInstructionFile(req, desiredRange, cryptoRange, wrapped, ifile);
            } finally {
               IOUtils.closeQuietly(ifile, this.log);
            }

            return var11;
         } else if (!this.isStrict() && this.cryptoConfig.isIgnoreMissingInstructionFile()) {
            this.log
               .warn(
                  String.format(
                     "Unable to detect encryption information for object '%s' in bucket '%s'. Returning object without decryption.",
                     retrieved.getKey(),
                     retrieved.getBucketName()
                  )
               );
            S3ObjectWrapper adjusted = this.adjustToDesiredRange(wrapped, desiredRange, null);
            return adjusted.getS3Object();
         } else {
            IOUtils.closeQuietly(wrapped, this.log);
            throw new SecurityException(
               "Instruction file not found for S3 object with bucket name: " + retrieved.getBucketName() + ", key: " + retrieved.getKey()
            );
         }
      }
   }

   private S3Object decipherWithInstFileSuffix(GetObjectRequest req, long[] desiredRange, long[] cryptoRange, S3Object retrieved, String instFileSuffix) {
      S3ObjectId id = req.getS3ObjectId();
      S3ObjectWrapper ifile = this.fetchInstructionFile(id, instFileSuffix);
      if (ifile == null) {
         throw new SdkClientException("Instruction file with suffix " + instFileSuffix + " is not found for " + retrieved);
      } else {
         S3Object var8;
         try {
            var8 = this.decipherWithInstructionFile(req, desiredRange, cryptoRange, new S3ObjectWrapper(retrieved, id), ifile);
         } finally {
            IOUtils.closeQuietly(ifile, this.log);
         }

         return var8;
      }
   }

   private S3Object decipherWithInstructionFile(
      GetObjectRequest req, long[] desiredRange, long[] cryptoRange, S3ObjectWrapper retrieved, S3ObjectWrapper instructionFile
   ) {
      ExtraMaterialsDescription extraMatDesc = ExtraMaterialsDescription.NONE;
      boolean keyWrapExpected = this.isStrict();
      if (req instanceof EncryptedGetObjectRequest) {
         EncryptedGetObjectRequest ereq = (EncryptedGetObjectRequest)req;
         extraMatDesc = ereq.getExtraMaterialDescription();
         if (!keyWrapExpected) {
            keyWrapExpected = ereq.isKeyWrapExpected();
         }
      }

      String json = instructionFile.toJsonString();
      Map<String, String> matdesc = Collections.unmodifiableMap(Jackson.stringMapFromJsonString(json));
      ContentCryptoMaterial cekMaterial = ContentCryptoMaterial.fromInstructionFile(
         matdesc,
         this.kekMaterialsProvider,
         this.cryptoConfig.getCryptoProvider(),
         this.cryptoConfig.getAlwaysUseCryptoProvider(),
         cryptoRange,
         extraMatDesc,
         keyWrapExpected,
         this.kms
      );
      this.securityCheck(cekMaterial, retrieved);
      S3ObjectWrapper decrypted = this.decrypt(retrieved, cekMaterial, cryptoRange);
      S3ObjectWrapper adjusted = this.adjustToDesiredRange(decrypted, desiredRange, matdesc);
      return adjusted.getS3Object();
   }

   private S3Object decipherWithMetadata(GetObjectRequest req, long[] desiredRange, long[] cryptoRange, S3ObjectWrapper retrieved) {
      ExtraMaterialsDescription extraMatDesc = ExtraMaterialsDescription.NONE;
      boolean keyWrapExpected = this.isStrict();
      if (req instanceof EncryptedGetObjectRequest) {
         EncryptedGetObjectRequest ereq = (EncryptedGetObjectRequest)req;
         extraMatDesc = ereq.getExtraMaterialDescription();
         if (!keyWrapExpected) {
            keyWrapExpected = ereq.isKeyWrapExpected();
         }
      }

      ContentCryptoMaterial cekMaterial = ContentCryptoMaterial.fromObjectMetadata(
         retrieved.getObjectMetadata(),
         this.kekMaterialsProvider,
         this.cryptoConfig.getCryptoProvider(),
         this.cryptoConfig.getAlwaysUseCryptoProvider(),
         cryptoRange,
         extraMatDesc,
         keyWrapExpected,
         this.kms
      );
      this.securityCheck(cekMaterial, retrieved);
      S3ObjectWrapper decrypted = this.decrypt(retrieved, cekMaterial, cryptoRange);
      S3ObjectWrapper adjusted = this.adjustToDesiredRange(decrypted, desiredRange, null);
      return adjusted.getS3Object();
   }

   protected final S3ObjectWrapper adjustToDesiredRange(S3ObjectWrapper s3object, long[] range, Map<String, String> instruction) {
      if (range == null) {
         return s3object;
      } else {
         ContentCryptoScheme encryptionScheme = s3object.encryptionSchemeOf(instruction);
         long instanceLen = s3object.getObjectMetadata().getInstanceLength();
         long maxOffset = instanceLen - (long)(encryptionScheme.getTagLengthInBits() / 8) - 1L;
         if (range[1] > maxOffset) {
            range[1] = maxOffset;
            if (range[0] > range[1]) {
               IOUtils.closeQuietly(s3object.getObjectContent(), this.log);
               s3object.setObjectContent(new ByteArrayInputStream(new byte[0]));
               return s3object;
            }
         }

         if (range[0] > range[1]) {
            return s3object;
         } else {
            try {
               S3ObjectInputStream objectContent = s3object.getObjectContent();
               InputStream adjustedRangeContents = new AdjustedRangeInputStream(objectContent, range[0], range[1]);
               s3object.setObjectContent(new S3ObjectInputStream(adjustedRangeContents, objectContent.getHttpRequest()));
               return s3object;
            } catch (IOException var11) {
               throw new SdkClientException("Error adjusting output to desired byte range: " + var11.getMessage());
            }
         }
      }
   }

   @Override
   public ObjectMetadata getObjectSecurely(GetObjectRequest getObjectRequest, File destinationFile) {
      this.assertParameterNotNull(destinationFile, "The destination file parameter must be specified when downloading an object directly to a file");
      S3Object s3Object = this.getObjectSecurely(getObjectRequest);
      if (s3Object == null) {
         return null;
      } else {
         OutputStream outputStream = null;

         try {
            outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
            byte[] buffer = new byte[10240];

            int bytesRead;
            while((bytesRead = s3Object.getObjectContent().read(buffer)) > -1) {
               outputStream.write(buffer, 0, bytesRead);
            }
         } catch (IOException var10) {
            throw new SdkClientException("Unable to store object contents to disk: " + var10.getMessage(), var10);
         } finally {
            IOUtils.closeQuietly(outputStream, this.log);
            IOUtils.closeQuietly(s3Object.getObjectContent(), this.log);
         }

         return s3Object.getObjectMetadata();
      }
   }

   @Override
   final MultipartUploadCryptoContext newUploadContext(InitiateMultipartUploadRequest req, ContentCryptoMaterial cekMaterial) {
      return new MultipartUploadCryptoContext(req.getBucketName(), req.getKey(), cekMaterial);
   }

   @Override
   final CipherLite cipherLiteForNextPart(MultipartUploadCryptoContext uploadContext) {
      return uploadContext.getCipherLite();
   }

   @Override
   final SdkFilterInputStream wrapForMultipart(CipherLiteInputStream is, long partSize) {
      return is;
   }

   @Override
   final long computeLastPartSize(UploadPartRequest req) {
      return req.getPartSize() + (long)(this.contentCryptoScheme.getTagLengthInBits() / 8);
   }

   @Override
   final void updateUploadContext(MultipartUploadCryptoContext uploadContext, SdkFilterInputStream is) {
   }

   private S3ObjectWrapper decrypt(S3ObjectWrapper wrapper, ContentCryptoMaterial cekMaterial, long[] range) {
      S3ObjectInputStream objectContent = wrapper.getObjectContent();
      wrapper.setObjectContent(
         new S3ObjectInputStream(new CipherLiteInputStream(objectContent, cekMaterial.getCipherLite(), 2048), objectContent.getHttpRequest())
      );
      return wrapper;
   }

   private void assertParameterNotNull(Object parameterValue, String errorMessage) {
      if (parameterValue == null) {
         throw new IllegalArgumentException(errorMessage);
      }
   }

   @Override
   protected final long ciphertextLength(long originalContentLength) {
      return originalContentLength + (long)(this.contentCryptoScheme.getTagLengthInBits() / 8);
   }

   static {
      CryptoRuntime.enableBouncyCastle();
   }
}
