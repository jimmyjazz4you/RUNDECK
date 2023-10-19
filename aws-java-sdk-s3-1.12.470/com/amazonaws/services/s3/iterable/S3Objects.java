package com.amazonaws.services.s3.iterable;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.util.Iterator;

public class S3Objects implements Iterable<S3ObjectSummary> {
   private AmazonS3 s3;
   private String prefix = null;
   private String bucketName;
   private Integer batchSize = null;
   private String delimiter;
   private String marker;
   private String encodingType;
   private boolean requesterPays;

   private S3Objects(AmazonS3 s3, String bucketName) {
      this.s3 = s3;
      this.bucketName = bucketName;
   }

   public static S3Objects inBucket(AmazonS3 s3, String bucketName) {
      return new S3Objects(s3, bucketName);
   }

   public static S3Objects withPrefix(AmazonS3 s3, String bucketName, String prefix) {
      S3Objects objects = new S3Objects(s3, bucketName);
      objects.prefix = prefix;
      return objects;
   }

   public S3Objects withBatchSize(int batchSize) {
      this.batchSize = batchSize;
      return this;
   }

   public void withRequesterPays(boolean requesterPays) {
      this.requesterPays = requesterPays;
   }

   public void withEncodingType(String encodingType) {
      this.encodingType = encodingType;
   }

   public void withMarker(String marker) {
      this.marker = marker;
   }

   public void withDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public Integer getBatchSize() {
      return this.batchSize;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public String getDelimiter() {
      return this.delimiter;
   }

   public String getMarker() {
      return this.marker;
   }

   public String getEncodingType() {
      return this.encodingType;
   }

   public boolean isRequesterPays() {
      return this.requesterPays;
   }

   public AmazonS3 getS3() {
      return this.s3;
   }

   @Override
   public Iterator<S3ObjectSummary> iterator() {
      return new S3Objects.S3ObjectIterator();
   }

   private class S3ObjectIterator implements Iterator<S3ObjectSummary> {
      private ObjectListing currentListing = null;
      private Iterator<S3ObjectSummary> currentIterator = null;

      private S3ObjectIterator() {
      }

      @Override
      public boolean hasNext() {
         this.prepareCurrentListing();
         return this.currentIterator.hasNext();
      }

      public S3ObjectSummary next() {
         this.prepareCurrentListing();
         return this.currentIterator.next();
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }

      private void prepareCurrentListing() {
         for(;
            this.currentListing == null || !this.currentIterator.hasNext() && this.currentListing.isTruncated();
            this.currentIterator = this.currentListing.getObjectSummaries().iterator()
         ) {
            if (this.currentListing == null) {
               ListObjectsRequest req = new ListObjectsRequest();
               req.setBucketName(S3Objects.this.getBucketName());
               req.setPrefix(S3Objects.this.getPrefix());
               req.setMaxKeys(S3Objects.this.getBatchSize());
               req.setDelimiter(S3Objects.this.getDelimiter());
               req.setMarker(S3Objects.this.getMarker());
               req.setEncodingType(S3Objects.this.getEncodingType());
               req.setRequesterPays(S3Objects.this.isRequesterPays());
               this.currentListing = S3Objects.this.getS3().listObjects(req);
            } else {
               this.currentListing = S3Objects.this.getS3().listNextBatchOfObjects(this.currentListing);
            }
         }
      }
   }
}
