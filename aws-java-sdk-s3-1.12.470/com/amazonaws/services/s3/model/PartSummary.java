package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Date;

public class PartSummary implements Serializable {
   private int partNumber;
   private Date lastModified;
   private String eTag;
   private long size;

   public int getPartNumber() {
      return this.partNumber;
   }

   public void setPartNumber(int partNumber) {
      this.partNumber = partNumber;
   }

   public Date getLastModified() {
      return this.lastModified;
   }

   public void setLastModified(Date lastModified) {
      this.lastModified = lastModified;
   }

   public String getETag() {
      return this.eTag;
   }

   public void setETag(String eTag) {
      this.eTag = eTag;
   }

   public long getSize() {
      return this.size;
   }

   public void setSize(long size) {
      this.size = size;
   }
}
