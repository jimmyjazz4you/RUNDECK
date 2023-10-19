package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.transfer.AbortableTransfer;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@SdkInternalApi
final class AbstractDownloadCallableConfig {
   private ExecutorService executor;
   private File destFile;
   private CountDownLatch latch;
   private AbortableTransfer abortableDownload;
   private boolean isDownloadParallel;
   private ScheduledExecutorService timedExecutor;
   private long timeout;

   public ExecutorService getExecutor() {
      return this.executor;
   }

   public void setExecutor(ExecutorService executor) {
      this.executor = executor;
   }

   public AbstractDownloadCallableConfig withExecutor(ExecutorService executor) {
      this.setExecutor(executor);
      return this;
   }

   public File getDestFile() {
      return this.destFile;
   }

   public void setDestFile(File destFile) {
      this.destFile = destFile;
   }

   public AbstractDownloadCallableConfig withDestFile(File destFile) {
      this.setDestFile(destFile);
      return this;
   }

   public CountDownLatch getLatch() {
      return this.latch;
   }

   public void setLatch(CountDownLatch latch) {
      this.latch = latch;
   }

   public AbstractDownloadCallableConfig withLatch(CountDownLatch latch) {
      this.setLatch(latch);
      return this;
   }

   public AbortableTransfer getAbortableDownload() {
      return this.abortableDownload;
   }

   public void setAbortableDownload(AbortableTransfer abortableDownload) {
      this.abortableDownload = abortableDownload;
   }

   public AbstractDownloadCallableConfig withAbortableDownload(AbortableTransfer abortableDownload) {
      this.setAbortableDownload(abortableDownload);
      return this;
   }

   public boolean isDownloadParallel() {
      return this.isDownloadParallel;
   }

   public void setDownloadParallel(boolean downloadParallel) {
      this.isDownloadParallel = downloadParallel;
   }

   public AbstractDownloadCallableConfig withDownloadParallel(boolean downloadParallel) {
      this.setDownloadParallel(downloadParallel);
      return this;
   }

   public ScheduledExecutorService getTimedExecutor() {
      return this.timedExecutor;
   }

   public void setTimedExecutor(ScheduledExecutorService timedExecutor) {
      this.timedExecutor = timedExecutor;
   }

   public AbstractDownloadCallableConfig withTimedExecutor(ScheduledExecutorService timedExecutor) {
      this.setTimedExecutor(timedExecutor);
      return this;
   }

   public long getTimeout() {
      return this.timeout;
   }

   public void setTimeout(long timeout) {
      this.timeout = timeout;
   }

   public AbstractDownloadCallableConfig withTimeout(long timeout) {
      this.setTimeout(timeout);
      return this;
   }
}
