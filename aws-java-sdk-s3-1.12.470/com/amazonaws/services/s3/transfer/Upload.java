package com.amazonaws.services.s3.transfer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.exception.PauseException;
import com.amazonaws.services.s3.transfer.model.UploadResult;

public interface Upload extends Transfer {
   UploadResult waitForUploadResult() throws AmazonClientException, AmazonServiceException, InterruptedException;

   PersistableUpload pause() throws PauseException;

   PauseResult<PersistableUpload> tryPause(boolean var1);

   void abort();
}
