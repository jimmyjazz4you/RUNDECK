package com.amazonaws.services.s3.internal;

import com.amazonaws.internal.SdkPredicate;
import com.amazonaws.services.s3.model.AmazonS3Exception;

public class CompleteMultipartUploadRetryablePredicate extends SdkPredicate<AmazonS3Exception> {
   private static final String ERROR_CODE = "InternalError";
   private static final String RETYABLE_ERROR_MESSAGE = "Please try again.";

   public boolean test(AmazonS3Exception exception) {
      if (exception != null && exception.getErrorCode() != null && exception.getErrorMessage() != null) {
         return exception.getErrorCode().contains("InternalError") && exception.getErrorMessage().contains("Please try again.");
      } else {
         return false;
      }
   }
}
