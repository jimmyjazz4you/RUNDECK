package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.SyncProgressListener;
import com.amazonaws.services.s3.transfer.TransferProgress;

public class TransferProgressUpdatingListener extends SyncProgressListener {
   private final TransferProgress transferProgress;

   public TransferProgressUpdatingListener(TransferProgress transferProgress) {
      this.transferProgress = transferProgress;
   }

   public void progressChanged(ProgressEvent progressEvent) {
      long bytes = progressEvent.getBytesTransferred();
      if (bytes != 0L) {
         this.transferProgress.updateProgress(bytes);
      }
   }
}
