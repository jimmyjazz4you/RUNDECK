package com.amazonaws.services.s3.transfer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.internal.FileLocks;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.internal.RequestCopyUtils;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListMultipartUploadsRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.MultipartUpload;
import com.amazonaws.services.s3.model.MultipartUploadListing;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PresignedUrlDownloadConfig;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.exception.FileLockException;
import com.amazonaws.services.s3.transfer.internal.CopyCallable;
import com.amazonaws.services.s3.transfer.internal.CopyImpl;
import com.amazonaws.services.s3.transfer.internal.CopyMonitor;
import com.amazonaws.services.s3.transfer.internal.DownloadImpl;
import com.amazonaws.services.s3.transfer.internal.DownloadMonitor;
import com.amazonaws.services.s3.transfer.internal.MultipleFileDownloadImpl;
import com.amazonaws.services.s3.transfer.internal.MultipleFileTransferMonitor;
import com.amazonaws.services.s3.transfer.internal.MultipleFileUploadImpl;
import com.amazonaws.services.s3.transfer.internal.PreparedDownloadContext;
import com.amazonaws.services.s3.transfer.internal.PresignUrlDownloadCallable;
import com.amazonaws.services.s3.transfer.internal.PresignedUrlDownloadImpl;
import com.amazonaws.services.s3.transfer.internal.S3ProgressListener;
import com.amazonaws.services.s3.transfer.internal.S3ProgressListenerChain;
import com.amazonaws.services.s3.transfer.internal.TransferManagerUtils;
import com.amazonaws.services.s3.transfer.internal.TransferProgressUpdatingListener;
import com.amazonaws.services.s3.transfer.internal.TransferStateChangeListener;
import com.amazonaws.services.s3.transfer.internal.UploadCallable;
import com.amazonaws.services.s3.transfer.internal.UploadImpl;
import com.amazonaws.services.s3.transfer.internal.UploadMonitor;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.VersionInfoUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransferManager {
   private final AmazonS3 s3;
   private TransferManagerConfiguration configuration;
   private final ExecutorService executorService;
   private final ScheduledExecutorService timedThreadPool;
   private static final Log log = LogFactory.getLog(TransferManager.class);
   private final boolean shutDownThreadPools;
   private final boolean isImmutable;
   private static final String USER_AGENT = TransferManager.class.getName() + "/" + VersionInfoUtils.getVersion();
   private static final String USER_AGENT_MULTIPART = TransferManager.class.getName() + "_multipart/" + VersionInfoUtils.getVersion();
   private static final String DEFAULT_DELIMITER = "/";
   private static final ThreadFactory daemonThreadFactory = new ThreadFactory() {
      final AtomicInteger threadCount = new AtomicInteger(0);

      @Override
      public Thread newThread(Runnable r) {
         int threadNumber = this.threadCount.incrementAndGet();
         Thread thread = new Thread(r);
         thread.setDaemon(true);
         thread.setName("S3TransferManagerTimedThread-" + threadNumber);
         return thread;
      }
   };

   @Deprecated
   public TransferManager() {
      this(new AmazonS3Client(new DefaultAWSCredentialsProviderChain()));
   }

   @Deprecated
   public TransferManager(AWSCredentialsProvider credentialsProvider) {
      this(new AmazonS3Client(credentialsProvider));
   }

   @Deprecated
   public TransferManager(AWSCredentials credentials) {
      this(new AmazonS3Client(credentials));
   }

   @Deprecated
   public TransferManager(AmazonS3 s3) {
      this(s3, TransferManagerUtils.createDefaultExecutorService());
   }

   @Deprecated
   public TransferManager(AmazonS3 s3, ExecutorService executorService) {
      this(s3, executorService, true);
   }

   @Deprecated
   public TransferManager(AmazonS3 s3, ExecutorService executorService, boolean shutDownThreadPools) {
      this.timedThreadPool = new ScheduledThreadPoolExecutor(1, daemonThreadFactory);
      this.s3 = s3;
      this.executorService = executorService;
      this.configuration = new TransferManagerConfiguration();
      this.shutDownThreadPools = shutDownThreadPools;
      this.isImmutable = false;
   }

   @SdkInternalApi
   TransferManager(TransferManagerParams params) {
      this.timedThreadPool = new ScheduledThreadPoolExecutor(1, daemonThreadFactory);
      this.s3 = params.getS3Client();
      this.executorService = params.getExecutorService();
      this.configuration = params.getConfiguration();
      this.shutDownThreadPools = params.getShutDownThreadPools();
      this.isImmutable = true;
   }

   protected TransferManager(TransferManagerBuilder builder) {
      this(builder.getParams());
   }

   @Deprecated
   public void setConfiguration(TransferManagerConfiguration configuration) {
      this.checkMutability();
      this.configuration = configuration;
   }

   public TransferManagerConfiguration getConfiguration() {
      return this.configuration;
   }

   public AmazonS3 getAmazonS3Client() {
      return this.s3;
   }

   public Upload upload(String bucketName, String key, InputStream input, ObjectMetadata objectMetadata) throws AmazonServiceException, AmazonClientException {
      return this.upload(new PutObjectRequest(bucketName, key, input, objectMetadata));
   }

   public Upload upload(String bucketName, String key, File file) throws AmazonServiceException, AmazonClientException {
      return this.upload(new PutObjectRequest(bucketName, key, file));
   }

   public Upload upload(PutObjectRequest putObjectRequest) throws AmazonServiceException, AmazonClientException {
      return this.doUpload(putObjectRequest, null, null, null);
   }

   public Upload upload(PutObjectRequest putObjectRequest, S3ProgressListener progressListener) throws AmazonServiceException, AmazonClientException {
      return this.doUpload(putObjectRequest, null, progressListener, null);
   }

   private Upload doUpload(
      PutObjectRequest putObjectRequest, TransferStateChangeListener stateListener, S3ProgressListener progressListener, PersistableUpload persistableUpload
   ) throws AmazonServiceException, AmazonClientException {
      assertNotObjectLambdaArn(putObjectRequest.getBucketName(), "upload");
      appendSingleObjectUserAgent(putObjectRequest);
      String multipartUploadId = persistableUpload != null ? persistableUpload.getMultipartUploadId() : null;
      if (putObjectRequest.getMetadata() == null) {
         putObjectRequest.setMetadata(new ObjectMetadata());
      }

      ObjectMetadata metadata = putObjectRequest.getMetadata();
      File file = TransferManagerUtils.getRequestFile(putObjectRequest);
      if (file != null) {
         metadata.setContentLength(file.length());
         if (metadata.getContentType() == null) {
            metadata.setContentType(Mimetypes.getInstance().getMimetype(file));
         }
      } else if (multipartUploadId != null) {
         throw new IllegalArgumentException("Unable to resume the upload. No file specified.");
      }

      String description = "Uploading to " + putObjectRequest.getBucketName() + "/" + putObjectRequest.getKey();
      TransferProgress transferProgress = new TransferProgress();
      transferProgress.setTotalBytesToTransfer(TransferManagerUtils.getContentLength(putObjectRequest));
      S3ProgressListenerChain listenerChain = new S3ProgressListenerChain(
         new TransferProgressUpdatingListener(transferProgress), putObjectRequest.getGeneralProgressListener(), progressListener
      );
      putObjectRequest.setGeneralProgressListener(listenerChain);
      UploadImpl upload = new UploadImpl(description, transferProgress, listenerChain, stateListener);
      UploadCallable uploadCallable = new UploadCallable(
         this, this.executorService, upload, putObjectRequest, listenerChain, multipartUploadId, transferProgress
      );
      UploadMonitor watcher = UploadMonitor.create(this, upload, this.executorService, uploadCallable, putObjectRequest, listenerChain);
      upload.setMonitor(watcher);
      return upload;
   }

   public Download download(String bucket, String key, File file) {
      return this.download(bucket, key, file, 0L);
   }

   public Download download(String bucket, String key, File file, long timeoutMillis) {
      return this.download(new GetObjectRequest(bucket, key), file, timeoutMillis);
   }

   public Download download(GetObjectRequest getObjectRequest, File file) {
      return this.download(getObjectRequest, file, 0L);
   }

   public Download download(GetObjectRequest getObjectRequest, File file, long timeoutMillis) {
      return this.doDownload(getObjectRequest, file, null, null, false, timeoutMillis, null);
   }

   public Download download(GetObjectRequest getObjectRequest, File file, S3ProgressListener progressListener) {
      return this.doDownload(getObjectRequest, file, null, progressListener, false, 0L, null);
   }

   public Download download(GetObjectRequest getObjectRequest, File file, S3ProgressListener progressListener, long timeoutMillis) {
      return this.doDownload(getObjectRequest, file, null, progressListener, false, timeoutMillis, null);
   }

   public Download download(GetObjectRequest getObjectRequest, File file, S3ProgressListener progressListener, long timeoutMillis, boolean resumeOnRetry) {
      return this.doDownload(getObjectRequest, file, null, progressListener, false, timeoutMillis, null, 0L, resumeOnRetry, 0L);
   }

   private Download doDownload(
      GetObjectRequest getObjectRequest,
      File file,
      TransferStateChangeListener stateListener,
      S3ProgressListener s3progressListener,
      boolean resumeExistingDownload,
      long timeoutMillis,
      PersistableDownload persistableDownload
   ) {
      assertNotObjectLambdaArn(getObjectRequest.getBucketName(), "download");
      long lastModifiedTimeRecordedDuringPause = 0L;
      Integer lastFullyDownloadedPartNumber = null;
      Long lastFullyDownloadedFilePosition = null;
      if (persistableDownload != null) {
         lastModifiedTimeRecordedDuringPause = persistableDownload.getlastModifiedTime();
         lastFullyDownloadedPartNumber = persistableDownload.getLastFullyDownloadedPartNumber();
         lastFullyDownloadedFilePosition = persistableDownload.getLastFullyDownloadedFilePosition();
      }

      return this.doDownload(
         getObjectRequest,
         file,
         stateListener,
         s3progressListener,
         resumeExistingDownload,
         timeoutMillis,
         lastFullyDownloadedPartNumber,
         lastModifiedTimeRecordedDuringPause,
         false,
         lastFullyDownloadedFilePosition
      );
   }

   private Download doDownload(
      GetObjectRequest getObjectRequest,
      File file,
      TransferStateChangeListener stateListener,
      S3ProgressListener s3progressListener,
      boolean resumeExistingDownload,
      long timeoutMillis,
      Integer lastFullyDownloadedPart,
      long lastModifiedTimeRecordedDuringPause,
      boolean resumeOnRetry,
      Long lastFullyDownloadedPartPosition
   ) {
      assertNotObjectLambdaArn(getObjectRequest.getBucketName(), "download");
      PreparedDownloadContext prepared = this.prepareDownload(
         getObjectRequest,
         file,
         stateListener,
         s3progressListener,
         resumeExistingDownload,
         timeoutMillis,
         lastFullyDownloadedPart,
         lastModifiedTimeRecordedDuringPause,
         resumeOnRetry,
         lastFullyDownloadedPartPosition
      );
      return this.submitDownload(prepared);
   }

   private PreparedDownloadContext prepareDownload(
      GetObjectRequest getObjectRequest,
      File file,
      TransferStateChangeListener stateListener,
      S3ProgressListener s3progressListener,
      boolean resumeExistingDownload,
      long timeoutMillis,
      Integer lastFullyDownloadedPart,
      long lastModifiedTimeRecordedDuringPause,
      boolean resumeOnRetry,
      Long lastFullyDownloadedPartPosition
   ) {
      this.assertParameterNotNull(getObjectRequest, "A valid GetObjectRequest must be provided to initiate download");
      this.assertParameterNotNull(file, "A valid file must be provided to download into");
      appendSingleObjectUserAgent(getObjectRequest);
      String description = "Downloading from " + getObjectRequest.getBucketName() + "/" + getObjectRequest.getKey();
      TransferProgress transferProgress = new TransferProgress();
      S3ProgressListenerChain listenerChain = new S3ProgressListenerChain(
         new TransferProgressUpdatingListener(transferProgress), getObjectRequest.getGeneralProgressListener(), s3progressListener
      );
      getObjectRequest.setGeneralProgressListener(new ProgressListenerChain(new TransferCompletionFilter(), new ProgressListener[]{listenerChain}));
      GetObjectMetadataRequest getObjectMetadataRequest = RequestCopyUtils.createGetObjectMetadataRequestFrom(getObjectRequest);
      ObjectMetadata objectMetadata = this.s3.getObjectMetadata(getObjectMetadataRequest);
      long lastModifiedTime = objectMetadata.getLastModified().getTime();
      long startingByte = 0L;
      long[] range = getObjectRequest.getRange();
      long lastByte;
      if (range != null && range.length == 2) {
         startingByte = range[0];
         lastByte = range[1];
      } else {
         lastByte = objectMetadata.getContentLength() - 1L;
      }

      long origStartingByte = startingByte;
      boolean isDownloadParallel = !this.configuration.isDisableParallelDownloads()
         && TransferManagerUtils.isDownloadParallelizable(this.s3, getObjectRequest, ServiceUtils.getPartCount(getObjectRequest, this.s3));
      DownloadImpl download = new DownloadImpl(
         description, transferProgress, listenerChain, null, stateListener, getObjectRequest, file, objectMetadata, isDownloadParallel
      );
      long totalBytesToDownload = lastByte - startingByte + 1L;
      transferProgress.setTotalBytesToTransfer(totalBytesToDownload);
      if (totalBytesToDownload > 0L && !isDownloadParallel) {
         getObjectRequest.withRange(startingByte, lastByte);
      }

      long fileLength = -1L;
      if (resumeExistingDownload) {
         if (this.isS3ObjectModifiedSincePause(lastModifiedTime, lastModifiedTimeRecordedDuringPause)) {
            throw new AmazonClientException(
               "The requested object in bucket "
                  + getObjectRequest.getBucketName()
                  + " with key "
                  + getObjectRequest.getKey()
                  + " is modified on Amazon S3 since the last pause."
            );
         }

         getObjectRequest.setUnmodifiedSinceConstraint(new Date(lastModifiedTime));
         if (!isDownloadParallel) {
            if (!FileLocks.lock(file)) {
               throw new FileLockException("Fail to lock " + file + " for resume download");
            }

            try {
               if (file.exists()) {
                  fileLength = file.length();
                  startingByte += fileLength;
                  getObjectRequest.setRange(startingByte, lastByte);
                  transferProgress.updateProgress(Math.min(fileLength, totalBytesToDownload));
                  totalBytesToDownload = lastByte - startingByte + 1L;
                  if (log.isDebugEnabled()) {
                     log.debug(
                        "Resume download: totalBytesToDownload="
                           + totalBytesToDownload
                           + ", origStartingByte="
                           + origStartingByte
                           + ", startingByte="
                           + startingByte
                           + ", lastByte="
                           + lastByte
                           + ", numberOfBytesRead="
                           + fileLength
                           + ", file: "
                           + file
                     );
                  }
               }
            } finally {
               FileLocks.unlock(file);
            }
         }
      }

      if (totalBytesToDownload < 0L) {
         throw new IllegalArgumentException("Unable to determine the range for download operation.");
      } else {
         CountDownLatch latch = new CountDownLatch(1);
         DownloadCallable downloadCallable = new DownloadCallable(
               this.s3,
               latch,
               getObjectRequest,
               resumeExistingDownload,
               download,
               file,
               startingByte,
               fileLength,
               timeoutMillis,
               this.timedThreadPool,
               this.executorService,
               lastFullyDownloadedPart,
               isDownloadParallel,
               resumeOnRetry
            )
            .withLastFullyMergedPartPosition(lastFullyDownloadedPartPosition);
         return new PreparedDownloadContext(download, downloadCallable, latch);
      }
   }

   private DownloadImpl submitDownload(PreparedDownloadContext preparedDownloadContext) {
      Future<File> future = this.executorService.submit(preparedDownloadContext.getCallable());
      DownloadImpl transfer = preparedDownloadContext.getTransfer();
      transfer.setMonitor(new DownloadMonitor(transfer, future));
      preparedDownloadContext.getLatch().countDown();
      return transfer;
   }

   private boolean isS3ObjectModifiedSincePause(long lastModifiedTimeRecordedDuringResume, long lastModifiedTimeRecordedDuringPause) {
      return lastModifiedTimeRecordedDuringResume != lastModifiedTimeRecordedDuringPause;
   }

   public PresignedUrlDownload download(PresignedUrlDownloadRequest request, File destFile) {
      return this.download(request, destFile, new PresignedUrlDownloadConfig());
   }

   public PresignedUrlDownload download(PresignedUrlDownloadRequest request, File destFile, PresignedUrlDownloadConfig downloadContext) {
      this.assertParameterNotNull(request, "A valid PresignedUrlDownloadRequest must be provided to initiate download");
      this.assertParameterNotNull(destFile, "A valid file must be provided to download into");
      this.assertParameterNotNull(downloadContext, "A valid PresignedUrlDownloadContext must be provided");
      assertNotObjectLambdaUrl(request.getPresignedUrl(), "download");
      appendSingleObjectUserAgent(request);
      String description = "Downloading from the given presigned url: " + request.getPresignedUrl();
      TransferProgress transferProgress = new TransferProgress();
      S3ProgressListenerChain listenerChain = new S3ProgressListenerChain(
         new TransferProgressUpdatingListener(transferProgress), request.getGeneralProgressListener(), downloadContext.getS3progressListener()
      );
      request.setGeneralProgressListener(new ProgressListenerChain(new TransferCompletionFilter(), new ProgressListener[]{listenerChain}));
      Long startByte = 0L;
      Long endByte = null;
      long[] range = request.getRange();
      if (range != null && range.length == 2) {
         startByte = range[0];
         endByte = range[1];
      } else {
         ObjectMetadata objectMetadata = this.getObjectMetadataUsingRange(request);
         if (objectMetadata != null) {
            Long contentLength = TransferManagerUtils.getContentLengthFromContentRange(objectMetadata);
            endByte = contentLength != null ? contentLength - 1L : null;
         }
      }

      long perRequestDownloadSize = downloadContext.getDownloadSizePerRequest();
      boolean isDownloadParallel = this.isDownloadParallel(request, startByte, endByte, perRequestDownloadSize);
      PresignedUrlDownloadImpl download = new PresignedUrlDownloadImpl(description, transferProgress, listenerChain, request);
      if (startByte != null && endByte != null) {
         transferProgress.setTotalBytesToTransfer(endByte - startByte + 1L);
      }

      CountDownLatch latch = new CountDownLatch(1);
      Future<File> future = this.executorService
         .submit(
            new PresignUrlDownloadCallable(
               this.executorService,
               destFile,
               latch,
               download,
               isDownloadParallel,
               this.timedThreadPool,
               downloadContext.getTimeoutMillis(),
               this.s3,
               request,
               perRequestDownloadSize,
               startByte,
               endByte,
               downloadContext.isResumeOnRetry()
            )
         );
      download.setMonitor(new DownloadMonitor(download, future));
      latch.countDown();
      return download;
   }

   private ObjectMetadata getObjectMetadataUsingRange(PresignedUrlDownloadRequest request) {
      PresignedUrlDownloadRequest copy = request.clone();
      S3Object s3Object = null;

      Object objectContent;
      try {
         s3Object = this.s3.download(copy.withRange(0L, 0L)).getS3Object();
         return s3Object.getObjectMetadata();
      } catch (AmazonS3Exception var11) {
         if (var11.getStatusCode() != 416 || !"InvalidRange".equals(var11.getErrorCode())) {
            throw var11;
         }

         objectContent = null;
      } finally {
         if (s3Object != null) {
            S3ObjectInputStream objectContent = s3Object.getObjectContent();
            IOUtils.drainInputStream(objectContent);
            IOUtils.closeQuietly(objectContent, log);
         }
      }

      return (ObjectMetadata)objectContent;
   }

   private boolean isDownloadParallel(PresignedUrlDownloadRequest request, Long startByte, Long endByte, long partialObjectMaxSize) {
      return !this.configuration.isDisableParallelDownloads()
         && !(this.s3 instanceof AmazonS3Encryption)
         && !(this.s3 instanceof AmazonS3EncryptionV2)
         && request.getRange() == null
         && startByte != null
         && endByte != null
         && endByte - startByte + 1L > partialObjectMaxSize;
   }

   public MultipleFileDownload downloadDirectory(String bucketName, String keyPrefix, File destinationDirectory) {
      return this.downloadDirectory(bucketName, keyPrefix, destinationDirectory, false);
   }

   public MultipleFileDownload downloadDirectory(String bucketName, String keyPrefix, File destinationDirectory, KeyFilter filter) {
      return this.downloadDirectory(bucketName, keyPrefix, destinationDirectory, false, filter);
   }

   public MultipleFileDownload downloadDirectory(String bucketName, String keyPrefix, File destinationDirectory, boolean resumeOnRetry) {
      return this.downloadDirectory(bucketName, keyPrefix, destinationDirectory, resumeOnRetry, null);
   }

   public MultipleFileDownload downloadDirectory(String bucketName, String keyPrefix, File destinationDirectory, boolean resumeOnRetry, KeyFilter filter) {
      assertNotObjectLambdaArn(bucketName, "downloadDirectory");
      if (keyPrefix == null) {
         keyPrefix = "";
      }

      if (filter == null) {
         filter = KeyFilter.INCLUDE_ALL;
      }

      List<S3ObjectSummary> objectSummaries = new LinkedList<>();
      Stack<String> commonPrefixes = new Stack<>();
      commonPrefixes.add(keyPrefix);
      long totalSize = 0L;

      do {
         String prefix = commonPrefixes.pop();
         ObjectListing listObjectsResponse = null;

         do {
            if (listObjectsResponse == null) {
               ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withDelimiter("/").withPrefix(prefix);
               listObjectsResponse = this.s3.listObjects(listObjectsRequest);
            } else {
               listObjectsResponse = this.s3.listNextBatchOfObjects(listObjectsResponse);
            }

            for(S3ObjectSummary s : listObjectsResponse.getObjectSummaries()) {
               if (!filter.shouldInclude(s)) {
                  log.debug("Skipping " + s.getKey() + " as it does not match filter.");
               } else {
                  if (this.leavesRoot(destinationDirectory, s.getKey())) {
                     throw new RuntimeException("Cannot download key " + s.getKey() + ", its relative path resolves outside the parent directory.");
                  }

                  if (!s.getKey().equals(prefix) && !listObjectsResponse.getCommonPrefixes().contains(s.getKey() + "/")) {
                     objectSummaries.add(s);
                     totalSize += s.getSize();
                  } else {
                     log.debug("Skipping download for object " + s.getKey() + " since it is also a virtual directory");
                  }
               }
            }

            commonPrefixes.addAll(listObjectsResponse.getCommonPrefixes());
         } while(listObjectsResponse.isTruncated());
      } while(!commonPrefixes.isEmpty());

      ProgressListenerChain additionalListeners = new ProgressListenerChain(new ProgressListener[0]);
      TransferProgress transferProgress = new TransferProgress();
      transferProgress.setTotalBytesToTransfer(totalSize);
      ProgressListener listener = new MultipleFileTransferProgressUpdatingListener(transferProgress, additionalListeners);
      List<DownloadImpl> downloads = new ArrayList<>();
      List<PreparedDownloadContext> preparedDownloadContexts = new ArrayList<>();
      String description = "Downloading from " + bucketName + "/" + keyPrefix;
      MultipleFileDownloadImpl multipleFileDownload = new MultipleFileDownloadImpl(
         description, transferProgress, additionalListeners, keyPrefix, bucketName, downloads
      );
      multipleFileDownload.setMonitor(new MultipleFileTransferMonitor(multipleFileDownload, downloads));
      CountDownLatch latch = new CountDownLatch(1);
      MultipleFileTransferStateChangeListener transferListener = new MultipleFileTransferStateChangeListener(latch, multipleFileDownload);
      if (objectSummaries.isEmpty()) {
         multipleFileDownload.setState(Transfer.TransferState.Completed);
         return multipleFileDownload;
      } else {
         for(S3ObjectSummary summary : objectSummaries) {
            File f = new File(destinationDirectory, summary.getKey());
            File parentFile = f.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()) {
               throw new RuntimeException("Couldn't create parent directories for " + f.getAbsolutePath());
            }

            GetObjectRequest req = (GetObjectRequest)new GetObjectRequest(summary.getBucketName(), summary.getKey()).withGeneralProgressListener(listener);
            PreparedDownloadContext ctx = this.prepareDownload(req, f, transferListener, null, false, 0L, null, 0L, resumeOnRetry, null);
            preparedDownloadContexts.add(ctx);
         }

         try {
            for(PreparedDownloadContext ctx : preparedDownloadContexts) {
               downloads.add(this.submitDownload(ctx));
            }
         } catch (Throwable var26) {
            for(DownloadImpl d : downloads) {
               try {
                  d.getMonitor().getFuture().cancel(true);
               } catch (Throwable var25) {
                  log.warn("DownloadImpl could not be aborted", var25);
               }
            }

            throw new SdkClientException(var26);
         }

         latch.countDown();
         return multipleFileDownload;
      }
   }

   private boolean leavesRoot(File localBaseDirectory, String key) {
      try {
         Path targetPath = new File(localBaseDirectory, key).getCanonicalFile().toPath();
         Path rootPath = localBaseDirectory.getCanonicalFile().toPath();
         return !targetPath.startsWith(rootPath);
      } catch (IOException var5) {
         throw new RuntimeException("Unable to canonicalize paths", var5);
      }
   }

   public MultipleFileUpload uploadDirectory(String bucketName, String virtualDirectoryKeyPrefix, File directory, boolean includeSubdirectories) {
      return this.uploadDirectory(bucketName, virtualDirectoryKeyPrefix, directory, includeSubdirectories, null);
   }

   public MultipleFileUpload uploadDirectory(
      String bucketName, String virtualDirectoryKeyPrefix, File directory, boolean includeSubdirectories, ObjectMetadataProvider metadataProvider
   ) {
      return this.uploadDirectory(bucketName, virtualDirectoryKeyPrefix, directory, includeSubdirectories, metadataProvider, null);
   }

   public MultipleFileUpload uploadDirectory(
      String bucketName,
      String virtualDirectoryKeyPrefix,
      File directory,
      boolean includeSubdirectories,
      ObjectMetadataProvider metadataProvider,
      ObjectTaggingProvider taggingProvider
   ) {
      return this.uploadDirectory(bucketName, virtualDirectoryKeyPrefix, directory, includeSubdirectories, metadataProvider, taggingProvider, null);
   }

   public MultipleFileUpload uploadDirectory(
      String bucketName,
      String virtualDirectoryKeyPrefix,
      File directory,
      boolean includeSubdirectories,
      ObjectMetadataProvider metadataProvider,
      ObjectTaggingProvider taggingProvider,
      ObjectCannedAclProvider cannedAclProvider
   ) {
      if (directory != null && directory.exists() && directory.isDirectory()) {
         assertNotObjectLambdaArn(bucketName, "uploadDirectory");
         List<File> files = new LinkedList<>();
         this.listFiles(directory, files, includeSubdirectories);
         return this.uploadFileList(bucketName, virtualDirectoryKeyPrefix, directory, files, metadataProvider, taggingProvider, cannedAclProvider);
      } else {
         throw new IllegalArgumentException("Must provide a directory to upload");
      }
   }

   public MultipleFileUpload uploadFileList(String bucketName, String virtualDirectoryKeyPrefix, File directory, List<File> files) {
      return this.uploadFileList(bucketName, virtualDirectoryKeyPrefix, directory, files, null);
   }

   public MultipleFileUpload uploadFileList(
      String bucketName, String virtualDirectoryKeyPrefix, File directory, List<File> files, ObjectMetadataProvider metadataProvider
   ) {
      return this.uploadFileList(bucketName, virtualDirectoryKeyPrefix, directory, files, metadataProvider, null);
   }

   public MultipleFileUpload uploadFileList(
      String bucketName,
      String virtualDirectoryKeyPrefix,
      File directory,
      List<File> files,
      ObjectMetadataProvider metadataProvider,
      ObjectTaggingProvider taggingProvider
   ) {
      return this.uploadFileList(bucketName, virtualDirectoryKeyPrefix, directory, files, metadataProvider, taggingProvider, null);
   }

   public MultipleFileUpload uploadFileList(
      String bucketName,
      String virtualDirectoryKeyPrefix,
      File directory,
      List<File> files,
      ObjectMetadataProvider metadataProvider,
      ObjectTaggingProvider taggingProvider,
      ObjectCannedAclProvider cannedAclProvider
   ) {
      if (directory != null && directory.exists() && directory.isDirectory()) {
         assertNotObjectLambdaArn(bucketName, "uploadFileList");
         if (virtualDirectoryKeyPrefix == null || virtualDirectoryKeyPrefix.length() == 0) {
            virtualDirectoryKeyPrefix = "";
         } else if (!virtualDirectoryKeyPrefix.endsWith("/")) {
            virtualDirectoryKeyPrefix = virtualDirectoryKeyPrefix + "/";
         }

         ProgressListenerChain additionalListeners = new ProgressListenerChain(new ProgressListener[0]);
         TransferProgress progress = new TransferProgress();
         ProgressListener listener = new MultipleFileTransferProgressUpdatingListener(progress, additionalListeners);
         List<UploadImpl> uploads = new LinkedList<>();
         MultipleFileUploadImpl multipleFileUpload = new MultipleFileUploadImpl(
            "Uploading etc", progress, additionalListeners, virtualDirectoryKeyPrefix, bucketName, uploads
         );
         multipleFileUpload.setMonitor(new MultipleFileTransferMonitor(multipleFileUpload, uploads));
         CountDownLatch latch = new CountDownLatch(1);
         MultipleFileTransferStateChangeListener transferListener = new MultipleFileTransferStateChangeListener(latch, multipleFileUpload);
         if (files != null && !files.isEmpty()) {
            int startingPosition = directory.getAbsolutePath().length();
            if (!directory.getAbsolutePath().endsWith(File.separator)) {
               ++startingPosition;
            }

            long totalSize = 0L;

            for(File f : files) {
               if (f.isFile()) {
                  totalSize += f.length();
                  String key = f.getAbsolutePath().substring(startingPosition).replaceAll("\\\\", "/");
                  UploadContext uploadContext = new UploadContext(f, bucketName, key);
                  ObjectMetadata metadata = new ObjectMetadata();
                  CannedAccessControlList cannedAcl = null;
                  ObjectTagging objectTagging = null;
                  if (metadataProvider != null) {
                     metadataProvider.provideObjectMetadata(f, metadata);
                  }

                  if (taggingProvider != null) {
                     objectTagging = taggingProvider.provideObjectTags(uploadContext);
                  }

                  if (cannedAclProvider != null) {
                     cannedAcl = cannedAclProvider.provideObjectCannedAcl(f);
                  }

                  uploads.add(
                     (UploadImpl)this.doUpload(
                        (PutObjectRequest)new PutObjectRequest(bucketName, virtualDirectoryKeyPrefix + key, f)
                           .withMetadata(metadata)
                           .withTagging(objectTagging)
                           .withCannedAcl(cannedAcl)
                           .withGeneralProgressListener(listener),
                        transferListener,
                        null,
                        null
                     )
                  );
               }
            }

            progress.setTotalBytesToTransfer(totalSize);
         } else {
            multipleFileUpload.setState(Transfer.TransferState.Completed);
         }

         latch.countDown();
         return multipleFileUpload;
      } else {
         throw new IllegalArgumentException("Must provide a common base directory for uploaded files");
      }
   }

   private void listFiles(File dir, List<File> results, boolean includeSubDirectories) {
      File[] found = dir.listFiles();
      if (found != null) {
         for(File f : found) {
            if (f.isDirectory()) {
               if (includeSubDirectories) {
                  this.listFiles(f, results, includeSubDirectories);
               }
            } else {
               results.add(f);
            }
         }
      }
   }

   public void abortMultipartUploads(String bucketName, Date date) throws AmazonServiceException, AmazonClientException {
      assertNotObjectLambdaArn(bucketName, "abortMultipartUploads");
      MultipartUploadListing uploadListing = this.s3.listMultipartUploads(appendSingleObjectUserAgent(new ListMultipartUploadsRequest(bucketName)));

      do {
         for(MultipartUpload upload : uploadListing.getMultipartUploads()) {
            if (upload.getInitiated().compareTo(date) < 0) {
               this.s3.abortMultipartUpload(appendSingleObjectUserAgent(new AbortMultipartUploadRequest(bucketName, upload.getKey(), upload.getUploadId())));
            }
         }

         ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(bucketName)
            .withUploadIdMarker(uploadListing.getNextUploadIdMarker())
            .withKeyMarker(uploadListing.getNextKeyMarker());
         uploadListing = this.s3.listMultipartUploads(appendSingleObjectUserAgent(request));
      } while(uploadListing.isTruncated());
   }

   public void shutdownNow() {
      this.shutdownNow(true);
   }

   public void shutdownNow(boolean shutDownS3Client) {
      if (this.shutDownThreadPools) {
         this.executorService.shutdownNow();
         this.timedThreadPool.shutdownNow();
      }

      if (shutDownS3Client) {
         this.s3.shutdown();
      }
   }

   private void shutdownThreadPools() {
      if (this.shutDownThreadPools) {
         this.executorService.shutdown();
         this.timedThreadPool.shutdown();
      }
   }

   public static <X extends AmazonWebServiceRequest> X appendSingleObjectUserAgent(X request) {
      request.getRequestClientOptions().appendUserAgent(USER_AGENT);
      return request;
   }

   public static <X extends AmazonWebServiceRequest> X appendMultipartUserAgent(X request) {
      request.getRequestClientOptions().appendUserAgent(USER_AGENT_MULTIPART);
      return request;
   }

   public Copy copy(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) throws AmazonServiceException, AmazonClientException {
      return this.copy(new CopyObjectRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey));
   }

   public Copy copy(CopyObjectRequest copyObjectRequest) {
      return this.copy(copyObjectRequest, null);
   }

   public Copy copy(CopyObjectRequest copyObjectRequest, TransferStateChangeListener stateChangeListener) throws AmazonServiceException, AmazonClientException {
      return this.copy(copyObjectRequest, this.s3, stateChangeListener);
   }

   public Copy copy(CopyObjectRequest copyObjectRequest, AmazonS3 srcS3, TransferStateChangeListener stateChangeListener) throws AmazonServiceException, AmazonClientException {
      assertNotObjectLambdaArn(copyObjectRequest.getDestinationBucketName(), "copy");
      assertNotObjectLambdaArn(copyObjectRequest.getSourceBucketName(), "copy");
      appendSingleObjectUserAgent(copyObjectRequest);
      this.assertParameterNotNull(copyObjectRequest.getSourceBucketName(), "The source bucket name must be specified when a copy request is initiated.");
      this.assertParameterNotNull(copyObjectRequest.getSourceKey(), "The source object key must be specified when a copy request is initiated.");
      this.assertParameterNotNull(
         copyObjectRequest.getDestinationBucketName(), "The destination bucket name must be specified when a copy request is initiated."
      );
      this.assertParameterNotNull(copyObjectRequest.getDestinationKey(), "The destination object key must be specified when a copy request is initiated.");
      this.assertParameterNotNull(srcS3, "The srcS3 parameter is mandatory");
      String description = "Copying object from "
         + copyObjectRequest.getSourceBucketName()
         + "/"
         + copyObjectRequest.getSourceKey()
         + " to "
         + copyObjectRequest.getDestinationBucketName()
         + "/"
         + copyObjectRequest.getDestinationKey();
      GetObjectMetadataRequest getObjectMetadataRequest = (GetObjectMetadataRequest)new GetObjectMetadataRequest(
            copyObjectRequest.getSourceBucketName(), copyObjectRequest.getSourceKey()
         )
         .withSSECustomerKey(copyObjectRequest.getSourceSSECustomerKey())
         .withRequesterPays(copyObjectRequest.isRequesterPays())
         .withVersionId(copyObjectRequest.getSourceVersionId())
         .withRequestCredentialsProvider(copyObjectRequest.getRequestCredentialsProvider());
      ObjectMetadata metadata = srcS3.getObjectMetadata(getObjectMetadataRequest);
      TransferProgress transferProgress = new TransferProgress();
      transferProgress.setTotalBytesToTransfer(metadata.getContentLength());
      ProgressListenerChain listenerChain = new ProgressListenerChain(new ProgressListener[]{new TransferProgressUpdatingListener(transferProgress)});
      CopyImpl copy = new CopyImpl(description, transferProgress, listenerChain, stateChangeListener);
      CopyCallable copyCallable = new CopyCallable(this, this.executorService, copy, copyObjectRequest, metadata, listenerChain);
      CopyMonitor watcher = CopyMonitor.create(this, copy, this.executorService, copyCallable, copyObjectRequest, listenerChain);
      copy.setMonitor(watcher);
      return copy;
   }

   public Upload resumeUpload(PersistableUpload persistableUpload) {
      this.assertParameterNotNull(persistableUpload, "PauseUpload is mandatory to resume a upload.");
      assertNotObjectLambdaArn(persistableUpload.getBucketName(), "resumeUpload");
      this.configuration.setMinimumUploadPartSize(persistableUpload.getPartSize());
      this.configuration.setMultipartUploadThreshold(persistableUpload.getMutlipartUploadThreshold());
      return this.doUpload(
         new PutObjectRequest(persistableUpload.getBucketName(), persistableUpload.getKey(), new File(persistableUpload.getFile())),
         null,
         null,
         persistableUpload
      );
   }

   public Download resumeDownload(PersistableDownload persistableDownload) {
      this.assertParameterNotNull(persistableDownload, "PausedDownload is mandatory to resume a download.");
      assertNotObjectLambdaArn(persistableDownload.getBucketName(), "resumeDownload");
      GetObjectRequest request = new GetObjectRequest(persistableDownload.getBucketName(), persistableDownload.getKey(), persistableDownload.getVersionId());
      if (persistableDownload.getRange() != null && persistableDownload.getRange().length == 2) {
         long[] range = persistableDownload.getRange();
         request.setRange(range[0], range[1]);
      }

      request.setRequesterPays(persistableDownload.isRequesterPays());
      request.setResponseHeaders(persistableDownload.getResponseHeaders());
      return this.doDownload(request, new File(persistableDownload.getFile()), null, null, true, 0L, persistableDownload);
   }

   private void assertParameterNotNull(Object parameterValue, String errorMessage) {
      if (parameterValue == null) {
         throw new IllegalArgumentException(errorMessage);
      }
   }

   @Override
   protected void finalize() throws Throwable {
      this.shutdownThreadPools();
   }

   private void checkMutability() {
      if (this.isImmutable) {
         throw new UnsupportedOperationException("TransferManager is immutable when created with the builder.");
      }
   }

   private static void assertNotObjectLambdaArn(String arn, String operation) {
      if (isObjectLambdaArn(arn)) {
         String error = String.format("%s does not support S3 Object Lambda resources", operation);
         throw new IllegalArgumentException(error);
      }
   }

   private static void assertNotObjectLambdaUrl(URL url, String operation) {
      if (isObjectLambdaHost(url)) {
         String error = String.format("%s does not support S3 Object Lambda resources", operation);
         throw new IllegalArgumentException(error);
      }
   }

   private static boolean isObjectLambdaArn(String arn) {
      if (arn == null) {
         return false;
      } else {
         return arn.startsWith("arn:") && arn.contains(":s3-object-lambda");
      }
   }

   private static boolean isObjectLambdaHost(URL url) {
      String host = url.getHost();
      return host == null ? false : host.contains(".s3-object-lambda.");
   }
}
