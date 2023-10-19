package com.amazonaws.services.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.regions.Region;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.s3.internal.MultiFileOutputStream;
import com.amazonaws.services.s3.internal.S3Direct;
import com.amazonaws.services.s3.internal.crypto.v1.CryptoModuleDispatcher;
import com.amazonaws.services.s3.internal.crypto.v1.S3CryptoModule;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.EncryptedInitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.EncryptionMaterials;
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
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;
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

@Deprecated
public class AmazonS3EncryptionClient extends AmazonS3Client implements AmazonS3Encryption {
   private static final String USER_AGENT_V1 = "S3CryptoV1n/" + VersionInfoUtils.getVersion();
   private final S3CryptoModule<?> crypto;
   private final AWSKMS kms;
   private final boolean isKMSClientInternal;

   @Deprecated
   public AmazonS3EncryptionClient(EncryptionMaterials encryptionMaterials) {
      this(new StaticEncryptionMaterialsProvider(encryptionMaterials));
   }

   @Deprecated
   public AmazonS3EncryptionClient(EncryptionMaterialsProvider encryptionMaterialsProvider) {
      this(new StaticCredentialsProvider(new AnonymousAWSCredentials()), encryptionMaterialsProvider, configFactory.getConfig(), new CryptoConfiguration());
   }

   @Deprecated
   public AmazonS3EncryptionClient(EncryptionMaterials encryptionMaterials, CryptoConfiguration cryptoConfig) {
      this(new StaticEncryptionMaterialsProvider(encryptionMaterials), cryptoConfig);
   }

   @Deprecated
   public AmazonS3EncryptionClient(EncryptionMaterialsProvider encryptionMaterialsProvider, CryptoConfiguration cryptoConfig) {
      this(new StaticCredentialsProvider(new AnonymousAWSCredentials()), encryptionMaterialsProvider, configFactory.getConfig(), cryptoConfig);
   }

   @Deprecated
   public AmazonS3EncryptionClient(AWSCredentials credentials, EncryptionMaterials encryptionMaterials) {
      this(credentials, new StaticEncryptionMaterialsProvider(encryptionMaterials));
   }

   @Deprecated
   public AmazonS3EncryptionClient(AWSCredentials credentials, EncryptionMaterialsProvider encryptionMaterialsProvider) {
      this(credentials, encryptionMaterialsProvider, configFactory.getConfig(), new CryptoConfiguration());
   }

   @Deprecated
   public AmazonS3EncryptionClient(AWSCredentialsProvider credentialsProvider, EncryptionMaterialsProvider encryptionMaterialsProvider) {
      this(credentialsProvider, encryptionMaterialsProvider, configFactory.getConfig(), new CryptoConfiguration());
   }

   @Deprecated
   public AmazonS3EncryptionClient(AWSCredentials credentials, EncryptionMaterials encryptionMaterials, CryptoConfiguration cryptoConfig) {
      this(credentials, new StaticEncryptionMaterialsProvider(encryptionMaterials), cryptoConfig);
   }

   @Deprecated
   public AmazonS3EncryptionClient(AWSCredentials credentials, EncryptionMaterialsProvider encryptionMaterialsProvider, CryptoConfiguration cryptoConfig) {
      this(credentials, encryptionMaterialsProvider, configFactory.getConfig(), cryptoConfig);
   }

   @Deprecated
   public AmazonS3EncryptionClient(
      AWSCredentialsProvider credentialsProvider, EncryptionMaterialsProvider encryptionMaterialsProvider, CryptoConfiguration cryptoConfig
   ) {
      this(credentialsProvider, encryptionMaterialsProvider, configFactory.getConfig(), cryptoConfig);
   }

   @Deprecated
   public AmazonS3EncryptionClient(
      AWSCredentials credentials, EncryptionMaterials encryptionMaterials, ClientConfiguration clientConfig, CryptoConfiguration cryptoConfig
   ) {
      this(credentials, new StaticEncryptionMaterialsProvider(encryptionMaterials), clientConfig, cryptoConfig);
   }

   @Deprecated
   public AmazonS3EncryptionClient(
      AWSCredentials credentials, EncryptionMaterialsProvider encryptionMaterialsProvider, ClientConfiguration clientConfig, CryptoConfiguration cryptoConfig
   ) {
      this(new StaticCredentialsProvider(credentials), encryptionMaterialsProvider, clientConfig, cryptoConfig);
   }

   @Deprecated
   public AmazonS3EncryptionClient(
      AWSCredentialsProvider credentialsProvider,
      EncryptionMaterialsProvider kekMaterialsProvider,
      ClientConfiguration clientConfig,
      CryptoConfiguration cryptoConfig
   ) {
      this(credentialsProvider, kekMaterialsProvider, clientConfig, cryptoConfig, null);
   }

   @Deprecated
   public AmazonS3EncryptionClient(
      AWSCredentialsProvider credentialsProvider,
      EncryptionMaterialsProvider kekMaterialsProvider,
      ClientConfiguration clientConfig,
      CryptoConfiguration cryptoConfig,
      RequestMetricCollector requestMetricCollector
   ) {
      this(null, credentialsProvider, kekMaterialsProvider, clientConfig, cryptoConfig, requestMetricCollector);
   }

