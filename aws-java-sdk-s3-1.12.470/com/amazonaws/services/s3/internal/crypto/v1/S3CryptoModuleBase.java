package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.internal.ReleasableInputStream;
import com.amazonaws.internal.ResettableInputStream;
import com.amazonaws.internal.SdkFilterInputStream;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import com.amazonaws.services.s3.internal.InputSubstream;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.internal.S3Direct;
import com.amazonaws.services.s3.internal.crypto.CipherLite;
import com.amazonaws.services.s3.internal.crypto.CipherLiteInputStream;
import com.amazonaws.services.s3.internal.crypto.ContentCryptoScheme;
import com.amazonaws.services.s3.internal.crypto.RenewableCipherLiteInputStream;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.AbstractPutObjectRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.CryptoStorageMode;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.EncryptionMaterialsFactory;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.InstructionFileId;
import com.amazonaws.services.s3.model.MaterialsDescriptionProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutInstructionFileRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3DataSource;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectId;
import com.amazonaws.services.s3.model.UploadObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.amazonaws.util.BinaryUtils;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.LengthCheckInputStream;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.Throwables;
import com.amazonaws.util.json.Jackson;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class S3CryptoModuleBase<T extends MultipartUploadCryptoContext> extends S3CryptoModule<T> {
   private static final boolean IS_MULTI_PART = true;
   protected static final int DEFAULT_BUFFER_SIZE = 2048;
   protected final EncryptionMaterialsProvider kekMaterialsProvider;
   protected final Log log = LogFactory.getLog(this.getClass());
   protected final S3CryptoScheme cryptoScheme;
   protected final ContentCryptoScheme contentCryptoScheme;
   protected final CryptoConfiguration cryptoConfig;
   protected final Map<String, T> multipartUploadContexts = Collections.synchronizedMap(new HashMap<>());
   protected final S3Direct s3;
   protected final AWSKMS kms;

   protected S3CryptoModuleBase(
      AWSKMS kms, S3Direct s3, AWSCredentialsProvider credentialsProvider, EncryptionMaterialsProvider kekMaterialsProvider, CryptoConfiguration cryptoConfig
   ) {
      if (!cryptoConfig.isReadOnly()) {
         throw new IllegalArgumentException("The cryto configuration parameter is required to be read-only");
      } else {
         this.kekMaterialsProvider = kekMaterialsProvider;
         this.s3 = s3;
         this.cryptoConfig = cryptoConfig;
         this.cryptoScheme = S3CryptoScheme.from(cryptoConfig.getCryptoMode());
         this.contentCryptoScheme = this.cryptoScheme.getContentCryptoScheme();
         this.kms = kms;
      }
   }

   protected S3CryptoModuleBase(
      S3Direct s3, AWSCredentialsProvider credentialsProvider, EncryptionMaterialsProvider kekMaterialsProvider, CryptoConfiguration cryptoConfig
   ) {
      this.kekMaterialsProvider = kekMaterialsProvider;
      this.s3 = s3;
      this.cryptoConfig = cryptoConfig;
      this.cryptoScheme = S3CryptoScheme.from(cryptoConfig.getCryptoMode());
      this.contentCryptoScheme = this.cryptoScheme.getContentCryptoScheme();
      this.kms = null;
   }

   protected abstract long ciphertextLength(long var1);

   @Override
   public PutObjectResult putObjectSecurely(PutObjectRequest req) {
      return this.cryptoConfig.getStorageMode() == CryptoStorageMode.InstructionFile
         ? this.putObjectUsingInstructionFile(req)
         : this.putObjectUsingMetadata(req);
   }

   private PutObjectResult putObjectUsingMetadata(PutObjectRequest req) {
      ContentCryptoMaterial cekMaterial = this.createContentCryptoMaterial(req);
      File fileOrig = req.getFile();
      InputStream isOrig = req.getInputStream();
      PutObjectRequest wrappedReq = this.wrapWithCipher(req, cekMaterial);
      req.setMetadata(this.updateMetadataWithContentCryptoMaterial(req.getMetadata(), req.getFile(), cekMaterial));

      PutObjectResult var6;
      try {
         var6 = this.s3.putObject(wrappedReq);
      } finally {
         S3DataSource.Utils.cleanupDataSource(req, fileOrig, isOrig, wrappedReq.getInputStream(), this.log);
      }

      return var6;
   }

   private PutObjectResult putObjectUsingInstructionFile(PutObjectRequest putObjectRequest) {
      File fileOrig = putObjectRequest.getFile();
      InputStream isOrig = putObjectRequest.getInputStream();
      PutObjectRequest putInstFileRequest = putObjectRequest.clone().withFile(null).withInputStream(null);
      putInstFileRequest.setKey(putInstFileRequest.getKey() + "." + "instruction");
      ContentCryptoMaterial cekMaterial = this.createContentCryptoMaterial(putObjectRequest);
      PutObjectRequest req = this.wrapWithCipher(putObjectRequest, cekMaterial);

      PutObjectResult result;
      try {
         result = this.s3.putObject(req);
      } finally {
         S3DataSource.Utils.cleanupDataSource(putObjectRequest, fileOrig, isOrig, req.getInputStream(), this.log);
      }

      this.s3.putObject(this.updateInstructionPutRequest(putInstFileRequest, cekMaterial));
      return result;
   }

   @Override
   public final void abortMultipartUploadSecurely(AbortMultipartUploadRequest req) {
      this.s3.abortMultipartUpload(req);
      this.multipartUploadContexts.remove(req.getUploadId());
   }

   @Override
   public final CopyPartResult copyPartSecurely(CopyPartRequest copyPartRequest) {
      String uploadId = copyPartRequest.getUploadId();
      T uploadContext = this.multipartUploadContexts.get(uploadId);
      CopyPartResult result = this.s3.copyPart(copyPartRequest);
      if (uploadContext != null && !uploadContext.hasFinalPartBeenSeen()) {
         uploadContext.setHasFinalPartBeenSeen(true);
      }

      return result;
   }

   abstract T newUploadContext(InitiateMultipartUploadRequest var1, ContentCryptoMaterial var2);

   @Override
   public InitiateMultipartUploadResult initiateMultipartUploadSecurely(InitiateMultipartUploadRequest req) {
      ContentCryptoMaterial cekMaterial = this.createContentCryptoMaterial(req);
      if (this.cryptoConfig.getStorageMode() == CryptoStorageMode.ObjectMetadata) {
         ObjectMetadata metadata = req.getObjectMetadata();
         if (metadata == null) {
            metadata = new ObjectMetadata();
         }

         req.setObjectMetadata(this.updateMetadataWithContentCryptoMaterial(metadata, null, cekMaterial));
      }

      InitiateMultipartUploadResult result = this.s3.initiateMultipartUpload(req);
      T uploadContext = this.newUploadContext(req, cekMaterial);
      if (req instanceof MaterialsDescriptionProvider) {
         MaterialsDescriptionProvider p = (MaterialsDescriptionProvider)req;
         uploadContext.setMaterialsDescription(p.getMaterialsDescription());
      }

      this.multipartUploadContexts.put(result.getUploadId(), uploadContext);
      return result;
   }

   abstract CipherLite cipherLiteForNextPart(T var1);

   abstract long computeLastPartSize(UploadPartRequest var1);

   abstract <I extends CipherLiteInputStream> SdkFilterInputStream wrapForMultipart(I var1, long var2);

   abstract void updateUploadContext(T var1, SdkFilterInputStream var2);

   @Override
   public UploadPartResult uploadPartSecurely(UploadPartRequest req) {
      int blockSize = this.contentCryptoScheme.getBlockSizeInBytes();
      boolean isLastPart = req.isLastPart();
      String uploadId = req.getUploadId();
      long partSize = req.getPartSize();
      boolean partSizeMultipleOfCipherBlockSize = 0L == partSize % (long)blockSize;
      if (!isLastPart && !partSizeMultipleOfCipherBlockSize) {
         throw new SdkClientException(
            "Invalid part size: part sizes for encrypted multipart uploads must be multiples of the cipher block size ("
               + blockSize
               + ") with the exception of the last part."
         );
      } else {
         T uploadContext = this.multipartUploadContexts.get(uploadId);
         if (uploadContext == null) {
            throw new SdkClientException("No client-side information available on upload ID " + uploadId);
         } else {
            uploadContext.beginPartUpload(req.getPartNumber());
            CipherLite cipherLite = this.cipherLiteForNextPart(uploadContext);
            File fileOrig = req.getFile();
            InputStream isOrig = req.getInputStream();
            SdkFilterInputStream isCurr = null;

            UploadPartResult result;
            try {
               CipherLiteInputStream clis = this.newMultipartS3CipherInputStream(req, cipherLite);
               isCurr = this.wrapForMultipart(clis, partSize);
               req.setInputStream(isCurr);
               req.setFile(null);
               req.setFileOffset(0L);
               if (isLastPart) {
                  long lastPartSize = this.computeLastPartSize(req);
                  if (lastPartSize > -1L) {
                     req.setPartSize(lastPartSize);
                  }

                  if (uploadContext.hasFinalPartBeenSeen()) {
                     throw new SdkClientException(
                        "This part was specified as the last part in a multipart upload, but a previous part was already marked as the last part.  Only the last part of the upload should be marked as the last part."
                     );
                  }
               }

               result = this.s3.uploadPart(req);
            } finally {
               S3DataSource.Utils.cleanupDataSource(req, fileOrig, isOrig, isCurr, this.log);
               uploadContext.endPartUpload();
            }

            if (isLastPart) {
               uploadContext.setHasFinalPartBeenSeen(true);
            }

            this.updateUploadContext(uploadContext, isCurr);
            return result;
         }
      }
   }

   protected final CipherLiteInputStream newMultipartS3CipherInputStream(UploadPartRequest req, CipherLite cipherLite) {
      File fileOrig = req.getFile();
      InputStream isOrig = req.getInputStream();
      InputStream isCurr = null;

      try {
         if (fileOrig == null) {
            if (isOrig == null) {
               throw new IllegalArgumentException("A File or InputStream must be specified when uploading part");
            }

            isCurr = isOrig;
         } else {
            isCurr = new ResettableInputStream(fileOrig);
         }

         isCurr = new InputSubstream(isCurr, req.getFileOffset(), req.getPartSize(), req.isLastPart());
         return (CipherLiteInputStream)(cipherLite.markSupported()
            ? new CipherLiteInputStream(isCurr, cipherLite, 2048, true, req.isLastPart())
            : new RenewableCipherLiteInputStream(isCurr, cipherLite, 2048, true, req.isLastPart()));
      } catch (Exception var7) {
         S3DataSource.Utils.cleanupDataSource(req, fileOrig, isOrig, isCurr, this.log);
         throw Throwables.failure(var7, "Unable to create cipher input stream");
      }
   }

   @Override
   public CompleteMultipartUploadResult completeMultipartUploadSecurely(CompleteMultipartUploadRequest req) {
      String uploadId = req.getUploadId();
      T uploadContext = this.multipartUploadContexts.get(uploadId);
      if (uploadContext != null && !uploadContext.hasFinalPartBeenSeen()) {
         throw new SdkClientException(
            "Unable to complete an encrypted multipart upload without being told which part was the last.  Without knowing which part was the last, the encrypted data in Amazon S3 is incomplete and corrupt."
         );
      } else {
         CompleteMultipartUploadResult result = this.s3.completeMultipartUpload(req);
         if (uploadContext != null && this.cryptoConfig.getStorageMode() == CryptoStorageMode.InstructionFile) {
            this.s3
               .putObject(this.createInstructionPutRequest(uploadContext.getBucketName(), uploadContext.getKey(), uploadContext.getContentCryptoMaterial()));
         }

         this.multipartUploadContexts.remove(uploadId);
         return result;
      }
   }

   protected final ObjectMetadata updateMetadataWithContentCryptoMaterial(ObjectMetadata metadata, File file, ContentCryptoMaterial instruction) {
      if (metadata == null) {
         metadata = new ObjectMetadata();
      }

      if (file != null) {
         Mimetypes mimetypes = Mimetypes.getInstance();
         metadata.setContentType(mimetypes.getMimetype(file));
      }

      return instruction.toObjectMetadata(metadata, this.cryptoConfig.getCryptoMode());
   }

   protected final ContentCryptoMaterial createContentCryptoMaterial(AmazonWebServiceRequest req) {
      if (req instanceof EncryptionMaterialsFactory) {
         EncryptionMaterialsFactory f = (EncryptionMaterialsFactory)req;
         EncryptionMaterials materials = f.getEncryptionMaterials();
         if (materials != null) {
            return this.buildContentCryptoMaterial(materials, req);
         }
      }

      if (req instanceof MaterialsDescriptionProvider) {
         MaterialsDescriptionProvider mdp = (MaterialsDescriptionProvider)req;
         Map<String, String> matdesc_req = mdp.getMaterialsDescription();
         ContentCryptoMaterial ccm = this.newContentCryptoMaterial(this.kekMaterialsProvider, matdesc_req, this.cryptoConfig.getCryptoProvider(), req);
         if (ccm != null) {
            return ccm;
         }

         if (matdesc_req != null) {
            EncryptionMaterials material = this.kekMaterialsProvider.getEncryptionMaterials();
            if (material == null || !material.isKMSEnabled()) {
               throw new SdkClientException("No material available from the encryption material provider for description " + matdesc_req);
            }
         }
      }

      return this.newContentCryptoMaterial(this.kekMaterialsProvider, this.cryptoConfig.getCryptoProvider(), req);
   }

   private ContentCryptoMaterial newContentCryptoMaterial(
      EncryptionMaterialsProvider kekMaterialProvider, Map<String, String> materialsDescription, Provider provider, AmazonWebServiceRequest req
   ) {
      EncryptionMaterials kekMaterials = kekMaterialProvider.getEncryptionMaterials(materialsDescription);
      return kekMaterials == null ? null : this.buildContentCryptoMaterial(kekMaterials, req);
   }

   private ContentCryptoMaterial newContentCryptoMaterial(EncryptionMaterialsProvider kekMaterialProvider, Provider provider, AmazonWebServiceRequest req) {
      EncryptionMaterials kekMaterials = kekMaterialProvider.getEncryptionMaterials();
      if (kekMaterials == null) {
         throw new SdkClientException("No material available from the encryption material provider");
      } else {
         return this.buildContentCryptoMaterial(kekMaterials, req);
      }
   }

   @Override
   public final void putLocalObjectSecurely(UploadObjectRequest reqIn, String uploadId, OutputStream os) throws IOException {
      UploadObjectRequest req = reqIn.clone();
      File fileOrig = req.getFile();
      InputStream isOrig = req.getInputStream();
      T uploadContext = this.multipartUploadContexts.get(uploadId);
      ContentCryptoMaterial cekMaterial = uploadContext.getContentCryptoMaterial();
      req = this.wrapWithCipher(req, cekMaterial);

      try {
         IOUtils.copy(req.getInputStream(), os);
         uploadContext.setHasFinalPartBeenSeen(true);
      } finally {
         S3DataSource.Utils.cleanupDataSource(req, fileOrig, isOrig, req.getInputStream(), this.log);
         IOUtils.closeQuietly(os, this.log);
      }
   }

   private ContentCryptoMaterial buildContentCryptoMaterial(EncryptionMaterials materials, AmazonWebServiceRequest req) {
      byte[] iv = new byte[this.contentCryptoScheme.getIVLengthInBytes()];
      this.cryptoConfig.getSecureRandom().nextBytes(iv);
      if (materials.isKMSEnabled()) {
         Map<String, String> encryptionContext = ContentCryptoMaterial.mergeMaterialDescriptions(materials, req);
         GenerateDataKeyRequest keyGenReq = new GenerateDataKeyRequest()
            .withEncryptionContext(encryptionContext)
            .withKeyId(materials.getCustomerMasterKeyId())
            .withKeySpec(this.contentCryptoScheme.getKeySpec());
         keyGenReq.withGeneralProgressListener(req.getGeneralProgressListener()).withRequestMetricCollector(req.getRequestMetricCollector());
         GenerateDataKeyResult keyGenRes = this.kms.generateDataKey(keyGenReq);
         SecretKey cek = new SecretKeySpec(BinaryUtils.copyAllBytesFrom(keyGenRes.getPlaintext()), this.contentCryptoScheme.getKeyGeneratorAlgorithm());
         byte[] keyBlob = BinaryUtils.copyAllBytesFrom(keyGenRes.getCiphertextBlob());
         return ContentCryptoMaterial.wrap(
            cek,
            iv,
            this.contentCryptoScheme,
            this.cryptoConfig.getCryptoProvider(),
            this.cryptoConfig.getAlwaysUseCryptoProvider(),
            new KMSSecuredCEK(keyBlob, encryptionContext)
         );
      } else {
         return ContentCryptoMaterial.create(this.generateCEK(materials), iv, materials, this.cryptoScheme, this.cryptoConfig, this.kms, req);
      }
   }

   protected final SecretKey generateCEK(EncryptionMaterials kekMaterials) {
      String keygenAlgo = this.contentCryptoScheme.getKeyGeneratorAlgorithm();

      try {
         KeyGenerator generator = this.cryptoConfig.getCryptoProvider() == null
            ? KeyGenerator.getInstance(keygenAlgo)
            : KeyGenerator.getInstance(keygenAlgo, this.cryptoConfig.getCryptoProvider());
         generator.init(this.contentCryptoScheme.getKeyLengthInBits(), this.cryptoConfig.getSecureRandom());
         boolean involvesBCPublicKey = false;
         KeyPair keypair = kekMaterials.getKeyPair();
         if (keypair != null) {
            String keyWrapAlgo = this.cryptoScheme.getKeyWrapScheme().getKeyWrapAlgorithm(keypair.getPublic());
            if (keyWrapAlgo == null) {
               Provider provider = generator.getProvider();
               String providerName = provider == null ? null : provider.getName();
               involvesBCPublicKey = "BC".equals(providerName);
            }
         }

         SecretKey secretKey = generator.generateKey();
         if (involvesBCPublicKey && secretKey.getEncoded()[0] == 0) {
            for(int retry = 0; retry < 9; ++retry) {
               secretKey = generator.generateKey();
               if (secretKey.getEncoded()[0] != 0) {
                  return secretKey;
               }
            }

            throw new SdkClientException("Failed to generate secret key");
         } else {
            return secretKey;
         }
      } catch (NoSuchAlgorithmException var9) {
         throw new SdkClientException("Unable to generate envelope symmetric key:" + var9.getMessage(), var9);
      }
   }

   protected final <R extends AbstractPutObjectRequest> R wrapWithCipher(R request, ContentCryptoMaterial cekMaterial) {
      ObjectMetadata metadata = request.getMetadata();
      if (metadata == null) {
         metadata = new ObjectMetadata();
      }

      metadata.setContentMD5(null);
      long plaintextLength = this.plaintextLength(request, metadata);
      if (plaintextLength >= 0L) {
         metadata.addUserMetadata("x-amz-unencrypted-content-length", Long.toString(plaintextLength));
         metadata.setContentLength(this.ciphertextLength(plaintextLength));
      }

      request.setMetadata(metadata);
      request.setInputStream(this.newS3CipherLiteInputStream(request, cekMaterial, plaintextLength));
      request.setFile(null);
      return request;
   }

   private CipherLiteInputStream newS3CipherLiteInputStream(AbstractPutObjectRequest req, ContentCryptoMaterial cekMaterial, long plaintextLength) {
      File fileOrig = req.getFile();
      InputStream isOrig = req.getInputStream();
      InputStream isCurr = null;

      try {
         if (fileOrig == null) {
            isCurr = isOrig == null ? null : ReleasableInputStream.wrap(isOrig);
         } else {
            isCurr = new ResettableInputStream(fileOrig);
         }

         if (plaintextLength > -1L) {
            isCurr = new LengthCheckInputStream(isCurr, plaintextLength, false);
         }

         CipherLite cipherLite = cekMaterial.getCipherLite();
         return (CipherLiteInputStream)(cipherLite.markSupported()
            ? new CipherLiteInputStream(isCurr, cipherLite, 2048)
            : new RenewableCipherLiteInputStream(isCurr, cipherLite, 2048));
      } catch (Exception var9) {
         S3DataSource.Utils.cleanupDataSource(req, fileOrig, isOrig, isCurr, this.log);
         throw Throwables.failure(var9, "Unable to create cipher input stream");
      }
   }

   protected final long plaintextLength(AbstractPutObjectRequest request, ObjectMetadata metadata) {
      if (request.getFile() != null) {
         return request.getFile().length();
      } else {
         return request.getInputStream() != null && metadata.getRawMetadataValue("Content-Length") != null ? metadata.getContentLength() : -1L;
      }
   }

   public final S3CryptoScheme getS3CryptoScheme() {
      return this.cryptoScheme;
   }

   protected final PutObjectRequest updateInstructionPutRequest(PutObjectRequest req, ContentCryptoMaterial cekMaterial) {
      byte[] bytes = cekMaterial.toJsonString(this.cryptoConfig.getCryptoMode()).getBytes(StringUtils.UTF8);
      ObjectMetadata metadata = req.getMetadata();
      if (metadata == null) {
         metadata = new ObjectMetadata();
         req.setMetadata(metadata);
      }

      metadata.setContentMD5(null);
      metadata.setContentLength((long)bytes.length);
      metadata.addUserMetadata("x-amz-crypto-instr-file", "");
      req.setMetadata(metadata);
      req.setInputStream(new ByteArrayInputStream(bytes));
      return req;
   }

   protected final PutObjectRequest createInstructionPutRequest(String bucketName, String key, ContentCryptoMaterial cekMaterial) {
      byte[] bytes = cekMaterial.toJsonString(this.cryptoConfig.getCryptoMode()).getBytes(StringUtils.UTF8);
      InputStream is = new ByteArrayInputStream(bytes);
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength((long)bytes.length);
      metadata.addUserMetadata("x-amz-crypto-instr-file", "");
      InstructionFileId ifileId = new S3ObjectId(bucketName, key).instructionFileId();
      return new PutObjectRequest(ifileId.getBucket(), ifileId.getKey(), is, metadata);
   }

   protected void securityCheck(ContentCryptoMaterial cekMaterial, S3ObjectWrapper retrieved) {
   }

   final S3ObjectWrapper fetchInstructionFile(S3ObjectId s3ObjectId, String instFileSuffix) {
      try {
         S3Object o = this.s3.getObject(this.createInstructionGetRequest(s3ObjectId, instFileSuffix));
         return o == null ? null : new S3ObjectWrapper(o, s3ObjectId);
      } catch (AmazonServiceException var4) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Unable to retrieve instruction file : " + var4.getMessage());
         }

         return null;
      }
   }

   @Override
   public final PutObjectResult putInstructionFileSecurely(PutInstructionFileRequest req) {
      S3ObjectId id = req.getS3ObjectId();
      GetObjectRequest getreq = new GetObjectRequest(id);
      S3Object retrieved = this.s3.getObject(getreq);
      IOUtils.closeQuietly(retrieved, this.log);
      if (retrieved == null) {
         throw new IllegalArgumentException("The specified S3 object (" + id + ") doesn't exist.");
      } else {
         S3ObjectWrapper wrapped = new S3ObjectWrapper(retrieved, id);

         try {
            ContentCryptoMaterial origCCM = this.contentCryptoMaterialOf(wrapped);
            if (ContentCryptoScheme.AES_GCM.equals(origCCM.getContentCryptoScheme()) && this.cryptoConfig.getCryptoMode() == CryptoMode.EncryptionOnly) {
               throw new SecurityException("Lowering the protection of encryption material is not allowed");
            } else {
               this.securityCheck(origCCM, wrapped);
               EncryptionMaterials newKEK = req.getEncryptionMaterials();
               ContentCryptoMaterial newCCM;
               if (newKEK == null) {
                  newCCM = origCCM.recreate(req.getMaterialsDescription(), this.kekMaterialsProvider, this.cryptoScheme, this.cryptoConfig, this.kms, req);
               } else {
                  newCCM = origCCM.recreate(newKEK, this.kekMaterialsProvider, this.cryptoScheme, this.cryptoConfig, this.kms, req);
               }

               PutObjectRequest putInstFileRequest = req.createPutObjectRequest(retrieved);
               return this.s3.putObject(this.updateInstructionPutRequest(putInstFileRequest, newCCM));
            }
         } catch (RuntimeException var10) {
            IOUtils.closeQuietly(retrieved, this.log);
            throw var10;
         } catch (Error var11) {
            IOUtils.closeQuietly(retrieved, this.log);
            throw var11;
         }
      }
   }

   private ContentCryptoMaterial contentCryptoMaterialOf(S3ObjectWrapper s3w) {
      if (s3w.hasEncryptionInfo()) {
         return ContentCryptoMaterial.fromObjectMetadata(
            s3w.getObjectMetadata(),
            this.kekMaterialsProvider,
            this.cryptoConfig.getCryptoProvider(),
            this.cryptoConfig.getAlwaysUseCryptoProvider(),
            false,
            this.kms
         );
      } else {
         S3ObjectWrapper orig_ifile = this.fetchInstructionFile(s3w.getS3ObjectId(), null);
         if (orig_ifile == null) {
            throw new IllegalArgumentException("S3 object is not encrypted: " + s3w);
         } else {
            String json = orig_ifile.toJsonString();
            return this.ccmFromJson(json);
         }
      }
   }

   private ContentCryptoMaterial ccmFromJson(String json) {
      Map<String, String> instruction = Collections.unmodifiableMap(Jackson.stringMapFromJsonString(json));
      return ContentCryptoMaterial.fromInstructionFile(
         instruction, this.kekMaterialsProvider, this.cryptoConfig.getCryptoProvider(), this.cryptoConfig.getAlwaysUseCryptoProvider(), false, this.kms
      );
   }

   final GetObjectRequest createInstructionGetRequest(S3ObjectId id) {
      return this.createInstructionGetRequest(id, null);
   }

   final GetObjectRequest createInstructionGetRequest(S3ObjectId s3objectId, String instFileSuffix) {
      return new GetObjectRequest(s3objectId.instructionFileId(instFileSuffix));
   }

   static long[] getAdjustedCryptoRange(long[] range) {
      return range != null && range[0] <= range[1] ? new long[]{getCipherBlockLowerBound(range[0]), getCipherBlockUpperBound(range[1])} : null;
   }

   private static long getCipherBlockLowerBound(long leftmostBytePosition) {
      long cipherBlockSize = 16L;
      long offset = leftmostBytePosition % cipherBlockSize;
      long lowerBound = leftmostBytePosition - offset - cipherBlockSize;
      return lowerBound < 0L ? 0L : lowerBound;
   }

   private static long getCipherBlockUpperBound(long rightmostBytePosition) {
      long cipherBlockSize = 16L;
      long offset = cipherBlockSize - rightmostBytePosition % cipherBlockSize;
      long upperBound = rightmostBytePosition + offset + cipherBlockSize;
      return upperBound < 0L ? Long.MAX_VALUE : upperBound;
   }
}
