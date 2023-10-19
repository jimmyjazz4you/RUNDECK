package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferProgress;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MultipleFileTransfer<T extends Transfer> extends AbstractTransfer {
   protected final Collection<? extends T> subTransfers;
   private AtomicBoolean subTransferStarted = new AtomicBoolean(false);

   MultipleFileTransfer(
      String description, TransferProgress transferProgress, ProgressListenerChain progressListenerChain, Collection<? extends T> subTransfers
   ) {
      super(description, transferProgress, progressListenerChain);
      this.subTransfers = subTransfers;
   }

   public void collateFinalState() {
      boolean seenCanceled = false;

      for(T download : this.subTransfers) {
         if (download.getState() == Transfer.TransferState.Failed) {
            this.setState(Transfer.TransferState.Failed);
            return;
         }

         if (download.getState() == Transfer.TransferState.Canceled) {
            seenCanceled = true;
         }
      }

      if (seenCanceled) {
         this.setState(Transfer.TransferState.Canceled);
      } else {
         this.setState(Transfer.TransferState.Completed);
      }
   }

   @Override
   public void setState(Transfer.TransferState state) {
      super.setState(state);
      switch(state) {
         case Waiting:
            this.fireProgressEvent(ProgressEventType.TRANSFER_PREPARING_EVENT);
            break;
         case InProgress:
            if (this.subTransferStarted.compareAndSet(false, true)) {
               this.fireProgressEvent(ProgressEventType.TRANSFER_STARTED_EVENT);
            }
            break;
         case Completed:
            this.fireProgressEvent(ProgressEventType.TRANSFER_COMPLETED_EVENT);
            break;
         case Canceled:
            this.fireProgressEvent(ProgressEventType.TRANSFER_CANCELED_EVENT);
            break;
         case Failed:
            this.fireProgressEvent(ProgressEventType.TRANSFER_FAILED_EVENT);
      }
   }
}
