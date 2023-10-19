package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.Upload;
import java.util.Collection;
import java.util.Collections;

public class MultipleFileUploadImpl extends MultipleFileTransfer<Upload> implements MultipleFileUpload {
   private final String keyPrefix;
   private final String bucketName;

   public MultipleFileUploadImpl(
      String description,
      TransferProgress transferProgress,
      ProgressListenerChain progressListenerChain,
      String keyPrefix,
      String bucketName,
      Collection<? extends Upload> subTransfers
   ) {
      super(description, transferProgress, progressListenerChain, subTransfers);
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
   public Collection<? extends Upload> getSubTransfers() {
      return Collections.unmodifiableCollection(this.subTransfers);
   }
}
