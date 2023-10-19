package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.event.SDKProgressPublisher;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.EncryptedInitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.model.CopyResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CopyCallable implements Callable<CopyResult> {
   private final AmazonS3 s3;
   private final ExecutorService threadPool;
   private final CopyObjectRequest copyObjectRequest;
   private String multipartUploadId;
   private final ObjectMetadata metadata;
   private final CopyImpl copy;
   private static final Log log = LogFactory.getLog(CopyCallable.class);
   private final TransferManagerConfiguration configuration;
   private final List<Future<PartETag>> futures = new ArrayList<>();
   private final ProgressListenerChain listenerChain;

   public CopyCallable(
      TransferManager transferManager,
      ExecutorService threadPool,
      CopyImpl copy,
      CopyObjectRequest copyObjectRequest,
      ObjectMetadata metadata,
      ProgressListenerChain progressListenerChain
   ) {
      this.s3 = transferManager.getAmazonS3Client();
      this.configuration = transferManager.getConfiguration();
      this.threadPool = threadPool;
      this.copyObjectRequest = copyObjectRequest;
      this.metadata = metadata;
      this.listenerChain = progressListenerChain;
      this.copy = copy;
   }

   List<Future<PartETag>> getFutures() {
      return this.futures;
   }

   String getMultipartUploadId() {
      return this.multipartUploadId;
   }

   public boolean isMultipartCopy() {
      return this.metadata.getContentLength() > this.configuration.getMultipartCopyThreshold();
   }

   public CopyResult call() throws Exception {
      this.copy.setState(Transfer.TransferState.InProgress);
      if (this.isMultipartCopy()) {
         SDKProgressPublisher.publishProgress(this.listenerChain, ProgressEventType.TRANSFER_STARTED_EVENT);
         this.copyInParts();
         return null;
      } else {
         return this.copyInOneChunk();
      }
   }

   private CopyResult copyInOneChunk() {
      CopyObjectResult copyObjectResult = this.s3.copyObject(this.copyObjectRequest);
      CopyResult copyResult = new CopyResult();
      copyResult.setSourceBucketName(this.copyObjectRequest.getSourceBucketName());
      copyResult.setSourceKey(this.copyObjectRequest.getSourceKey());
      copyResult.setDestinationBucketName(this.copyObjectRequest.getDestinationBucketName());
      copyResult.setDestinationKey(this.copyObjectRequest.getDestinationKey());
      copyResult.setETag(copyObjectResult.getETag());
      copyResult.setVersionId(copyObjectResult.getVersionId());
      return copyResult;
   }

   private void copyInParts() throws Exception {
      this.multipartUploadId = this.initiateMultipartUpload(this.copyObjectRequest);
      long optimalPartSize = this.getOptimalPartSize(this.metadata.getContentLength());

      try {
         CopyPartRequestFactory requestFactory = new CopyPartRequestFactory(
            this.copyObjectRequest, this.multipartUploadId, optimalPartSize, this.metadata.getContentLength()
         );
         this.copyPartsInParallel(requestFactory);
      } catch (Exception var4) {
         SDKProgressPublisher.publishProgress(this.listenerChain, ProgressEventType.TRANSFER_FAILED_EVENT);
         this.abortMultipartCopy();
         throw new RuntimeException("Unable to perform multipart copy", var4);
      }
   }

   private long getOptimalPartSize(long contentLengthOfSource) {
      long optimalPartSize = TransferManagerUtils.calculateOptimalPartSizeForCopy(this.copyObjectRequest, this.configuration, contentLengthOfSource);
      log.debug("Calculated optimal part size: " + optimalPartSize);
      return optimalPartSize;
   }

   private void copyPartsInParallel(CopyPartRequestFactory requestFactory) {
      while(requestFactory.hasMoreRequests()) {
         if (this.threadPool.isShutdown()) {
            throw new CancellationException("TransferManager has been shutdown");
         }

         CopyPartRequest request = requestFactory.getNextCopyPartRequest();
         this.futures.add(this.threadPool.submit(new CopyPartCallable(this.s3, request)));
      }
   }

   private String initiateMultipartUpload(CopyObjectRequest origReq) {
      EncryptedInitiateMultipartUploadRequest req = (EncryptedInitiateMultipartUploadRequest)new EncryptedInitiateMultipartUploadRequest(
            origReq.getDestinationBucketName(), origReq.getDestinationKey()
         )
         .withCannedACL(origReq.getCannedAccessControlList())
         .withRequesterPays(origReq.isRequesterPays())
         .withAccessControlList(origReq.getAccessControlList())
         .withStorageClass(origReq.getStorageClass())
         .withSSECustomerKey(origReq.getDestinationSSECustomerKey())
         .withSSEAwsKeyManagementParams(origReq.getSSEAwsKeyManagementParams())
         .withGeneralProgressListener(origReq.getGeneralProgressListener())
         .withRequestMetricCollector(origReq.getRequestMetricCollector());
      req.setCreateEncryptionMaterial(false);
      ObjectMetadata newObjectMetadata = origReq.getNewObjectMetadata();
      if (newObjectMetadata == null) {
         newObjectMetadata = new ObjectMetadata();
      }

      if (newObjectMetadata.getContentType() == null) {
         newObjectMetadata.setContentType(this.metadata.getContentType());
      }

      req.setObjectMetadata(newObjectMetadata);
      this.populateMetadataWithEncryptionParams(this.metadata, newObjectMetadata);
      req.setTagging(origReq.getNewObjectTagging());
      req.withObjectLockMode(origReq.getObjectLockMode())
         .withObjectLockLegalHoldStatus(origReq.getObjectLockLegalHoldStatus())
         .withObjectLockRetainUntilDate(origReq.getObjectLockRetainUntilDate());
      req.withRequestCredentialsProvider(origReq.getRequestCredentialsProvider());
      String uploadId = this.s3.initiateMultipartUpload(req).getUploadId();
      log.debug("Initiated new multipart upload: " + uploadId);
      return uploadId;
   }

   private void populateMetadataWithEncryptionParams(ObjectMetadata source, ObjectMetadata destination) {
      Map<String, String> userMetadataSource = source.getUserMetadata();
      Map<String, String> userMetadataDestination = destination.getUserMetadata();
      String[] headersToCopy = new String[]{
         "x-amz-cek-alg",
         "x-amz-iv",
         "x-amz-key",
         "x-amz-key-v2",
         "x-amz-wrap-alg",
         "x-amz-tag-len",
         "x-amz-matdesc",
         "x-amz-unencrypted-content-length",
         "x-amz-unencrypted-content-md5"
      };
      if (userMetadataSource != null) {
         if (userMetadataDestination == null) {
            userMetadataDestination = new HashMap<>();
            destination.setUserMetadata(userMetadataDestination);
         }

         for(String header : headersToCopy) {
            String headerValue = userMetadataSource.get(header);
            if (headerValue != null) {
               userMetadataDestination.put(header, headerValue);
            }
         }
      }
   }

   private void abortMultipartCopy() {
      try {
         AbortMultipartUploadRequest abortRequest = (AbortMultipartUploadRequest)new AbortMultipartUploadRequest(
               this.copyObjectRequest.getDestinationBucketName(), this.copyObjectRequest.getDestinationKey(), this.multipartUploadId
            )
            .withRequesterPays(this.copyObjectRequest.isRequesterPays())
            .withRequestCredentialsProvider(this.copyObjectRequest.getRequestCredentialsProvider());
         this.s3.abortMultipartUpload(abortRequest);
      } catch (Exception var2) {
         log.info("Unable to abort multipart upload, you may need to manually remove uploaded parts: " + var2.getMessage(), var2);
      }
   }
}
