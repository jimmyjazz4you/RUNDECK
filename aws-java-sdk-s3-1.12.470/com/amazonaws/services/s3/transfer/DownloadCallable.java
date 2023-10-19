package com.amazonaws.services.s3.transfer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.FileLocks;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.exception.FileLockException;
import com.amazonaws.services.s3.transfer.internal.AbstractDownloadCallable;
import com.amazonaws.services.s3.transfer.internal.CompleteMultipartDownload;
import com.amazonaws.services.s3.transfer.internal.DownloadImpl;
import com.amazonaws.services.s3.transfer.internal.DownloadMonitor;
import com.amazonaws.services.s3.transfer.internal.DownloadS3ObjectCallable;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.RandomAccessFile;
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
final class DownloadCallable extends AbstractDownloadCallable {
   private static final Log LOG = LogFactory.getLog(DownloadCallable.class);
   private final AmazonS3 s3;
   private final GetObjectRequest req;
   private final boolean resumeExistingDownload;
   private final DownloadImpl download;
   private final long origStartingByte;
   private Integer lastFullyMergedPartNumber;
   private Long lastFullyMergedPartPosition;
   private final boolean resumeOnRetry;
   private long expectedFileLength;
   private static boolean testing;

   DownloadCallable(
      AmazonS3 s3,
      CountDownLatch latch,
      GetObjectRequest req,
      boolean resumeExistingDownload,
      DownloadImpl download,
      File dstfile,
      long origStartingByte,
      long expectedFileLength,
      long timeout,
      ScheduledExecutorService timedExecutor,
      ExecutorService executor,
      Integer lastFullyDownloadedPartNumber,
      boolean isDownloadParallel,
      boolean resumeOnRetry
   ) {
      super(constructCallableConfig(executor, dstfile, latch, download, isDownloadParallel, timedExecutor, timeout));
      if (s3 != null && req != null && download != null) {
         this.s3 = s3;
         this.req = req;
         this.resumeExistingDownload = resumeExistingDownload;
         this.download = download;
         this.origStartingByte = origStartingByte;
         this.expectedFileLength = expectedFileLength;
         this.lastFullyMergedPartNumber = lastFullyDownloadedPartNumber;
         this.resumeOnRetry = resumeOnRetry;
      } else {
         throw new IllegalArgumentException();
      }
   }

   DownloadCallable withLastFullyMergedPartPosition(Long lastFullyMergedPartPosition) {
      this.lastFullyMergedPartPosition = lastFullyMergedPartPosition;
      return this;
   }

   @Override
   protected void downloadAsSingleObject() {
      S3Object s3Object = this.retryableDownloadS3ObjectToFile(this.dstfile, new DownloadTaskImpl(this.s3, this.download, this.req));
      this.updateDownloadStatus(s3Object);
   }

