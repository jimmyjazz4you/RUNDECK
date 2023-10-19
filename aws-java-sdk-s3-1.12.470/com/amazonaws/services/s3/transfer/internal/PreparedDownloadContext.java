package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.annotation.SdkInternalApi;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

@SdkInternalApi
public final class PreparedDownloadContext {
   private final DownloadImpl transfer;
   private final Callable<File> callable;
   private final CountDownLatch latch;

   public PreparedDownloadContext(DownloadImpl transfer, Callable<File> callable, CountDownLatch latch) {
      this.transfer = transfer;
      this.callable = callable;
      this.latch = latch;
   }

   public DownloadImpl getTransfer() {
      return this.transfer;
   }

   public Callable<File> getCallable() {
      return this.callable;
   }

   public CountDownLatch getLatch() {
      return this.latch;
   }
}
