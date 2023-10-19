package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.PauseStatus;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.util.ValidationUtils;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransferManagerUtils {
   private static final Log log = LogFactory.getLog(TransferManagerUtils.class);

   public static ThreadPoolExecutor createDefaultExecutorService() {
      ThreadFactory threadFactory = new ThreadFactory() {
         private int threadCount = 1;

         @Override
         public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("s3-transfer-manager-worker-" + this.threadCount++);
            return thread;
         }
      };
      return (ThreadPoolExecutor)Executors.newFixedThreadPool(10, threadFactory);
   }

   public static boolean isUploadParallelizable(PutObjectRequest putObjectRequest, boolean isUsingEncryption) {
      if (isUsingEncryption) {
         return false;
      } else {
         return getRequestFile(putObjectRequest) != null;
      }
   }

   public static long getContentLength(PutObjectRequest putObjectRequest) {
      File file = getRequestFile(putObjectRequest);
      if (file != null) {
         return file.length();
      } else {
         return putObjectRequest.getInputStream() != null && putObjectRequest.getMetadata().getContentLength() > 0L
            ? putObjectRequest.getMetadata().getContentLength()
            : -1L;
      }
   }

   public static long calculateOptimalPartSize(PutObjectRequest putObjectRequest, TransferManagerConfiguration configuration) {
      double contentLength = (double)getContentLength(putObjectRequest);
      double optimalPartSize = contentLength / 10000.0;
      optimalPartSize = Math.ceil(optimalPartSize);
      return (long)Math.max(optimalPartSize, (double)configuration.getMinimumUploadPartSize());
   }

   public static boolean shouldUseMultipartUpload(PutObjectRequest putObjectRequest, TransferManagerConfiguration configuration) {
      long contentLength = getContentLength(putObjectRequest);
      return contentLength > configuration.getMultipartUploadThreshold();
   }

   public static File getRequestFile(PutObjectRequest putObjectRequest) {
      return putObjectRequest.getFile() != null ? putObjectRequest.getFile() : null;
   }

   public static long calculateOptimalPartSizeForCopy(
      CopyObjectRequest copyObjectRequest, TransferManagerConfiguration configuration, long contentLengthOfSource
   ) {
      double optimalPartSize = (double)contentLengthOfSource / 10000.0;
      optimalPartSize = Math.ceil(optimalPartSize);
      return (long)Math.max(optimalPartSize, (double)configuration.getMultipartCopyPartSize());
   }

   public static PauseStatus determinePauseStatus(Transfer.TransferState transferState, boolean forceCancel) {
      if (forceCancel) {
         if (transferState == Transfer.TransferState.Waiting) {
            return PauseStatus.CANCELLED_BEFORE_START;
         }

         if (transferState == Transfer.TransferState.InProgress) {
            return PauseStatus.CANCELLED;
         }
      }

      return transferState == Transfer.TransferState.Waiting ? PauseStatus.NOT_STARTED : PauseStatus.NO_EFFECT;
   }

   public static boolean isDownloadParallelizable(AmazonS3 s3, GetObjectRequest getObjectRequest, Integer partCount) {
      ValidationUtils.assertNotNull(s3, "S3 client");
      ValidationUtils.assertNotNull(getObjectRequest, "GetObjectRequest");
      return !(s3 instanceof AmazonS3Encryption)
         && !(s3 instanceof AmazonS3EncryptionV2)
         && getObjectRequest.getRange() == null
         && getObjectRequest.getPartNumber() == null
         && partCount != null;
   }

   public static Long getContentLengthFromContentRange(ObjectMetadata metadata) {
      ValidationUtils.assertNotNull(metadata, "Object metadata");
      String contentRange = (String)metadata.getRawMetadataValue("Content-Range");
      if (contentRange != null) {
         try {
            String[] tokens = contentRange.split("[ -/]+");
            return Long.parseLong(tokens[3]);
         } catch (Exception var3) {
            log.info(String.format("Error parsing 'Content-Range' header value: %s. So returning null value for content length", contentRange), var3);
         }
      }

      return null;
   }
}
