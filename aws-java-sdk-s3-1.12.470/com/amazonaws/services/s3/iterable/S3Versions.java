package com.amazonaws.services.s3.iterable;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import java.util.Iterator;

public class S3Versions implements Iterable<S3VersionSummary> {
   private AmazonS3 s3;
   private String bucketName;
   private String prefix;
   private String key;
   private Integer batchSize;

   private S3Versions(AmazonS3 s3, String bucketName) {
      this.s3 = s3;
      this.bucketName = bucketName;
   }

   public static S3Versions inBucket(AmazonS3 s3, String bucketName) {
      return new S3Versions(s3, bucketName);
   }

   public static S3Versions withPrefix(AmazonS3 s3, String bucketName, String prefix) {
      S3Versions versions = new S3Versions(s3, bucketName);
      versions.prefix = prefix;
      return versions;
   }

   public static S3Versions forKey(AmazonS3 s3, String bucketName, String key) {
      S3Versions versions = new S3Versions(s3, bucketName);
      versions.key = key;
      return versions;
   }

   public S3Versions withBatchSize(int batchSize) {
      this.batchSize = batchSize;
      return this;
   }

   public Integer getBatchSize() {
      return this.batchSize;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getKey() {
      return this.key;
   }

   public AmazonS3 getS3() {
      return this.s3;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   @Override
   public Iterator<S3VersionSummary> iterator() {
      return new S3Versions.VersionIterator();
   }

   private class VersionIterator implements Iterator<S3VersionSummary> {
      private VersionListing currentListing = null;
      private Iterator<S3VersionSummary> currentIterator = null;
      private S3VersionSummary nextSummary = null;

      private VersionIterator() {
      }

      @Override
      public boolean hasNext() {
         this.prepareCurrentListing();
         return this.nextMatchingSummary() != null;
      }

      public S3VersionSummary next() {
         this.prepareCurrentListing();
         S3VersionSummary returnValue = this.nextMatchingSummary();
         this.nextSummary = null;
         return returnValue;
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }

      private S3VersionSummary nextMatchingSummary() {
         return S3Versions.this.getKey() != null && (this.nextSummary == null || !this.nextSummary.getKey().equals(S3Versions.this.getKey()))
            ? null
            : this.nextSummary;
      }

      private void prepareCurrentListing() {
         for(;
            this.currentListing == null || !this.currentIterator.hasNext() && this.currentListing.isTruncated();
            this.currentIterator = this.currentListing.getVersionSummaries().iterator()
         ) {
            if (this.currentListing == null) {
               ListVersionsRequest req = new ListVersionsRequest();
               req.setBucketName(S3Versions.this.getBucketName());
               if (S3Versions.this.getKey() != null) {
                  req.setPrefix(S3Versions.this.getKey());
               } else {
                  req.setPrefix(S3Versions.this.getPrefix());
               }

               req.setMaxResults(S3Versions.this.getBatchSize());
               this.currentListing = S3Versions.this.getS3().listVersions(req);
            } else {
               this.currentListing = S3Versions.this.getS3().listNextBatchOfVersions(this.currentListing);
            }
         }

         if (this.nextSummary == null && this.currentIterator.hasNext()) {
            this.nextSummary = this.currentIterator.next();
         }
      }
   }
}
