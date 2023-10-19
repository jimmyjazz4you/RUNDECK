package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.transfer.Copy;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.model.CopyResult;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CopyImpl extends AbstractTransfer implements Copy {
   public CopyImpl(
      String description, TransferProgress transferProgress, ProgressListenerChain progressListenerChain, TransferStateChangeListener stateChangeListener
   ) {
      super(description, transferProgress, progressListenerChain, stateChangeListener);
   }

   @Override
   public CopyResult waitForCopyResult() throws AmazonClientException, AmazonServiceException, InterruptedException {
      try {
         CopyResult result;
         Future<?> f;
         for(result = null; !this.monitor.isDone() || result == null; result = (CopyResult)f.get()) {
            f = this.monitor.getFuture();
         }

         return result;
      } catch (ExecutionException var3) {
         this.rethrowExecutionException(var3);
         return null;
      }
   }
}
