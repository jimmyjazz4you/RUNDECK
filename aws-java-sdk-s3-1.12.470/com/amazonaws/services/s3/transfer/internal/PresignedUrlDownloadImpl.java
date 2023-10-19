package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.PresignedUrlDownload;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferProgress;
import java.io.IOException;
import java.net.URL;

@SdkInternalApi
public class PresignedUrlDownloadImpl extends AbstractTransfer implements PresignedUrlDownload {
   private final PresignedUrlDownloadRequest presignedUrlDownloadRequest;
   private S3Object s3Object;

   public PresignedUrlDownloadImpl(
      String description,
      TransferProgress transferProgress,
      ProgressListenerChain progressListenerChain,
      PresignedUrlDownloadRequest presignedUrlDownloadRequest
   ) {
      super(description, transferProgress, progressListenerChain);
      this.presignedUrlDownloadRequest = presignedUrlDownloadRequest;
   }

   public synchronized void setS3Object(S3Object s3Object) {
      this.s3Object = s3Object;
   }

   @Override
   public URL getPresignedUrl() {
      return this.presignedUrlDownloadRequest.getPresignedUrl();
   }

   @Override
   public synchronized void abort() throws IOException {
      this.monitor.getFuture().cancel(true);
      if (this.s3Object != null) {
         this.s3Object.getObjectContent().abort();
      }

      this.setState(Transfer.TransferState.Canceled);
   }

   @Override
   public void setState(Transfer.TransferState state) {
      super.setState(state);
      switch(state) {
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
