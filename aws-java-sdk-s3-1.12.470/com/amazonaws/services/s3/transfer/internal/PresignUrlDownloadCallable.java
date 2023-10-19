package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.FileLocks;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.exception.FileLockException;
import java.io.File;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import javax.net.ssl.SSLProtocolException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public class PresignUrlDownloadCallable extends AbstractDownloadCallable {
   private static final Log LOG = LogFactory.getLog(PresignUrlDownloadCallable.class);
   private final AmazonS3 s3;
   private final PresignedUrlDownloadRequest request;
   private final PresignedUrlDownloadImpl download;
   private final long perRequestDownloadSize;
   private final Long startByte;
   private final Long endByte;
   private final boolean resumeOnRetry;
   private long expectedFileLength;
   private static boolean testing;

   public PresignUrlDownloadCallable(
      ExecutorService executor,
      File dstfile,
      CountDownLatch latch,
      PresignedUrlDownloadImpl download,
      boolean isDownloadParallel,
      ScheduledExecutorService timedExecutor,
      long timeout,
      AmazonS3 s3,
      PresignedUrlDownloadRequest request,
      long perRequestDownloadSize,
      Long startByte,
      Long endByte,
      boolean resumeOnRetry
   ) {
      super(constructCallableConfig(executor, dstfile, latch, download, isDownloadParallel, timedExecutor, timeout));
      if (s3 != null && request != null && download != null) {
         this.s3 = s3;
         this.request = request;
         this.download = download;
         this.perRequestDownloadSize = perRequestDownloadSize;
         this.startByte = startByte;
         this.endByte = endByte;
         this.resumeOnRetry = resumeOnRetry;
         this.expectedFileLength = 0L;
      } else {
         throw new IllegalArgumentException();
      }
   }

   @Override
   protected void downloadAsSingleObject() {
      S3Object s3Object = this.retryableDownloadS3ObjectToFile(this.dstfile, new PresignedUrlRetryableDownloadTaskImpl(this.s3, this.download, this.request));
      this.updateDownloadStatus(s3Object);
   }

   @Override
   protected void downloadInParallel() throws Exception {
      this.downloadInParallelUsingRange();
   }

   @Override
   protected void setState(Transfer.TransferState transferState) {
      this.download.setState(transferState);
   }

   private void updateDownloadStatus(S3Object result) {
      if (result == null) {
         this.download.setState(Transfer.TransferState.Canceled);
         this.download.setMonitor(new DownloadMonitor(this.download, null));
      } else {
         this.download.setState(Transfer.TransferState.Completed);
      }
   }

   private S3Object retryableDownloadS3ObjectToFile(File file, ServiceUtils.RetryableS3DownloadTask retryableS3DownloadTask) {
      boolean hasRetried = false;

      while(true) {
         boolean appendData = this.resumeOnRetry && this.canResumeDownload() && hasRetried;
         if (appendData) {
            this.adjustRequest(this.request);
         }

         S3Object s3Object = retryableS3DownloadTask.getS3ObjectStream();
         if (s3Object == null) {
            return null;
         }

         try {
            if (testing && !hasRetried) {
               throw new SdkClientException("testing");
            }

            ServiceUtils.downloadToFile(s3Object, file, retryableS3DownloadTask.needIntegrityCheck(), appendData, this.expectedFileLength);
            return s3Object;
         } catch (AmazonClientException var11) {
            if (!var11.isRetryable()) {
               throw var11;
            }

            Throwable cause = var11.getCause();
            if (cause instanceof SocketException && !cause.getMessage().equals("Connection reset") || cause instanceof SSLProtocolException) {
               throw var11;
            }

            if (hasRetried) {
               throw var11;
            }

            LOG.debug("Retry the download of object " + s3Object.getKey() + " (bucket " + s3Object.getBucketName() + ")", var11);
            hasRetried = true;
         } finally {
            s3Object.getObjectContent().abort();
         }
      }
   }

   private boolean canResumeDownload() {
      return this.startByte != null && this.endByte != null;
   }

   private void adjustRequest(PresignedUrlDownloadRequest request) {
      long start = -1L;
      long end = -1L;
      if (request.getRange() != null) {
         long[] range = request.getRange();
         start = range[0];
         end = range[1];
      } else {
         start = this.startByte;
         end = this.endByte;
      }

      if (this.dstfile.exists()) {
         if (!FileLocks.lock(this.dstfile)) {
            throw new FileLockException("Fail to lock " + this.dstfile + " for range adjustment");
         } else {
            try {
               this.expectedFileLength = this.dstfile.length();
               long newStart = start + this.expectedFileLength;
               LOG.debug(
                  "Adjusting request range from "
                     + Arrays.toString(new long[]{start, end})
                     + " to "
                     + Arrays.toString(new long[]{newStart, end})
                     + " for file "
                     + this.dstfile
               );
               request.setRange(newStart, end);
               long totalBytesToDownload = end - newStart + 1L;
               if (totalBytesToDownload < 0L) {
                  throw new IllegalArgumentException(
                     "Unable to determine the range for download operation. lastByte="
                        + end
                        + ", StartingByte="
                        + newStart
                        + ", expectedFileLength="
                        + this.expectedFileLength
                        + ", totalBytesToDownload="
                        + totalBytesToDownload
                  );
               }
            } finally {
               FileLocks.unlock(this.dstfile);
            }
         }
      }
   }

   private void downloadInParallelUsingRange() throws Exception {
      ServiceUtils.createParentDirectoryIfNecessary(this.dstfile);
      if (!FileLocks.lock(this.dstfile)) {
         throw new FileLockException("Fail to lock " + this.dstfile);
      } else {
         long currentStart = this.startByte;
         long currentEnd = 0L;

         for(long filePositionToWrite = 0L; currentStart <= this.endByte; currentStart = currentEnd + 1L) {
            currentEnd = currentStart + this.perRequestDownloadSize - 1L;
            if (currentEnd > this.endByte) {
               currentEnd = this.endByte;
            }

            PresignedUrlDownloadRequest rangeRequest = this.request.clone();
            rangeRequest.setRange(currentStart, currentEnd);
            Callable<S3Object> s3Object = this.serviceCall(rangeRequest);
            this.futures.add(this.executor.submit(new DownloadS3ObjectCallable(s3Object, this.dstfile, filePositionToWrite)));
            filePositionToWrite += this.perRequestDownloadSize;
         }

         Future<File> future = this.executor.submit(this.completeAllFutures());
         ((DownloadMonitor)this.download.getMonitor()).setFuture(future);
      }
   }

   private Callable<S3Object> serviceCall(final PresignedUrlDownloadRequest presignedUrlDownloadRequest) {
      return new Callable<S3Object>() {
         public S3Object call() throws Exception {
            return PresignUrlDownloadCallable.this.s3.download(presignedUrlDownloadRequest).getS3Object();
         }
      };
   }

   private Callable<File> completeAllFutures() {
      return new Callable<File>() {
         public File call() throws Exception {
            try {
               for(Future<Long> future : PresignUrlDownloadCallable.this.futures) {
                  future.get();
               }

               PresignUrlDownloadCallable.this.download.setState(Transfer.TransferState.Completed);
            } finally {
               FileLocks.unlock(PresignUrlDownloadCallable.this.dstfile);
            }

            return PresignUrlDownloadCallable.this.dstfile;
         }
      };
   }

   @SdkTestInternalApi
   public static void setTesting(boolean b) {
      testing = b;
   }
}
