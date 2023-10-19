package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class PartETag implements Serializable {
   private int partNumber;
   private String eTag;

   public PartETag(int partNumber, String eTag) {
      this.partNumber = partNumber;
      this.eTag = eTag;
   }

   public int getPartNumber() {
      return this.partNumber;
   }

   public void setPartNumber(int partNumber) {
      this.partNumber = partNumber;
   }

   public PartETag withPartNumber(int partNumber) {
      this.partNumber = partNumber;
      return this;
   }

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String eTag) {
      this.eTag = eTag;
   }

   public PartETag withETag(String eTag) {
      this.eTag = eTag;
      return this;
   }
}
