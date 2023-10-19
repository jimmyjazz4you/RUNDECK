package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.event.SDKProgressPublisher;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.PauseResult;
import com.amazonaws.services.s3.transfer.PauseStatus;
import com.amazonaws.services.s3.transfer.PersistableUpload;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.internal.future.CompletedFuture;
import com.amazonaws.services.s3.transfer.internal.future.DelegatingFuture;
import com.amazonaws.services.s3.transfer.internal.future.FailedFuture;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UploadMonitor implements Callable<Void>, TransferMonitor {
   private final AmazonS3 s3;
   private final PutObjectRequest origReq;
   private final ProgressListenerChain listener;
   private final UploadCallable multipartUploadCallable;
   private final UploadImpl transfer;
   private final ExecutorService threadPool;
   private final DelegatingFuture<UploadResult> resultFuture = new DelegatingFuture<>();
   private final DelegatingFuture<Void> initiateUploadFuture = new DelegatingFuture<>();

   @Override
   public Future<UploadResult> getFuture() {
      return this.resultFuture;
   }

   @Override
   public synchronized boolean isDone() {
      return this.resultFuture.isDone();
   }

   public static UploadMonitor create(
      TransferManager manager,
      UploadImpl transfer,
      ExecutorService threadPool,
      UploadCallable multipartUploadCallable,
      PutObjectRequest putObjectRequest,
      ProgressListenerChain progressListenerChain
   ) {
      UploadMonitor uploadMonitor = new UploadMonitor(manager, transfer, threadPool, multipartUploadCallable, putObjectRequest, progressListenerChain);
      uploadMonitor.initiateUploadFuture.setDelegate(threadPool.submit(uploadMonitor));
      return uploadMonitor;
   }

   private UploadMonitor(
      TransferManager manager,
      UploadImpl transfer,
      ExecutorService threadPool,
      UploadCallable multipartUploadCallable,
      PutObjectRequest putObjectRequest,
      ProgressListenerChain progressListenerChain
   ) {
      this.s3 = manager.getAmazonS3Client();
      this.multipartUploadCallable = multipartUploadCallable;
      this.origReq = putObjectRequest;
      this.listener = progressListenerChain;
      this.transfer = transfer;
      this.threadPool = threadPool;
   }

   public Void call() {
      try {
         UploadResult result = this.multipartUploadCallable.call();
         if (result == null) {
            CompleteMultipartUpload completeTask = new CompleteMultipartUpload(
               this.multipartUploadCallable.getMultipartUploadId(),
               this.s3,
               this.origReq,
               this.multipartUploadCallable.getFutures(),
               this.multipartUploadCallable.getETags(),
               this.listener,
               this
            );
            this.resultFuture.setDelegate(this.threadPool.submit(completeTask));
         } else {
            this.setTransferStateToCompleted();
            this.resultFuture.setDelegate(new CompletedFuture<>(result));
         }
      } catch (CancellationException var3) {
         this.transfer.setState(Transfer.TransferState.Canceled);
         SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_CANCELED_EVENT);
         SdkClientException exception = new SdkClientException("Upload canceled");
         this.resultFuture.setDelegate(new FailedFuture<>(exception));
      } catch (Throwable var4) {
         this.transfer.setState(Transfer.TransferState.Failed);
         this.resultFuture.setDelegate(new FailedFuture<>(var4));
      }

      return null;
   }

   void setTransferStateToCompleted() {
      this.transfer.setState(Transfer.TransferState.Completed);
      if (this.multipartUploadCallable.isMultipartUpload()) {
         SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_COMPLETED_EVENT);
      }
   }

   void setTransferStateToFailed() {
      this.transfer.setState(Transfer.TransferState.Failed);
   }

   PauseResult<PersistableUpload> pause(boolean forceCancel) {
      PersistableUpload persistableUpload = this.multipartUploadCallable.getPersistableUpload();
      if (persistableUpload == null) {
         PauseStatus pauseStatus = TransferManagerUtils.determinePauseStatus(this.transfer.getState(), forceCancel);
         if (forceCancel) {
            this.cancelTransferFutures();
            this.multipartUploadCallable.safelyAbortMultipartUpload(this.initiateUploadFuture);
         }

         return new PauseResult<>(pauseStatus);
      } else {
         this.initiateUploadFuture.cancel(true);
         this.cancelTransferFutures();
         return new PauseResult<>(PauseStatus.SUCCESS, persistableUpload);
      }
   }

   private void cancelTransferFutures() {
      this.resultFuture.cancel(true);
      this.multipartUploadCallable.getFutures().cancel(true);
   }

   void performAbort() {
      this.cancelTransferFutures();
      this.multipartUploadCallable.safelyAbortMultipartUpload(this.initiateUploadFuture);
      SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_CANCELED_EVENT);
   }
}
