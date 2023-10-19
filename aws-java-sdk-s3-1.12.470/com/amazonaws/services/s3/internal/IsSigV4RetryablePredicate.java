package com.amazonaws.services.s3.internal;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.internal.SdkPredicate;
import java.util.Arrays;
import java.util.List;

public class IsSigV4RetryablePredicate extends SdkPredicate<AmazonServiceException> {
   private static final List<String> AUTH_ERROR_CODES = Arrays.asList("InvalidRequest", "InvalidArgument");
   private static final List<String> AUTH_ERROR_MESSAGES = Arrays.asList(
      "Please use AWS4-HMAC-SHA256.", "AWS KMS managed keys require AWS Signature Version 4."
   );

   public boolean test(AmazonServiceException ase) {
      if (ase != null && ase.getErrorMessage() != null) {
         if (AUTH_ERROR_CODES.contains(ase.getErrorCode())) {
            for(String possibleErrorMessage : AUTH_ERROR_MESSAGES) {
               if (ase.getErrorMessage().contains(possibleErrorMessage)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
