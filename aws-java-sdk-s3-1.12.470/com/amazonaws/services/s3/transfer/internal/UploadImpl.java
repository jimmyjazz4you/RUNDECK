package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.transfer.PauseResult;
import com.amazonaws.services.s3.transfer.PauseStatus;
import com.amazonaws.services.s3.transfer.PersistableUpload;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.exception.PauseException;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UploadImpl extends AbstractTransfer implements Upload {
   public UploadImpl(
      String description, TransferProgress transferProgressInternalState, ProgressListenerChain progressListenerChain, TransferStateChangeListener listener
   ) {
      super(description, transferProgressInternalState, progressListenerChain, listener);
   }

   @Override
   public UploadResult waitForUploadResult() throws AmazonClientException, AmazonServiceException, InterruptedException {
      try {
         UploadResult result;
         Future<?> f;
         for(result = null; !this.monitor.isDone() || result == null; result = (UploadResult)f.get()) {
            f = this.monitor.getFuture();
         }

         return result;
      } catch (ExecutionException var3) {
         this.rethrowExecutionException(var3);
         return null;
      }
   }

   @Override
   public PersistableUpload pause() throws PauseException {
      PauseResult<PersistableUpload> pauseResult = this.pause(true);
      if (pauseResult.getPauseStatus() != PauseStatus.SUCCESS) {
         throw new PauseException(pauseResult.getPauseStatus());
      } else {
         return pauseResult.getInfoToResume();
      }
   }

   private PauseResult<PersistableUpload> pause(boolean forceCancelTransfers) throws AmazonClientException {
      UploadMonitor uploadMonitor = (UploadMonitor)this.monitor;
      return uploadMonitor.pause(forceCancelTransfers);
   }

   @Override
   public PauseResult<PersistableUpload> tryPause(boolean forceCancelTransfers) {
      return this.pause(forceCancelTransfers);
   }

   @Override
   public void abort() {
      UploadMonitor uploadMonitor = (UploadMonitor)this.monitor;
      uploadMonitor.performAbort();
   }
}