   @Deprecated
   public AmazonS3EncryptionClient(
      AWSKMSClient kms,
      AWSCredentialsProvider credentialsProvider,
      EncryptionMaterialsProvider kekMaterialsProvider,
      ClientConfiguration clientConfig,
      CryptoConfiguration cryptoConfig,
      RequestMetricCollector requestMetricCollector
   ) {
      super(credentialsProvider, clientConfig, requestMetricCollector);
      this.assertParameterNotNull(kekMaterialsProvider, "EncryptionMaterialsProvider parameter must not be null.");
      this.assertParameterNotNull(cryptoConfig, "CryptoConfiguration parameter must not be null.");
      this.isKMSClientInternal = kms == null;
      this.kms = this.isKMSClientInternal ? this.newAWSKMSClient(credentialsProvider, clientConfig, cryptoConfig, requestMetricCollector) : kms;
      this.crypto = new CryptoModuleDispatcher(this.kms, new AmazonS3EncryptionClient.S3DirectImpl(), credentialsProvider, kekMaterialsProvider, cryptoConfig);
   }

   @SdkInternalApi
   AmazonS3EncryptionClient(AmazonS3EncryptionClientParams params) {
      super(params);
      this.assertParameterNotNull(params.getEncryptionMaterials(), "EncryptionMaterialsProvider parameter must not be null.");
      this.assertParameterNotNull(params.getCryptoConfiguration(), "CryptoConfiguration parameter must not be null.");
      this.isKMSClientInternal = params.getKmsClient() == null;
      this.kms = (AWSKMS)(this.isKMSClientInternal
         ? this.newAWSKMSClient(
            params.getClientParams().getCredentialsProvider(),
            params.getClientParams().getClientConfiguration(),
            params.getCryptoConfiguration(),
            params.getClientParams().getRequestMetricCollector()
         )
         : params.getKmsClient());
      this.crypto = new CryptoModuleDispatcher(
         this.kms,
         new AmazonS3EncryptionClient.S3DirectImpl(),
         params.getClientParams().getCredentialsProvider(),
         params.getEncryptionMaterials(),
         params.getCryptoConfiguration()
      );
   }

   public static AmazonS3EncryptionClientBuilder encryptionBuilder() {
      return AmazonS3EncryptionClientBuilder.standard();
   }

   private AWSKMSClient newAWSKMSClient(
      AWSCredentialsProvider credentialsProvider,
      ClientConfiguration clientConfig,
      CryptoConfiguration cryptoConfig,
      RequestMetricCollector requestMetricCollector
   ) {
      AWSKMSClient kmsClient = new AWSKMSClient(credentialsProvider, clientConfig, requestMetricCollector);
      Region kmsRegion = cryptoConfig.getAwsKmsRegion();
      if (kmsRegion != null) {
         kmsClient.setRegion(kmsRegion);
      }

      return kmsClient;
   }

   private void assertParameterNotNull(Object parameterValue, String errorMessage) {
      if (parameterValue == null) {
         throw new IllegalArgumentException(errorMessage);
      }
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
      req.getRequestClientOptions().appendUserAgent(USER_AGENT_V1);
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

   public PutObjectResult putInstructionFile(PutInstructionFileRequest req) {
      return this.crypto.putInstructionFileSecurely(req);
   }

   @Override
   public void shutdown() {
      super.shutdown();
      if (this.isKMSClientInternal) {
         this.kms.shutdown();
      }
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

      observer.init(req, new AmazonS3EncryptionClient.S3DirectImpl(), this, es);
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

   private <T extends Throwable> T onAbort(UploadObjectObserver observer, T t) {
      observer.onAbort();
      return t;
   }

   private final class S3DirectImpl extends S3Direct {
      private S3DirectImpl() {
      }

      @Override
      public PutObjectResult putObject(PutObjectRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.putObject(req);
      }

      @Override
      public S3Object getObject(GetObjectRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.getObject(req);
      }

      @Override
      public ObjectMetadata getObject(GetObjectRequest req, File dest) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.getObject(req, dest);
      }

      @Override
      public ObjectMetadata getObjectMetadata(GetObjectMetadataRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.getObjectMetadata(req);
      }

      @Override
      public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.completeMultipartUpload(req);
      }

      @Override
      public InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.initiateMultipartUpload(req);
      }

      @Override
      public UploadPartResult uploadPart(UploadPartRequest req) throws SdkClientException, AmazonServiceException {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.uploadPart(req);
      }

      @Override
      public CopyPartResult copyPart(CopyPartRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         return AmazonS3EncryptionClient.super.copyPart(req);
      }

      @Override
      public void abortMultipartUpload(AbortMultipartUploadRequest req) {
         this.appendUserAgent(req, AmazonS3EncryptionClient.USER_AGENT_V1);
         AmazonS3EncryptionClient.super.abortMultipartUpload(req);
      }

      final <X extends AmazonWebServiceRequest> X appendUserAgent(X request, String userAgent) {
         request.getRequestClientOptions().appendUserAgent(userAgent);
         return request;
      }
   }
}
