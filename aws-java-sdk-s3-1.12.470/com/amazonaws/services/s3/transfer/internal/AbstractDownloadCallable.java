package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.transfer.AbortableTransfer;
import com.amazonaws.services.s3.transfer.Transfer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SdkInternalApi
public abstract class AbstractDownloadCallable implements Callable<File> {
   protected final ExecutorService executor;
   protected final List<Future<Long>> futures;
   protected final File dstfile;
   private final CountDownLatch latch;
   private final AbortableTransfer abortableDownload;
   private final boolean isDownloadParallel;
   private final ScheduledExecutorService timedExecutor;
   private final long timeout;

   protected AbstractDownloadCallable(AbstractDownloadCallableConfig options) {
      if (options.getLatch() != null && options.getDestFile() != null && options.getAbortableDownload() != null) {
         this.executor = options.getExecutor();
         this.timedExecutor = options.getTimedExecutor();
         this.futures = new ArrayList<>();
         this.dstfile = options.getDestFile();
         this.latch = options.getLatch();
         this.abortableDownload = options.getAbortableDownload();
         this.isDownloadParallel = options.isDownloadParallel();
         this.timeout = options.getTimeout();
      } else {
         throw new IllegalArgumentException();
      }
   }

   public File call() throws Exception {
      try {
         this.latch.await();
         if (this.isTimeoutEnabled()) {
            this.timedExecutor.schedule(new Runnable() {
               @Override
               public void run() {
                  try {
                     if (AbstractDownloadCallable.this.abortableDownload.getState() != Transfer.TransferState.Completed) {
                        AbstractDownloadCallable.this.abortableDownload.abort();
                     }
                  } catch (Exception var2) {
                     throw new SdkClientException("Unable to abort download after timeout", var2);
                  }
               }
            }, this.timeout, TimeUnit.MILLISECONDS);
         }

         this.setState(Transfer.TransferState.InProgress);
         ServiceUtils.createParentDirectoryIfNecessary(this.dstfile);
         if (this.isDownloadParallel) {
            this.downloadInParallel();
         } else {
            this.downloadAsSingleObject();
         }

         return this.dstfile;
      } catch (Throwable var2) {
         this.cleanupAfterException();
         if (var2 instanceof Exception) {
            throw (Exception)var2;
         } else {
            throw (Error)var2;
         }
      }
   }

   protected abstract void setState(Transfer.TransferState var1);

   protected abstract void downloadAsSingleObject();

   protected abstract void downloadInParallel() throws Exception;

   protected static AbstractDownloadCallableConfig constructCallableConfig(
      ExecutorService executor,
      File dstfile,
      CountDownLatch latch,
      AbortableTransfer download,
      boolean isDownloadParallel,
      ScheduledExecutorService timedExecutor,
      long timeout
   ) {
      return new AbstractDownloadCallableConfig()
         .withExecutor(executor)
         .withDestFile(dstfile)
         .withLatch(latch)
         .withAbortableDownload(download)
         .withDownloadParallel(isDownloadParallel)
         .withTimedExecutor(timedExecutor)
         .withTimeout(timeout);
   }

   private boolean isTimeoutEnabled() {
      return this.timeout > 0L;
   }

   private void cleanupAfterException() {
      for(Future<Long> f : this.futures) {
         f.cancel(true);
      }

      if (this.abortableDownload.getState() != Transfer.TransferState.Canceled) {
         this.setState(Transfer.TransferState.Failed);
      }
   }
}
