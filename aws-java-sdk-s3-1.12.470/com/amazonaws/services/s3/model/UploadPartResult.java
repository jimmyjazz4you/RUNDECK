package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import com.amazonaws.services.s3.internal.SSEResultBase;
import java.io.Serializable;

public class UploadPartResult extends SSEResultBase implements Serializable, S3RequesterChargedResult {
   private int partNumber;
   private String eTag;
   private boolean isRequesterCharged;

   public int getPartNumber() {
      return this.partNumber;
   }

   public void setPartNumber(int partNumber) {
      this.partNumber = partNumber;
   }

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String eTag) {
      this.eTag = eTag;
   }

   public PartETag getPartETag() {
      return new PartETag(this.partNumber, this.eTag);
   }

   @Override
   public boolean isRequesterCharged() {
      return this.isRequesterCharged;
   }

   @Override
   public void setRequesterCharged(boolean isRequesterCharged) {
      this.isRequesterCharged = isRequesterCharged;
   }
}
