package com.amazonaws.retry.v2;

import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.List;

public class RetryOnExceptionsCondition implements RetryCondition {
   private final List<Class<? extends Exception>> exceptionsToRetryOn;

   public RetryOnExceptionsCondition(List<Class<? extends Exception>> exceptionsToRetryOn) {
      this.exceptionsToRetryOn = new ArrayList<>(ValidationUtils.assertNotNull(exceptionsToRetryOn, "exceptionsToRetryOn"));
   }

   @Override
   public boolean shouldRetry(RetryPolicyContext context) {
      if (context.exception() != null) {
         for(Class<? extends Exception> exceptionClass : this.exceptionsToRetryOn) {
            if (this.exceptionMatches(context, exceptionClass)) {
               return true;
            }

            if (this.wrappedCauseMatches(context, exceptionClass)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean exceptionMatches(RetryPolicyContext context, Class<? extends Exception> exceptionClass) {
      return context.exception().getClass().equals(exceptionClass);
   }

   private boolean wrappedCauseMatches(RetryPolicyContext context, Class<? extends Exception> exceptionClass) {
      return context.exception().getCause() == null ? false : context.exception().getCause().getClass().equals(exceptionClass);
   }
}
