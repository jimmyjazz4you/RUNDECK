package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.internal.ReleasableInputStream;
import com.amazonaws.services.s3.internal.InputSubstream;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SSECustomerKey;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import java.io.File;

public class UploadPartRequestFactory {
   private final String bucketName;
   private final String key;
   private final String uploadId;
   private final long optimalPartSize;
   private final File file;
   private final PutObjectRequest origReq;
   private int partNumber = 1;
   private long offset = 0L;
   private long remainingBytes;
   private SSECustomerKey sseCustomerKey;
   private final int totalNumberOfParts;
   private ReleasableInputStream wrappedStream;

   public UploadPartRequestFactory(PutObjectRequest origReq, String uploadId, long optimalPartSize) {
      this.origReq = origReq;
      this.uploadId = uploadId;
      this.optimalPartSize = optimalPartSize;
      this.bucketName = origReq.getBucketName();
      this.key = origReq.getKey();
      this.file = TransferManagerUtils.getRequestFile(origReq);
      this.remainingBytes = TransferManagerUtils.getContentLength(origReq);
      this.sseCustomerKey = origReq.getSSECustomerKey();
      this.totalNumberOfParts = (int)Math.ceil((double)this.remainingBytes / (double)this.optimalPartSize);
      if (origReq.getInputStream() != null) {
         this.wrappedStream = ReleasableInputStream.wrap(origReq.getInputStream());
      }
   }

   public synchronized boolean hasMoreRequests() {
      return this.remainingBytes > 0L;
   }

   public synchronized UploadPartRequest getNextUploadPartRequest() {
      long partSize = Math.min(this.optimalPartSize, this.remainingBytes);
      boolean isLastPart = this.remainingBytes - partSize <= 0L;
      UploadPartRequest req = null;
      if (this.wrappedStream != null) {
         req = new UploadPartRequest()
            .withBucketName(this.bucketName)
            .withKey(this.key)
            .withUploadId(this.uploadId)
            .withInputStream(new InputSubstream(this.wrappedStream, 0L, partSize, isLastPart))
            .withPartNumber(this.partNumber++)
            .withPartSize(partSize);
      } else {
         req = new UploadPartRequest()
            .withBucketName(this.bucketName)
            .withKey(this.key)
            .withUploadId(this.uploadId)
            .withFile(this.file)
            .withFileOffset(this.offset)
            .withPartNumber(this.partNumber++)
            .withPartSize(partSize);
      }

      ObjectMetadata origReqMetadata = this.origReq.getMetadata();
      if (origReqMetadata != null
         && origReqMetadata.getRawMetadataValue("x-amz-server-side-encryption-customer-key") != null
         && origReqMetadata.getSSECustomerAlgorithm() != null
         && origReqMetadata.getSSECustomerKeyMd5() != null) {
         ObjectMetadata metadata = new ObjectMetadata();
         metadata.setHeader("x-amz-server-side-encryption-customer-key", origReqMetadata.getRawMetadataValue("x-amz-server-side-encryption-customer-key"));
         metadata.setSSECustomerAlgorithm(origReqMetadata.getSSECustomerAlgorithm());
         metadata.setSSECustomerKeyMd5(origReqMetadata.getSSECustomerKeyMd5());
         req.withObjectMetadata(metadata);
      }

      req.withRequesterPays(this.origReq.isRequesterPays());
      TransferManager.appendMultipartUserAgent(req);
      if (this.sseCustomerKey != null) {
         req.setSSECustomerKey(this.sseCustomerKey);
      }

      this.offset += partSize;
      this.remainingBytes -= partSize;
      req.setLastPart(isLastPart);
      req.withGeneralProgressListener(this.origReq.getGeneralProgressListener()).withRequestMetricCollector(this.origReq.getRequestMetricCollector());
      req.getRequestClientOptions().setReadLimit(this.origReq.getReadLimit());
      req.withRequestCredentialsProvider(this.origReq.getRequestCredentialsProvider());
      return req;
   }

   public int getTotalNumberOfParts() {
      return this.totalNumberOfParts;
   }
}
