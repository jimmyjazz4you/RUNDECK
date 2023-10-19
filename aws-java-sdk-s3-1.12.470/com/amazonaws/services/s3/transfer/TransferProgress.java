package com.amazonaws.services.s3.transfer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class TransferProgress {
   private static final Log log = LogFactory.getLog(TransferProgress.class);
   private volatile long bytesTransferred = 0L;
   private volatile long totalBytesToTransfer = -1L;

   /** @deprecated */
   public long getBytesTransfered() {
      return this.getBytesTransferred();
   }

   public long getBytesTransferred() {
      return this.bytesTransferred;
   }

   public long getTotalBytesToTransfer() {
      return this.totalBytesToTransfer;
   }

   @Deprecated
   public synchronized double getPercentTransfered() {
      return this.getPercentTransferred();
   }

   public synchronized double getPercentTransferred() {
      if (this.getBytesTransferred() < 0L) {
         return 0.0;
      } else {
         return this.totalBytesToTransfer < 0L ? -1.0 : (double)this.bytesTransferred / (double)this.totalBytesToTransfer * 100.0;
      }
   }

   public synchronized void updateProgress(long bytes) {
      this.bytesTransferred += bytes;
      if (this.totalBytesToTransfer > -1L && this.bytesTransferred > this.totalBytesToTransfer) {
         this.bytesTransferred = this.totalBytesToTransfer;
         if (log.isDebugEnabled()) {
            log.debug(
               "Number of bytes transfered is more than the actual total bytes to transfer. Total number of bytes to Transfer : "
                  + this.totalBytesToTransfer
                  + ". Bytes Transferred : "
                  + (this.bytesTransferred + bytes)
            );
         }
      }
   }

   public void setTotalBytesToTransfer(long totalBytesToTransfer) {
      this.totalBytesToTransfer = totalBytesToTransfer;
   }
}
