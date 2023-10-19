package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.event.SDKProgressPublisher;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.model.CopyResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class CopyMonitor implements Callable<CopyResult>, TransferMonitor {
   private final AmazonS3 s3;
   private final ExecutorService threadPool;
   private final CopyObjectRequest origReq;
   private final CopyCallable multipartCopyCallable;
   private final CopyImpl transfer;
   private final ProgressListenerChain listener;
   private final List<Future<PartETag>> futures = new ArrayList<>();
   private boolean isCopyDone = false;
   private AtomicReference<Future<CopyResult>> futureReference = new AtomicReference<>(null);

   @Override
   public Future<CopyResult> getFuture() {
      return this.futureReference.get();
   }

   @Override
   public synchronized boolean isDone() {
      return this.isCopyDone;
   }

   private synchronized void markAllDone() {
      this.isCopyDone = true;
   }

   public static CopyMonitor create(
      TransferManager manager,
      CopyImpl transfer,
      ExecutorService threadPool,
      CopyCallable multipartCopyCallable,
      CopyObjectRequest copyObjectRequest,
      ProgressListenerChain progressListenerChain
   ) {
      CopyMonitor copyMonitor = new CopyMonitor(manager, transfer, threadPool, multipartCopyCallable, copyObjectRequest, progressListenerChain);
      Future<CopyResult> thisFuture = threadPool.submit(copyMonitor);
      copyMonitor.futureReference.compareAndSet(null, thisFuture);
      return copyMonitor;
   }

   private CopyMonitor(
      TransferManager manager,
      CopyImpl transfer,
      ExecutorService threadPool,
      CopyCallable multipartCopyCallable,
      CopyObjectRequest copyObjectRequest,
      ProgressListenerChain progressListenerChain
   ) {
      this.s3 = manager.getAmazonS3Client();
      this.multipartCopyCallable = multipartCopyCallable;
      this.origReq = copyObjectRequest;
      this.listener = progressListenerChain;
      this.transfer = transfer;
      this.threadPool = threadPool;
   }

   public CopyResult call() throws Exception {
      try {
         CopyResult result = this.multipartCopyCallable.call();
         if (result == null) {
            this.futures.addAll(this.multipartCopyCallable.getFutures());
            this.futureReference
               .set(
                  this.threadPool
                     .submit(
                        new CompleteMultipartCopy(this.multipartCopyCallable.getMultipartUploadId(), this.s3, this.origReq, this.futures, this.listener, this)
                     )
               );
         } else {
            this.copyComplete();
         }

         return result;
      } catch (CancellationException var2) {
         this.transfer.setState(Transfer.TransferState.Canceled);
         SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_CANCELED_EVENT);
         throw new SdkClientException("Upload canceled");
      } catch (Exception var3) {
         this.transfer.setState(Transfer.TransferState.Failed);
         SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_FAILED_EVENT);
         throw var3;
      }
   }

   void copyComplete() {
      this.markAllDone();
      this.transfer.setState(Transfer.TransferState.Completed);
      this.transfer.getProgress().updateProgress(this.transfer.getProgress().getTotalBytesToTransfer());
      if (this.multipartCopyCallable.isMultipartCopy()) {
         SDKProgressPublisher.publishProgress(this.listener, ProgressEventType.TRANSFER_COMPLETED_EVENT);
      }
   }

   void reportFailure() {
      this.transfer.setState(Transfer.TransferState.Failed);
   }
}
