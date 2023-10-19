package com.amazonaws.retry;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkBaseException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class RetryUtils {
   static final Set<String> THROTTLING_ERROR_CODES = new HashSet<>(9);
   static final Set<String> CLOCK_SKEW_ERROR_CODES = new HashSet<>(6);
   static final Set<String> RETRYABLE_ERROR_CODES = new HashSet<>(1);
   static final Set<Integer> RETRYABLE_STATUS_CODES = new HashSet<>(4);
   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");

   public static boolean isRetryableServiceException(SdkBaseException exception) {
      return isAse(exception) && isRetryableServiceException(toAse(exception));
   }

   public static boolean isRetryableServiceException(AmazonServiceException exception) {
      return RETRYABLE_STATUS_CODES.contains(exception.getStatusCode())
         || RETRYABLE_ERROR_CODES.contains(exception.getErrorCode())
         || reasonPhraseMatchesErrorCode(exception, RETRYABLE_ERROR_CODES);
   }

   public static boolean isThrottlingException(SdkBaseException exception) {
      return isAse(exception) && isThrottlingException(toAse(exception));
   }

   public static boolean isThrottlingException(AmazonServiceException exception) {
      return THROTTLING_ERROR_CODES.contains(exception.getErrorCode())
         || exception.getStatusCode() == 429
         || reasonPhraseMatchesErrorCode(exception, THROTTLING_ERROR_CODES);
   }

   public static boolean isRequestEntityTooLargeException(SdkBaseException exception) {
      return isAse(exception) && isRequestEntityTooLargeException(toAse(exception));
   }

   public static boolean isRequestEntityTooLargeException(AmazonServiceException exception) {
      return exception.getStatusCode() == 413;
   }

   public static boolean isClockSkewError(SdkBaseException exception) {
      return isAse(exception) && isClockSkewError(toAse(exception));
   }

   public static boolean isClockSkewError(AmazonServiceException exception) {
      return CLOCK_SKEW_ERROR_CODES.contains(exception.getErrorCode()) || reasonPhraseMatchesErrorCode(exception, CLOCK_SKEW_ERROR_CODES);
   }

   private static boolean isAse(SdkBaseException e) {
      return e instanceof AmazonServiceException;
   }

   private static AmazonServiceException toAse(SdkBaseException e) {
      return (AmazonServiceException)e;
   }

   private static boolean reasonPhraseMatchesErrorCode(AmazonServiceException e, Set<String> errorCodes) {
      String errorCode = e.getErrorCode();
      if (errorCode != null) {
         String statusCode = String.valueOf(e.getStatusCode());
         if (errorCode.startsWith(statusCode)) {
            String reasonPhrase = errorCode.substring(statusCode.length());
            reasonPhrase = WHITESPACE_PATTERN.matcher(reasonPhrase).replaceAll("");
            return errorCodes.contains(reasonPhrase);
         }
      }

      return false;
   }

   static {
      THROTTLING_ERROR_CODES.add("Throttling");
      THROTTLING_ERROR_CODES.add("ThrottlingException");
      THROTTLING_ERROR_CODES.add("ThrottledException");
      THROTTLING_ERROR_CODES.add("ProvisionedThroughputExceededException");
      THROTTLING_ERROR_CODES.add("SlowDown");
      THROTTLING_ERROR_CODES.add("TooManyRequestsException");
      THROTTLING_ERROR_CODES.add("RequestLimitExceeded");
      THROTTLING_ERROR_CODES.add("BandwidthLimitExceeded");
      THROTTLING_ERROR_CODES.add("RequestThrottled");
      THROTTLING_ERROR_CODES.add("RequestThrottledException");
      THROTTLING_ERROR_CODES.add("EC2ThrottledException");
      THROTTLING_ERROR_CODES.add("PriorRequestNotComplete");
      CLOCK_SKEW_ERROR_CODES.add("RequestTimeTooSkewed");
      CLOCK_SKEW_ERROR_CODES.add("RequestExpired");
      CLOCK_SKEW_ERROR_CODES.add("InvalidSignatureException");
      CLOCK_SKEW_ERROR_CODES.add("SignatureDoesNotMatch");
      CLOCK_SKEW_ERROR_CODES.add("AuthFailure");
      CLOCK_SKEW_ERROR_CODES.add("RequestInTheFuture");
      RETRYABLE_ERROR_CODES.add("TransactionInProgressException");
      RETRYABLE_ERROR_CODES.add("RequestTimeout");
      RETRYABLE_ERROR_CODES.add("RequestTimeoutException");
      RETRYABLE_ERROR_CODES.add("IDPCommunicationError");
      RETRYABLE_STATUS_CODES.add(500);
      RETRYABLE_STATUS_CODES.add(502);
      RETRYABLE_STATUS_CODES.add(503);
      RETRYABLE_STATUS_CODES.add(504);
   }
}
