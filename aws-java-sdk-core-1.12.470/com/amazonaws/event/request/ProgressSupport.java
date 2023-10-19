package com.amazonaws.event.request;

import com.amazonaws.annotation.ThreadSafe;

@ThreadSafe
public class ProgressSupport extends Progress {
   private volatile long requestContentLength = -1L;
   private volatile long requestBytesTransferred;
   private volatile long responseContentLength = -1L;
   private volatile long responseBytesTransferred;
   private static final Object lock = new Object();

   @Override
   public long getRequestContentLength() {
      return this.requestContentLength;
   }

   @Override
   public void addRequestContentLength(long contentLength) {
      if (contentLength < 0L) {
         throw new IllegalArgumentException();
      } else {
         synchronized(lock) {
            if (this.requestContentLength == -1L) {
               this.requestContentLength = contentLength;
            } else {
               this.requestContentLength += contentLength;
            }
         }
      }
   }

   @Override
   public long getRequestBytesTransferred() {
      return this.requestBytesTransferred;
   }

   @Override
   public long getResponseContentLength() {
      return this.responseContentLength;
   }

   @Override
   public void addResponseContentLength(long contentLength) {
      if (contentLength < 0L) {
         throw new IllegalArgumentException();
      } else {
         synchronized(lock) {
            if (this.responseContentLength == -1L) {
               this.responseContentLength = contentLength;
            } else {
               this.responseContentLength += contentLength;
            }
         }
      }
   }

   @Override
   public long getResponseBytesTransferred() {
      return this.responseBytesTransferred;
   }

   @Override
   public void addRequestBytesTransferred(long bytes) {
      synchronized(lock) {
         this.requestBytesTransferred += bytes;
      }
   }

   @Override
   public void addResponseBytesTransferred(long bytes) {
      synchronized(lock) {
         this.responseBytesTransferred += bytes;
      }
   }

   @Override
   public String toString() {
      return String.format(
         "Request: %d/%d, Response: %d/%d", this.requestBytesTransferred, this.requestContentLength, this.responseBytesTransferred, this.responseContentLength
      );
   }

   @Override
   public final boolean isEnabled() {
      return true;
   }
}
