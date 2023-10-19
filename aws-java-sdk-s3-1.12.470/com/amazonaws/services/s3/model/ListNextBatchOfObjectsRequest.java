package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class ListNextBatchOfObjectsRequest extends AmazonWebServiceRequest implements Serializable {
   private ObjectListing previousObjectListing;

   public ListNextBatchOfObjectsRequest(ObjectListing previousObjectListing) {
      this.setPreviousObjectListing(previousObjectListing);
   }

   public ObjectListing getPreviousObjectListing() {
      return this.previousObjectListing;
   }

   public void setPreviousObjectListing(ObjectListing previousObjectListing) {
      if (previousObjectListing == null) {
         throw new IllegalArgumentException("The parameter previousObjectListing must be specified.");
      } else {
         this.previousObjectListing = previousObjectListing;
      }
   }

   public ListNextBatchOfObjectsRequest withPreviousObjectListing(ObjectListing previousObjectListing) {
      this.setPreviousObjectListing(previousObjectListing);
      return this;
   }

   public ListObjectsRequest toListObjectsRequest() {
      return new ListObjectsRequest(
            this.previousObjectListing.getBucketName(),
            this.previousObjectListing.getPrefix(),
            this.previousObjectListing.getNextMarker(),
            this.previousObjectListing.getDelimiter(),
            this.previousObjectListing.getMaxKeys()
         )
         .withEncodingType(this.previousObjectListing.getEncodingType());
   }
}