   @Override
   protected void downloadInParallel() throws Exception {
      this.downloadInParallel(ServiceUtils.getPartCount(this.req, this.s3));
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

   private void downloadInParallel(int partCount) throws Exception {
      if (this.lastFullyMergedPartNumber == null) {
         this.lastFullyMergedPartNumber = 0;
      }

      if (this.lastFullyMergedPartPosition == null) {
         this.lastFullyMergedPartPosition = 0L;
      }

      long previousPartLength = 0L;
      long filePositionToWrite = this.lastFullyMergedPartPosition;
      ServiceUtils.createParentDirectoryIfNecessary(this.dstfile);
      this.truncateDestinationFileIfNecessary();
      if (!FileLocks.lock(this.dstfile)) {
         throw new FileLockException("Fail to lock " + this.dstfile);
      } else {
         try {
            for(int i = this.lastFullyMergedPartNumber + 1; i <= partCount; ++i) {
               filePositionToWrite += previousPartLength;
               GetObjectRequest getPartRequest = (GetObjectRequest)new GetObjectRequest(this.req.getBucketName(), this.req.getKey(), this.req.getVersionId())
                  .withUnmodifiedSinceConstraint(this.req.getUnmodifiedSinceConstraint())
                  .withModifiedSinceConstraint(this.req.getModifiedSinceConstraint())
                  .withResponseHeaders(this.req.getResponseHeaders())
                  .withSSECustomerKey(this.req.getSSECustomerKey())
                  .withGeneralProgressListener(this.req.getGeneralProgressListener());
               getPartRequest.setMatchingETagConstraints(this.req.getMatchingETagConstraints());
               getPartRequest.setNonmatchingETagConstraints(this.req.getNonmatchingETagConstraints());
               getPartRequest.setRequesterPays(this.req.isRequesterPays());
               getPartRequest.setRequestCredentialsProvider(this.req.getRequestCredentialsProvider());
               getPartRequest.setPartNumber(i);
               this.futures.add(this.executor.submit(new DownloadS3ObjectCallable(this.serviceCall(getPartRequest), this.dstfile, filePositionToWrite)));
               previousPartLength = ServiceUtils.getPartSize(this.req, this.s3, i);
            }

            Future<File> future = this.executor
               .submit(
                  new CompleteMultipartDownload(this.futures, this.dstfile, this.download, this.lastFullyMergedPartNumber = this.lastFullyMergedPartNumber + 1)
               );
            ((DownloadMonitor)this.download.getMonitor()).setFuture(future);
         } catch (Exception var8) {
            FileLocks.unlock(this.dstfile);
            throw var8;
         }
      }
   }

   private Callable<S3Object> serviceCall(final GetObjectRequest request) {
      return new Callable<S3Object>() {
         public S3Object call() throws Exception {
            return DownloadCallable.this.s3.getObject(request);
         }
      };
   }

   private void truncateDestinationFileIfNecessary() {
      RandomAccessFile raf = null;
      if (!FileLocks.lock(this.dstfile)) {
         throw new FileLockException("Fail to lock " + this.dstfile);
      } else {
         try {
            raf = new RandomAccessFile(this.dstfile, "rw");
            if (this.lastFullyMergedPartNumber == 0) {
               raf.setLength(0L);
            } else {
               long lastByte = ServiceUtils.getLastByteInPart(this.s3, this.req, this.lastFullyMergedPartNumber);
               if (this.dstfile.length() < lastByte) {
                  throw new SdkClientException("File " + this.dstfile.getAbsolutePath() + " has been modified since last pause.");
               }

               raf.setLength(lastByte + 1L);
               this.download.getProgress().updateProgress(lastByte + 1L);
            }
         } catch (Exception var7) {
            throw new SdkClientException("Unable to append part file to dstfile " + var7.getMessage(), var7);
         } finally {
            IOUtils.closeQuietly(raf, LOG);
            FileLocks.unlock(this.dstfile);
         }
      }
   }

   private void adjustRequest(GetObjectRequest req) {
      long[] range = req.getRange();
      long lastByte = range[1];
      long totalBytesToDownload = lastByte - this.origStartingByte + 1L;
      if (this.dstfile.exists()) {
         if (!FileLocks.lock(this.dstfile)) {
            throw new FileLockException("Fail to lock " + this.dstfile + " for range adjustment");
         }

         try {
            this.expectedFileLength = this.dstfile.length();
            long startingByte = this.origStartingByte + this.expectedFileLength;
            LOG.info(
               "Adjusting request range from "
                  + Arrays.toString(range)
                  + " to "
                  + Arrays.toString(new long[]{startingByte, lastByte})
                  + " for file "
                  + this.dstfile
            );
            req.setRange(startingByte, lastByte);
            totalBytesToDownload = lastByte - startingByte + 1L;
         } finally {
            FileLocks.unlock(this.dstfile);
         }
      }

      if (totalBytesToDownload < 0L) {
         throw new IllegalArgumentException(
            "Unable to determine the range for download operation. lastByte="
               + lastByte
               + ", origStartingByte="
               + this.origStartingByte
               + ", expectedFileLength="
               + this.expectedFileLength
               + ", totalBytesToDownload="
               + totalBytesToDownload
         );
      }
   }

   private S3Object retryableDownloadS3ObjectToFile(File file, ServiceUtils.RetryableS3DownloadTask retryableS3DownloadTask) {
      boolean hasRetried = false;

      while(true) {
         boolean appendData = this.resumeExistingDownload || this.resumeOnRetry && hasRetried;
         if (appendData && hasRetried) {
            this.adjustRequest(this.req);
         }

         S3Object s3Object = retryableS3DownloadTask.getS3ObjectStream();
         if (s3Object == null) {
            return null;
         }

         try {
            if (testing && this.resumeExistingDownload && !hasRetried) {
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

            LOG.info("Retry the download of object " + s3Object.getKey() + " (bucket " + s3Object.getBucketName() + ")", var11);
            hasRetried = true;
         } finally {
            s3Object.getObjectContent().abort();
         }
      }
   }

   static void setTesting(boolean b) {
      testing = b;
   }
}
