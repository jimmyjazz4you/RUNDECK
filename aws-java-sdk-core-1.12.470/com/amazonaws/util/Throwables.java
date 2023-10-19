package com.amazonaws.util;

import com.amazonaws.AbortedException;
import com.amazonaws.AmazonClientException;
import org.apache.commons.logging.LogFactory;

public enum Throwables {
   public static Throwable getRootCause(Throwable orig) {
      if (orig == null) {
         return orig;
      } else {
         Throwable t = orig;

         for(int i = 0; i < 1000; ++i) {
            Throwable cause = t.getCause();
            if (cause == null) {
               return t;
            }

            t = cause;
         }

         LogFactory.getLog(Throwables.class).debug("Possible circular reference detected on " + orig.getClass() + ": [" + orig + "]");
         return orig;
      }
   }

   public static RuntimeException failure(Throwable t) {
      if (t instanceof RuntimeException) {
         return (RuntimeException)t;
      } else if (t instanceof Error) {
         throw (Error)t;
      } else {
         return (RuntimeException)(t instanceof InterruptedException ? new AbortedException(t) : new AmazonClientException(t));
      }
   }

   public static RuntimeException failure(Throwable t, String errmsg) {
      if (t instanceof RuntimeException) {
         return (RuntimeException)t;
      } else if (t instanceof Error) {
         throw (Error)t;
      } else {
         return (RuntimeException)(t instanceof InterruptedException ? new AbortedException(errmsg, t) : new AmazonClientException(errmsg, t));
      }
   }
}
