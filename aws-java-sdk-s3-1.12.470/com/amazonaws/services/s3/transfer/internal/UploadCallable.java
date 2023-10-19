package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.event.SDKProgressPublisher;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.EncryptedInitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.EncryptedPutObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListPartsRequest;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PartListing;
import com.amazonaws.services.s3.model.PartSummary;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.transfer.PersistableUpload;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.internal.future.CompletedFuture;
import com.amazonaws.services.s3.transfer.internal.future.CompositeFuture;
import com.amazonaws.services.s3.transfer.internal.future.DelegatingFuture;
import com.amazonaws.services.s3.transfer.internal.future.FutureImpl;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UploadCallable implements Callable<UploadResult> {
   private static final Log log = LogFactory.getLog(UploadCallable.class);
   private final AmazonS3 s3;
   private final ExecutorService threadPool;
   private final PutObjectRequest origReq;
   private final FutureImpl<String> multipartUploadId = new FutureImpl<>();
   private final UploadImpl upload;
   private final TransferManagerConfiguration configuration;
   private final DelegatingFuture<List<PartETag>> partsFuture = new DelegatingFuture<>();
   private final ProgressListenerChain listener;
   private final TransferProgress transferProgress;
   private final List<PartETag> eTagsToSkip = new ArrayList<>();
   private final int MULTIPART_UPLOAD_ID_RETRIEVAL_TIMEOUT_SECONDS = 30;
   private PersistableUpload persistableUpload;
   private final AtomicReference<UploadCallable.State> state = new AtomicReference<>(UploadCallable.State.BEFORE_INITIATE);
   private final AtomicBoolean abortRequestSent = new AtomicBoolean(false);

   public UploadCallable(
      TransferManager transferManager,
      ExecutorService threadPool,
      UploadImpl upload,
      PutObjectRequest origReq,
      ProgressListenerChain progressListenerChain,
      String uploadId,
      TransferProgress transferProgress
   ) {
      this.s3 = transferManager.getAmazonS3Client();
      this.configuration = transferManager.getConfiguration();
      this.threadPool = threadPool;
      this.origReq = origReq;
      this.listener = progressListenerChain;
      this.upload = upload;
      this.transferProgress = transferProgress;
      if (uploadId != null) {
         this.multipartUploadId.complete(uploadId);
      }
   }

   Future<List<PartETag>> getFutures() {
      return this.partsFuture;
   }

   List<PartETag> getETags() {
      return this.eTagsToSkip;
   }

   String getMultipartUploadId() {
      return this.multipartUploadId.getOrThrowUnchecked("Failed to retrieve multipart upload ID.");
   }

   public boolean isMultipartUpload() {
      return TransferManagerUtils.shouldUseMultipartUpload(this.origReq, this.configuration);
   }

   public UploadResult call() throws Exception {
      UploadResult var1;
      try {
         this.upload.setState(Transfer.TransferState.InProgress);
         if (!this.isMultipartUpload()) {
            return this.uploadInOneChunk();
         }

         SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_STARTED_EVENT);
         var1 = this.uploadInParts();
      } finally {
         this.partsFuture.setDelegateIfUnset(new CompletedFuture<>(Collections.emptyList()));
         this.multipartUploadId.complete(null);
      }

      return var1;
   }

   private UploadResult uploadInOneChunk() {
      this.multipartUploadId.complete(null);
      PutObjectResult putObjectResult = this.s3.putObject(this.origReq);
      UploadResult uploadResult = new UploadResult();
      uploadResult.setBucketName(this.origReq.getBucketName());
      uploadResult.setKey(this.origReq.getKey());
      uploadResult.setETag(putObjectResult.getETag());
      uploadResult.setVersionId(putObjectResult.getVersionId());
      return uploadResult;
   }

   private void captureUploadStateIfPossible(String multipartUploadId) {
      if (this.origReq.getSSECustomerKey() == null) {
         this.persistableUpload = new PersistableUpload(
            this.origReq.getBucketName(),
            this.origReq.getKey(),
            this.origReq.getFile().getAbsolutePath(),
            multipartUploadId,
            this.configuration.getMinimumUploadPartSize(),
            this.configuration.getMultipartUploadThreshold()
         );
         this.notifyPersistableTransferAvailability();
      }
   }

   public PersistableUpload getPersistableUpload() {
      return this.persistableUpload;
   }

   private void notifyPersistableTransferAvailability() {
      S3ProgressPublisher.publishTransferPersistable(this.listener, this.persistableUpload);
   }

   private UploadResult uploadInParts() throws Exception {
      boolean isUsingEncryption = this.s3 instanceof AmazonS3Encryption || this.s3 instanceof AmazonS3EncryptionV2;
      long optimalPartSize = this.getOptimalPartSize(isUsingEncryption);

      Object var6;
      try {
         String uploadId = this.multipartUploadId.isDone() ? this.multipartUploadId.get() : this.initiateMultipartUpload(this.origReq, isUsingEncryption);
         UploadPartRequestFactory requestFactory = new UploadPartRequestFactory(this.origReq, uploadId, optimalPartSize);
         if (!TransferManagerUtils.isUploadParallelizable(this.origReq, isUsingEncryption)) {
            return this.uploadPartsInSeries(requestFactory, uploadId);
         }

         this.captureUploadStateIfPossible(uploadId);
         this.uploadPartsInParallel(requestFactory, uploadId);
         var6 = null;
      } catch (Exception var16) {
         SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_FAILED_EVENT);
         if (this.multipartUploadId.isDone()) {
            this.abortMultipartUpload(this.multipartUploadId.get());
         }

         throw var16;
      } finally {
         if (this.origReq.getInputStream() != null) {
            try {
               this.origReq.getInputStream().close();
            } catch (Exception var15) {
               log.warn("Unable to cleanly close input stream: " + var15.getMessage(), var15);
            }
         }
      }

      return (UploadResult)var6;
   }

   void safelyAbortMultipartUpload(Future<?> future) {
      if (this.multipartUploadId.isDone()) {
         this.state.set(UploadCallable.State.ABORTED);
         this.abortMultipartUpload(this.getUploadIdOrTimeout());
      } else if (!this.state.compareAndSet(UploadCallable.State.BEFORE_INITIATE, UploadCallable.State.ABORTED)
         && this.state.compareAndSet(UploadCallable.State.INITIATED, UploadCallable.State.ABORTED)) {
         this.abortMultipartUpload(this.getUploadIdOrTimeout());
      }

      future.cancel(true);
   }

   private String getUploadIdOrTimeout() {
      try {
         return this.multipartUploadId.get(30L, TimeUnit.SECONDS);
      } catch (Exception var2) {
         throw new IllegalStateException("Failed to retrieve an upload ID after 30 seconds.");
      }
   }

   private void abortMultipartUpload(String multipartUploadId) {
      if (multipartUploadId != null) {
         if (this.abortRequestSent.compareAndSet(false, true)) {
            try {
               AbortMultipartUploadRequest abortRequest = (AbortMultipartUploadRequest)new AbortMultipartUploadRequest(
                     this.origReq.getBucketName(), this.origReq.getKey(), multipartUploadId
                  )
                  .withRequesterPays(this.origReq.isRequesterPays())
                  .withRequestCredentialsProvider(this.origReq.getRequestCredentialsProvider());
               this.s3.abortMultipartUpload(abortRequest);
            } catch (Exception var3) {
               log.info("Unable to abort multipart upload, you may need to manually remove uploaded parts: " + var3.getMessage(), var3);
            }
         }
      }
   }

   private long getOptimalPartSize(boolean isUsingEncryption) {
      long optimalPartSize = TransferManagerUtils.calculateOptimalPartSize(this.origReq, this.configuration);
      if (isUsingEncryption && optimalPartSize % 32L > 0L) {
         optimalPartSize = optimalPartSize - optimalPartSize % 32L + 32L;
      }

      log.debug("Calculated optimal part size: " + optimalPartSize);
      return optimalPartSize;
   }

   private UploadResult uploadPartsInSeries(UploadPartRequestFactory requestFactory, String multipartUploadId) {
      List<PartETag> partETags;
      UploadPartRequest uploadPartRequest;
      for(partETags = new ArrayList<>(); requestFactory.hasMoreRequests(); partETags.add(this.s3.uploadPart(uploadPartRequest).getPartETag())) {
         if (this.threadPool.isShutdown()) {
            throw new CancellationException("TransferManager has been shutdown");
         }

         uploadPartRequest = requestFactory.getNextUploadPartRequest();
         InputStream inputStream = uploadPartRequest.getInputStream();
         if (inputStream != null && inputStream.markSupported()) {
            if (uploadPartRequest.getPartSize() >= 2147483647L) {
               inputStream.mark(Integer.MAX_VALUE);
            } else {
               inputStream.mark((int)uploadPartRequest.getPartSize());
            }
         }
      }

      CompleteMultipartUploadRequest req = (CompleteMultipartUploadRequest)new CompleteMultipartUploadRequest(
            this.origReq.getBucketName(), this.origReq.getKey(), multipartUploadId, partETags
         )
         .withRequesterPays(this.origReq.isRequesterPays())
         .withGeneralProgressListener(this.origReq.getGeneralProgressListener())
         .withRequestMetricCollector(this.origReq.getRequestMetricCollector())
         .withRequestCredentialsProvider(this.origReq.getRequestCredentialsProvider());
      CompleteMultipartUploadResult res = this.s3.completeMultipartUpload(req);
      UploadResult uploadResult = new UploadResult();
      uploadResult.setBucketName(res.getBucketName());
      uploadResult.setKey(res.getKey());
      uploadResult.setETag(res.getETag());
      uploadResult.setVersionId(res.getVersionId());
      return uploadResult;
   }

   private void uploadPartsInParallel(UploadPartRequestFactory requestFactory, String uploadId) {
      Map<Integer, PartSummary> partNumbers = this.identifyExistingPartsForResume(uploadId);
      List<Future<PartETag>> futures = new ArrayList<>();

      try {
         while(requestFactory.hasMoreRequests()) {
            if (this.threadPool.isShutdown()) {
               throw new CancellationException("TransferManager has been shutdown");
            }

            UploadPartRequest request = requestFactory.getNextUploadPartRequest();
            if (partNumbers.containsKey(request.getPartNumber())) {
               PartSummary summary = partNumbers.get(request.getPartNumber());
               this.eTagsToSkip.add(new PartETag(request.getPartNumber(), summary.getETag()));
               this.transferProgress.updateProgress(summary.getSize());
            } else {
               futures.add(this.threadPool.submit(new UploadPartCallable(this.s3, request, this.shouldCalculatePartMd5())));
            }
         }
      } finally {
         this.partsFuture.setDelegate(new CompositeFuture<>(futures));
      }
   }

   private Map<Integer, PartSummary> identifyExistingPartsForResume(String uploadId) {
      Map<Integer, PartSummary> partNumbers = new HashMap<>();
      if (uploadId == null) {
         return partNumbers;
      } else {
         int partNumber = 0;

         while(true) {
            ListPartsRequest listPartsRequest = (ListPartsRequest)new ListPartsRequest(this.origReq.getBucketName(), this.origReq.getKey(), uploadId)
               .withPartNumberMarker(partNumber)
               .withRequesterPays(this.origReq.isRequesterPays())
               .withRequestCredentialsProvider(this.origReq.getRequestCredentialsProvider());
            PartListing parts = this.s3.listParts(listPartsRequest);

            for(PartSummary partSummary : parts.getParts()) {
               partNumbers.put(partSummary.getPartNumber(), partSummary);
            }

            if (!parts.isTruncated()) {
               return partNumbers;
            }

            partNumber = parts.getNextPartNumberMarker();
         }
      }
   }

   private String initiateMultipartUpload(PutObjectRequest origReq, boolean isUsingEncryption) {
      InitiateMultipartUploadRequest req = null;
      if (isUsingEncryption && origReq instanceof EncryptedPutObjectRequest) {
         req = new EncryptedInitiateMultipartUploadRequest(origReq.getBucketName(), origReq.getKey())
            .withCannedACL(origReq.getCannedAcl())
            .withObjectMetadata(origReq.getMetadata());
         ((EncryptedInitiateMultipartUploadRequest)req).setMaterialsDescription(((EncryptedPutObjectRequest)origReq).getMaterialsDescription());
      } else {
         req = new InitiateMultipartUploadRequest(origReq.getBucketName(), origReq.getKey())
            .withCannedACL(origReq.getCannedAcl())
            .withObjectMetadata(origReq.getMetadata());
      }

      req.withTagging(origReq.getTagging());
      TransferManager.appendMultipartUserAgent(req);
      req.withAccessControlList(origReq.getAccessControlList())
         .withRequesterPays(origReq.isRequesterPays())
         .withStorageClass(origReq.getStorageClass())
         .withRedirectLocation(origReq.getRedirectLocation())
         .withSSECustomerKey(origReq.getSSECustomerKey())
         .withSSEAwsKeyManagementParams(origReq.getSSEAwsKeyManagementParams())
         .withGeneralProgressListener(origReq.getGeneralProgressListener())
         .withRequestMetricCollector(origReq.getRequestMetricCollector());
      req.withObjectLockMode(origReq.getObjectLockMode())
         .withObjectLockRetainUntilDate(origReq.getObjectLockRetainUntilDate())
         .withObjectLockLegalHoldStatus(origReq.getObjectLockLegalHoldStatus());
      req.withRequestCredentialsProvider(origReq.getRequestCredentialsProvider());
      if (!this.state.compareAndSet(UploadCallable.State.BEFORE_INITIATE, UploadCallable.State.INITIATED)) {
         throw new IllegalStateException("Failed to update state to " + UploadCallable.State.INITIATED + " (State: " + this.state.get() + ")");
      } else {
         String uploadId;
         try {
            uploadId = this.s3.initiateMultipartUpload(req).getUploadId();
            this.multipartUploadId.complete(uploadId);
         } catch (RuntimeException var6) {
            this.multipartUploadId.complete(null);
            throw var6;
         } catch (Error var7) {
            this.multipartUploadId.complete(null);
            throw var7;
         }

         log.debug("Initiated new multipart upload: " + uploadId);
         return uploadId;
      }
   }

   private boolean shouldCalculatePartMd5() {
      return this.origReq.getObjectLockMode() != null
         || this.origReq.getObjectLockRetainUntilDate() != null
         || this.origReq.getObjectLockLegalHoldStatus() != null
         || this.configuration.isAlwaysCalculateMultipartMd5();
   }

   private static enum State {
      BEFORE_INITIATE,
      INITIATED,
      ABORTED;
   }
}
