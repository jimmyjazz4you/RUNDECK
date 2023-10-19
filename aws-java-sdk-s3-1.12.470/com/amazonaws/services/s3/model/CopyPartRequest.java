package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CopyPartRequest
   extends AmazonWebServiceRequest
   implements Serializable,
   S3AccelerateUnsupported,
   ExpectedBucketOwnerRequest,
   ExpectedSourceBucketOwnerRequest {
   private String uploadId;
   private int partNumber;
   private String sourceBucketName;
   private String sourceKey;
   private String sourceVersionId;
   private String destinationBucketName;
   private String destinationKey;
   private final List<String> matchingETagConstraints = new ArrayList<>();
   private final List<String> nonmatchingEtagConstraints = new ArrayList<>();
   private Date unmodifiedSinceConstraint;
   private Date modifiedSinceConstraint;
   private Long firstByte;
   private Long lastByte;
   private SSECustomerKey sourceSSECustomerKey;
   private SSECustomerKey destinationSSECustomerKey;
   private boolean isRequesterPays;
   private String expectedBucketOwner;
   private String expectedSourceBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public CopyPartRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   @Override
   public String getExpectedSourceBucketOwner() {
      return this.expectedSourceBucketOwner;
   }

   public CopyPartRequest withExpectedSourceBucketOwner(String expectedSourceBucketOwner) {
      this.expectedSourceBucketOwner = expectedSourceBucketOwner;
      return this;
   }

   @Override
   public void setExpectedSourceBucketOwner(String expectedSourceBucketOwner) {
      this.withExpectedSourceBucketOwner(expectedSourceBucketOwner);
   }

   public String getUploadId() {
      return this.uploadId;
   }

   public void setUploadId(String uploadId) {
      this.uploadId = uploadId;
   }

   public CopyPartRequest withUploadId(String uploadId) {
      this.uploadId = uploadId;
      return this;
   }

   public int getPartNumber() {
      return this.partNumber;
   }

   public void setPartNumber(int partNumber) {
      this.partNumber = partNumber;
   }

   public CopyPartRequest withPartNumber(int partNumber) {
      this.partNumber = partNumber;
      return this;
   }

   public String getSourceBucketName() {
      return this.sourceBucketName;
   }

   public void setSourceBucketName(String sourceBucketName) {
      this.sourceBucketName = sourceBucketName;
   }

   public CopyPartRequest withSourceBucketName(String sourceBucketName) {
      this.sourceBucketName = sourceBucketName;
      return this;
   }

   public String getSourceKey() {
      return this.sourceKey;
   }

   public void setSourceKey(String sourceKey) {
      this.sourceKey = sourceKey;
   }

   public CopyPartRequest withSourceKey(String sourceKey) {
      this.sourceKey = sourceKey;
      return this;
   }

   public String getSourceVersionId() {
      return this.sourceVersionId;
   }

   public void setSourceVersionId(String sourceVersionId) {
      this.sourceVersionId = sourceVersionId;
   }

   public CopyPartRequest withSourceVersionId(String sourceVersionId) {
      this.sourceVersionId = sourceVersionId;
      return this;
   }

   public String getDestinationBucketName() {
      return this.destinationBucketName;
   }

   public void setDestinationBucketName(String destinationBucketName) {
      this.destinationBucketName = destinationBucketName;
   }

   public CopyPartRequest withDestinationBucketName(String destinationBucketName) {
      this.setDestinationBucketName(destinationBucketName);
      return this;
   }

   public String getDestinationKey() {
      return this.destinationKey;
   }

   public void setDestinationKey(String destinationKey) {
      this.destinationKey = destinationKey;
   }

   public CopyPartRequest withDestinationKey(String destinationKey) {
      this.setDestinationKey(destinationKey);
      return this;
   }

   public Long getFirstByte() {
      return this.firstByte;
   }

   public void setFirstByte(Long firstByte) {
      this.firstByte = firstByte;
   }

   public CopyPartRequest withFirstByte(Long firstByte) {
      this.firstByte = firstByte;
      return this;
   }

   public Long getLastByte() {
      return this.lastByte;
   }

   public void setLastByte(Long lastByte) {
      this.lastByte = lastByte;
   }

   public CopyPartRequest withLastByte(Long lastByte) {
      this.lastByte = lastByte;
      return this;
   }

   public List<String> getMatchingETagConstraints() {
      return this.matchingETagConstraints;
   }

   public void setMatchingETagConstraints(List<String> eTagList) {
      this.matchingETagConstraints.clear();
      this.matchingETagConstraints.addAll(eTagList);
   }

   public CopyPartRequest withMatchingETagConstraints(List<String> eTagList) {
      this.setMatchingETagConstraints(eTagList);
      return this;
   }

   public CopyPartRequest withMatchingETagConstraint(String eTag) {
      this.matchingETagConstraints.add(eTag);
      return this;
   }

   public List<String> getNonmatchingETagConstraints() {
      return this.nonmatchingEtagConstraints;
   }

   public void setNonmatchingETagConstraints(List<String> eTagList) {
      this.nonmatchingEtagConstraints.clear();
      this.nonmatchingEtagConstraints.addAll(eTagList);
   }

   public CopyPartRequest withNonmatchingETagConstraints(List<String> eTagList) {
      this.setNonmatchingETagConstraints(eTagList);
      return this;
   }

   public CopyPartRequest withNonmatchingETagConstraint(String eTag) {
      this.nonmatchingEtagConstraints.add(eTag);
      return this;
   }

   public Date getUnmodifiedSinceConstraint() {
      return this.unmodifiedSinceConstraint;
   }

   public void setUnmodifiedSinceConstraint(Date date) {
      this.unmodifiedSinceConstraint = date;
   }

   public CopyPartRequest withUnmodifiedSinceConstraint(Date date) {
      this.setUnmodifiedSinceConstraint(date);
      return this;
   }

   public Date getModifiedSinceConstraint() {
      return this.modifiedSinceConstraint;
   }

   public void setModifiedSinceConstraint(Date date) {
      this.modifiedSinceConstraint = date;
   }

   public CopyPartRequest withModifiedSinceConstraint(Date date) {
      this.setModifiedSinceConstraint(date);
      return this;
   }

   public SSECustomerKey getSourceSSECustomerKey() {
      return this.sourceSSECustomerKey;
   }

   public void setSourceSSECustomerKey(SSECustomerKey sseKey) {
      this.sourceSSECustomerKey = sseKey;
   }

   public CopyPartRequest withSourceSSECustomerKey(SSECustomerKey sseKey) {
      this.setSourceSSECustomerKey(sseKey);
      return this;
   }

   public SSECustomerKey getDestinationSSECustomerKey() {
      return this.destinationSSECustomerKey;
   }

   public void setDestinationSSECustomerKey(SSECustomerKey sseKey) {
      this.destinationSSECustomerKey = sseKey;
   }

   public CopyPartRequest withDestinationSSECustomerKey(SSECustomerKey sseKey) {
      this.setDestinationSSECustomerKey(sseKey);
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public CopyPartRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }
}
