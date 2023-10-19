package com.amazonaws.retry.v2;

import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.List;

public class RetryOnStatusCodeCondition implements RetryCondition {
   private final List<Integer> statusCodesToRetryOn;

   public RetryOnStatusCodeCondition(List<Integer> statusCodesToRetryOn) {
      this.statusCodesToRetryOn = new ArrayList<>(ValidationUtils.assertNotNull(statusCodesToRetryOn, "statusCodesToRetryOn"));
   }

   @Override
   public boolean shouldRetry(RetryPolicyContext context) {
      if (context.httpStatusCode() != null) {
         for(Integer statusCode : this.statusCodesToRetryOn) {
            if (statusCode.equals(context.httpStatusCode())) {
               return true;
            }
         }
      }

      return false;
   }
}
