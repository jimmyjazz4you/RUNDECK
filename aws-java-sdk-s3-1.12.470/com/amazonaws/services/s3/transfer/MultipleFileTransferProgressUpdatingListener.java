package com.amazonaws.services.s3.transfer;

import com.amazonaws.event.DeliveryMode;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.transfer.internal.TransferProgressUpdatingListener;

final class MultipleFileTransferProgressUpdatingListener extends TransferProgressUpdatingListener implements DeliveryMode {
   private final ProgressListenerChain progressListenerChain;

   public MultipleFileTransferProgressUpdatingListener(TransferProgress transferProgress, ProgressListenerChain progressListenerChain) {
      super(transferProgress);
      this.progressListenerChain = progressListenerChain;
   }

   @Override
   public void progressChanged(ProgressEvent progressEvent) {
      super.progressChanged(progressEvent);
      this.progressListenerChain.progressChanged(progressEvent);
   }

   public boolean isSyncCallSafe() {
      return this.progressListenerChain == null || this.progressListenerChain.isSyncCallSafe();
   }
}
