package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferProgress;
import java.io.IOException;
import java.util.Collection;

public class MultipleFileDownloadImpl extends MultipleFileTransfer<Download> implements MultipleFileDownload {
   private final String keyPrefix;
   private final String bucketName;

   public MultipleFileDownloadImpl(
      String description,
      TransferProgress transferProgress,
      ProgressListenerChain progressListenerChain,
      String keyPrefix,
      String bucketName,
      Collection<? extends Download> downloads
   ) {
      super(description, transferProgress, progressListenerChain, downloads);
      this.keyPrefix = keyPrefix;
      this.bucketName = bucketName;
   }

   @Override
   public String getKeyPrefix() {
      return this.keyPrefix;
   }

   @Override
   public String getBucketName() {
      return this.bucketName;
   }

   @Override
   public void waitForCompletion() throws AmazonClientException, AmazonServiceException, InterruptedException {
      if (!this.subTransfers.isEmpty()) {
         super.waitForCompletion();
      }
   }

   @Override
   public void abort() throws IOException {
      for(Transfer fileDownload : this.subTransfers) {
         ((DownloadImpl)fileDownload).abortWithoutNotifyingStateChangeListener();
      }

      for(Transfer fileDownload : this.subTransfers) {
         ((DownloadImpl)fileDownload).notifyStateChangeListeners(Transfer.TransferState.Canceled);
      }
   }
}
