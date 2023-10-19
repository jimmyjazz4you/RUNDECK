package com.amazonaws.services.s3.transfer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.model.CopyResult;

public interface Copy extends Transfer {
   CopyResult waitForCopyResult() throws AmazonClientException, AmazonServiceException, InterruptedException;
}
