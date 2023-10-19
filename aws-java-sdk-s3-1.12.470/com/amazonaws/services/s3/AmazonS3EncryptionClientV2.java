package com.amazonaws.services.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.regions.Region;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.s3.internal.MultiFileOutputStream;
import com.amazonaws.services.s3.internal.S3Direct;
import com.amazonaws.services.s3.internal.crypto.v2.S3CryptoModule;
import com.amazonaws.services.s3.internal.crypto.v2.S3CryptoModuleAE;
import com.amazonaws.services.s3.internal.crypto.v2.S3CryptoModuleAEStrict;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.CryptoRangeGetMode;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.EncryptedInitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.InstructionFileId;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutInstructionFileRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectId;
import com.amazonaws.services.s3.model.UploadObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.amazonaws.util.VersionInfoUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AmazonS3EncryptionClientV2 extends AmazonS3Client implements AmazonS3EncryptionV2 {
   private static final String USER_AGENT_V2 = "S3CryptoV2/" + VersionInfoUtils.getVersion();
   private static final Log log = LogFactory.getLog(AmazonS3EncryptionClientV2.class);
   private final S3CryptoModule<?> crypto;
   private final AWSKMS kmsClient;
   private final boolean isKMSClientInternal;

   public static AmazonS3EncryptionClientV2Builder encryptionBuilder() {
      return AmazonS3EncryptionClientV2Builder.standard();
   }

   @SdkInternalApi
   AmazonS3EncryptionClientV2(AmazonS3EncryptionClientV2Params params) {
      super(params);
      this.validateParameters(params);
      CryptoConfigurationV2 readOnlyCryptoConfig = this.validateConfigAndCreateReadOnlyCopy(params.getCryptoConfiguration());
      this.isKMSClientInternal = params.getKmsClient() == null;
      this.kmsClient = this.isKMSClientInternal
         ? this.newAWSKMSClient(
            params.getClientParams().getCredentialsProvider(),
            params.getClientParams().getClientConfiguration(),
            readOnlyCryptoConfig,
            params.getClientParams().getRequestMetricCollector()
         )
         : params.getKmsClient();
      this.crypto = this.createCryptoModule(
         readOnlyCryptoConfig, this.kmsClient, params.getEncryptionMaterialsProvider(), params.getClientParams().getCredentialsProvider()
      );
      warnOnLegacyCryptoMode(params.getCryptoConfiguration().getCryptoMode());
      warnOnRangeGetsEnabled(params);
   }

   private void validateParameters(AmazonS3EncryptionClientV2Params params) {
      this.assertParameterNotNull(params.getEncryptionMaterialsProvider(), "EncryptionMaterialsProvider parameter must not be null.");
      this.assertParameterNotNull(params.getCryptoConfiguration(), "CryptoConfiguration parameter must not be null.");
   }

   private S3CryptoModule<?> createCryptoModule(
      CryptoConfigurationV2 cryptoConfig, AWSKMS kmsClient, EncryptionMaterialsProvider encryptionMaterialsProvider, AWSCredentialsProvider credentialsProvider
   ) {
      if (cryptoConfig.getCryptoMode() == CryptoMode.AuthenticatedEncryption) {
         return new S3CryptoModuleAE(kmsClient, new AmazonS3EncryptionClientV2.S3DirectImpl(), credentialsProvider, encryptionMaterialsProvider, cryptoConfig);
      } else if (cryptoConfig.getCryptoMode() == CryptoMode.StrictAuthenticatedEncryption) {
         return new S3CryptoModuleAEStrict(
            kmsClient, new AmazonS3EncryptionClientV2.S3DirectImpl(), credentialsProvider, encryptionMaterialsProvider, cryptoConfig
         );
      } else {
         throw new UnsupportedOperationException("Cannot encrypt using mode " + cryptoConfig.getCryptoMode());
      }
   }

   private CryptoConfigurationV2 validateConfigAndCreateReadOnlyCopy(CryptoConfigurationV2 cryptoConfig) {
      CryptoConfigurationV2 clonedCryptoConfig = cryptoConfig.clone();
      if (clonedCryptoConfig.getCryptoMode() == null) {
         clonedCryptoConfig.setCryptoMode(CryptoMode.StrictAuthenticatedEncryption);
      }

      if (CryptoMode.AuthenticatedEncryption != clonedCryptoConfig.getCryptoMode()
         && CryptoMode.StrictAuthenticatedEncryption != clonedCryptoConfig.getCryptoMode()) {
         throw new IllegalArgumentException("Invalid value for CryptoMode : " + clonedCryptoConfig.getCryptoMode());
      } else if (cryptoConfig.isUnsafeUndecryptableObjectPassthrough() && CryptoMode.StrictAuthenticatedEncryption == cryptoConfig.getCryptoMode()) {
         throw new IllegalArgumentException(
            String.format("unsafeUndecryptableObjectPassthrough must not be enabled in %s mode", CryptoMode.StrictAuthenticatedEncryption)
         );
      } else {
         return clonedCryptoConfig.readOnly();
      }
   }

   private AWSKMS newAWSKMSClient(
      AWSCredentialsProvider credentialsProvider,
      ClientConfiguration clientConfig,
      CryptoConfigurationV2 cryptoConfig,
      RequestMetricCollector requestMetricCollector
   ) {
      AWSKMSClientBuilder kmsClientBuilder = (AWSKMSClientBuilder)((AWSKMSClientBuilder)((AWSKMSClientBuilder)AWSKMSClientBuilder.standard()
               .withCredentials(credentialsProvider))
            .withClientConfiguration(clientConfig))
         .withMetricsCollector(requestMetricCollector);
      Region kmsRegion = cryptoConfig.getAwsKmsRegion();
      if (kmsRegion != null) {
         kmsClientBuilder.withRegion(kmsRegion.getName());
      }

      return (AWSKMS)kmsClientBuilder.build();
   }

   private void assertParameterNotNull(Object parameterValue, String errorMessage) {
      if (parameterValue == null) {
         throw new IllegalArgumentException(errorMessage);
      }
   }

   public AWSKMS getKmsClient() {
      return this.isKMSClientInternal ? null : this.kmsClient;
   }

   public EncryptionMaterialsProvider getEncryptionMaterialsProvider() {
      return this.crypto.getEncryptionMaterialsProvider();
   }

   public CryptoConfigurationV2 getCryptoConfiguration() {
      return this.crypto.getCryptoConfiguration();
   }

   @Override
   public PutObjectResult putObject(PutObjectRequest req) {
      return this.crypto.putObjectSecurely(req.clone());
   }

   @Override
   public S3Object getObject(GetObjectRequest req) {
      return this.crypto.getObjectSecurely(req);
   }

   @Override
   public ObjectMetadata getObject(GetObjectRequest req, File dest) {
      return this.crypto.getObjectSecurely(req, dest);
   }

   @Override
   public void deleteObject(DeleteObjectRequest req) {
      req.getRequestClientOptions().appendUserAgent(USER_AGENT_V2);
      super.deleteObject(req);
      InstructionFileId ifid = new S3ObjectId(req.getBucketName(), req.getKey()).instructionFileId();
      DeleteObjectRequest instructionDeleteRequest = (DeleteObjectRequest)req.clone();
      instructionDeleteRequest.withBucketName(ifid.getBucket()).withKey(ifid.getKey());
      super.deleteObject(instructionDeleteRequest);
   }

   @Override
   public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest req) {
      return this.crypto.completeMultipartUploadSecurely(req);
   }

   @Override
   public InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest req) {
      boolean isCreateEncryptionMaterial = true;
      if (req instanceof EncryptedInitiateMultipartUploadRequest) {
         EncryptedInitiateMultipartUploadRequest cryptoReq = (EncryptedInitiateMultipartUploadRequest)req;
         isCreateEncryptionMaterial = cryptoReq.isCreateEncryptionMaterial();
      }

      return isCreateEncryptionMaterial ? this.crypto.initiateMultipartUploadSecurely(req) : super.initiateMultipartUpload(req);
   }

   @Override
   public UploadPartResult uploadPart(UploadPartRequest uploadPartRequest) throws SdkClientException, AmazonServiceException {
      return this.crypto.uploadPartSecurely(uploadPartRequest);
   }

   @Override
   public CopyPartResult copyPart(CopyPartRequest copyPartRequest) {
      return this.crypto.copyPartSecurely(copyPartRequest);
   }

   @Override
   public void abortMultipartUpload(AbortMultipartUploadRequest req) {
      this.crypto.abortMultipartUploadSecurely(req);
   }

   @Override
   public PutObjectResult putInstructionFile(PutInstructionFileRequest req) {
      return this.crypto.putInstructionFileSecurely(req);
   }

   @Override
   public CompleteMultipartUploadResult uploadObject(UploadObjectRequest req) throws IOException, InterruptedException, ExecutionException {
      ExecutorService es = req.getExecutorService();
      boolean defaultExecutorService = es == null;
      if (es == null) {
         es = Executors.newFixedThreadPool(this.clientConfiguration.getMaxConnections());
      }

      UploadObjectObserver observer = req.getUploadObjectObserver();
      if (observer == null) {
         observer = new UploadObjectObserver();
      }

      observer.init(req, new AmazonS3EncryptionClientV2.S3DirectImpl(), this, es);
      String uploadId = observer.onUploadInitiation(req);
      List<PartETag> partETags = new ArrayList<>();
      MultiFileOutputStream mfos = req.getMultiFileOutputStream();
      if (mfos == null) {
         mfos = new MultiFileOutputStream();
      }

      try {
         mfos.init(observer, req.getPartSize(), req.getDiskLimit());
         this.crypto.putLocalObjectSecurely(req, uploadId, mfos);

         for(Future<UploadPartResult> future : observer.getFutures()) {
            UploadPartResult partResult = future.get();
            partETags.add(new PartETag(partResult.getPartNumber(), partResult.getETag()));
         }
      } catch (IOException var18) {
         throw (IOException)this.onAbort(observer, var18);
      } catch (InterruptedException var19) {
         throw (InterruptedException)this.onAbort(observer, var19);
      } catch (ExecutionException var20) {
         throw (ExecutionException)this.onAbort(observer, var20);
      } catch (RuntimeException var21) {
         throw (RuntimeException)this.onAbort(observer, var21);
      } catch (Error var22) {
         throw (Error)this.onAbort(observer, var22);
      } finally {
         if (defaultExecutorService) {
            es.shutdownNow();
         }

         mfos.cleanup();
      }

      return observer.onCompletion(partETags);
   }

   @Override
   public void shutdown() {
      super.shutdown();
      if (this.isKMSClientInternal) {
         this.kmsClient.shutdown();
      }
   }

   private <T extends Throwable> T onAbort(UploadObjectObserver observer, T t) {
      observer.onAbort();
      return t;
   }

   private static void warnOnRangeGetsEnabled(AmazonS3EncryptionClientV2Params params) {
      CryptoConfigurationV2 cryptoConfig = params.getCryptoConfiguration();
      CryptoRangeGetMode rangeGetMode = cryptoConfig.getRangeGetMode();
      if (rangeGetMode != CryptoRangeGetMode.DISABLED) {
         log.warn(
            "The S3 Encryption Client is configured to support range get requests. Range gets do not provide authenticated encryption properties even when used with an authenticated mode (AES-GCM). See https://docs.aws.amazon.com/general/latest/gr/aws_sdk_cryptography.html"
         );
      }
   }

   private static void warnOnLegacyCryptoMode(CryptoMode cryptoMode) {
      if (cryptoMode == CryptoMode.AuthenticatedEncryption) {
         log.warn(
            "The S3 Encryption Client is configured to read encrypted data with legacy encryption modes through the CryptoMode setting. If you don't have objects encrypted with these legacy modes, you should disable support for them to enhance security. See https://docs.aws.amazon.com/general/latest/gr/aws_sdk_cryptography.html"
         );
      }
   }

   private final class S3DirectImpl extends S3Direct {
      private S3DirectImpl() {
      }

      @Override
      public PutObjectResult putObject(PutObjectRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.putObject(req);
      }

      @Override
      public S3Object getObject(GetObjectRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.getObject(req);
      }

      @Override
      public ObjectMetadata getObject(GetObjectRequest req, File dest) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.getObject(req, dest);
      }

      @Override
      public ObjectMetadata getObjectMetadata(GetObjectMetadataRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.getObjectMetadata(req);
      }

      @Override
      public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.completeMultipartUpload(req);
      }

      @Override
      public InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.initiateMultipartUpload(req);
      }

      @Override
      public UploadPartResult uploadPart(UploadPartRequest req) throws SdkClientException, AmazonServiceException {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.uploadPart(req);
      }

      @Override
      public CopyPartResult copyPart(CopyPartRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         return AmazonS3EncryptionClientV2.super.copyPart(req);
      }

      @Override
      public void abortMultipartUpload(AbortMultipartUploadRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClientV2.USER_AGENT_V2);
         AmazonS3EncryptionClientV2.super.abortMultipartUpload(req);
      }

      final <X extends AmazonWebServiceRequest> X appendUserAgent(X request, String userAgent) {
         request.getRequestClientOptions().appendUserAgent(userAgent);
         return request;
      }
   }
}
