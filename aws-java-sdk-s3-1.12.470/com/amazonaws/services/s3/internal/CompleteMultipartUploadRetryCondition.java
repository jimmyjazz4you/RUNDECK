package com.amazonaws.services.s3.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.internal.SdkPredicate;
import com.amazonaws.retry.RetryPolicy.RetryCondition;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.util.ValidationUtils;

public class CompleteMultipartUploadRetryCondition implements RetryCondition {
   private static final int MAX_RETRY_ATTEMPTS = 3;
   private final SdkPredicate<AmazonS3Exception> completeMultipartRetryablePredicate;
   private final int maxCompleteMultipartUploadRetries;

   public CompleteMultipartUploadRetryCondition() {
      this(new CompleteMultipartUploadRetryablePredicate(), 3);
   }

   @SdkTestInternalApi
   CompleteMultipartUploadRetryCondition(SdkPredicate<AmazonS3Exception> predicate, int maxRetryAttempts) {
      ValidationUtils.assertNotNull(predicate, "sdk predicate");
      this.completeMultipartRetryablePredicate = predicate;
      this.maxCompleteMultipartUploadRetries = maxRetryAttempts;
   }

   public boolean shouldRetry(AmazonWebServiceRequest originalRequest, AmazonClientException exception, int retriesAttempted) {
      if (!(exception instanceof AmazonS3Exception)) {
         return false;
      } else {
         return this.completeMultipartRetryablePredicate.test((AmazonS3Exception)exception) && retriesAttempted < this.maxCompleteMultipartUploadRetries;
      }
   }
}
