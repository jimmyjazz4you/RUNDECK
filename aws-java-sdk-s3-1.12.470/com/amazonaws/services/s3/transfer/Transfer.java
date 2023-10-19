package com.amazonaws.services.s3.transfer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListener;

public interface Transfer {
   boolean isDone();

   void waitForCompletion() throws AmazonClientException, AmazonServiceException, InterruptedException;

   AmazonClientException waitForException() throws InterruptedException;

   String getDescription();

   Transfer.TransferState getState();

   void addProgressListener(ProgressListener var1);

   void removeProgressListener(ProgressListener var1);

   TransferProgress getProgress();

   @Deprecated
   void addProgressListener(com.amazonaws.services.s3.model.ProgressListener var1);

   @Deprecated
   void removeProgressListener(com.amazonaws.services.s3.model.ProgressListener var1);

   public static enum TransferState {
      Waiting,
      InProgress,
      Completed,
      Canceled,
      Failed;
   }
}
