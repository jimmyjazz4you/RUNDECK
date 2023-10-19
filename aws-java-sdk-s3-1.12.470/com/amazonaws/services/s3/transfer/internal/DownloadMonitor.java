package com.amazonaws.services.s3.transfer.internal;

import java.io.File;
import java.util.concurrent.Future;

public class DownloadMonitor implements TransferMonitor {
   private Future<File> future;
   private final AbstractTransfer download;

   public DownloadMonitor(AbstractTransfer download, Future<File> future) {
      this.download = download;
      this.future = future;
   }

   @Override
   public synchronized Future<File> getFuture() {
      return this.future;
   }

   public synchronized void setFuture(Future<File> future) {
      this.future = future;
   }

   @Override
   public boolean isDone() {
      return this.download.isDone();
   }
}
