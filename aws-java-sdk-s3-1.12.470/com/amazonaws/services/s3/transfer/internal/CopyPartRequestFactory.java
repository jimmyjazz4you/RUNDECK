package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyPartRequest;

public class CopyPartRequestFactory {
   private final String uploadId;
   private final long optimalPartSize;
   private final CopyObjectRequest origReq;
   private int partNumber = 1;
   private long offset = 0L;
   private long remainingBytes;

   public CopyPartRequestFactory(CopyObjectRequest origReq, String uploadId, long optimalPartSize, long contentLength) {
      this.origReq = origReq;
      this.uploadId = uploadId;
      this.optimalPartSize = optimalPartSize;
      this.remainingBytes = contentLength;
   }

   public synchronized boolean hasMoreRequests() {
      return this.remainingBytes > 0L;
   }

   public synchronized CopyPartRequest getNextCopyPartRequest() {
      long partSize = Math.min(this.optimalPartSize, this.remainingBytes);
      CopyPartRequest req = (CopyPartRequest)new CopyPartRequest()
         .withSourceBucketName(this.origReq.getSourceBucketName())
         .withSourceKey(this.origReq.getSourceKey())
         .withUploadId(this.uploadId)
         .withPartNumber(this.partNumber++)
         .withDestinationBucketName(this.origReq.getDestinationBucketName())
         .withDestinationKey(this.origReq.getDestinationKey())
         .withSourceVersionId(this.origReq.getSourceVersionId())
         .withFirstByte(this.offset)
         .withLastByte(this.offset + partSize - 1L)
         .withSourceSSECustomerKey(this.origReq.getSourceSSECustomerKey())
         .withDestinationSSECustomerKey(this.origReq.getDestinationSSECustomerKey())
         .withRequesterPays(this.origReq.isRequesterPays())
         .withMatchingETagConstraints(this.origReq.getMatchingETagConstraints())
         .withModifiedSinceConstraint(this.origReq.getModifiedSinceConstraint())
         .withNonmatchingETagConstraints(this.origReq.getNonmatchingETagConstraints())
         .withSourceVersionId(this.origReq.getSourceVersionId())
         .withUnmodifiedSinceConstraint(this.origReq.getUnmodifiedSinceConstraint())
         .withGeneralProgressListener(this.origReq.getGeneralProgressListener())
         .withRequestMetricCollector(this.origReq.getRequestMetricCollector())
         .withRequestCredentialsProvider(this.origReq.getRequestCredentialsProvider());
      this.offset += partSize;
      this.remainingBytes -= partSize;
      return req;
   }
}
