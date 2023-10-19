package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.SSEResultBase;
import java.io.Serializable;
import java.util.Date;

public class CopyPartResult extends SSEResultBase implements Serializable {
   private String etag;
   private Date lastModifiedDate;
   private String versionId;
   private int partNumber;

   public int getPartNumber() {
      return this.partNumber;
   }

   public void setPartNumber(int partNumber) {
      this.partNumber = partNumber;
   }

   public String getETag() {
      return this.etag;
   }

   public void setETag(String etag) {
      this.etag = etag;
   }

   public PartETag getPartETag() {
      return new PartETag(this.partNumber, this.etag);
   }

   public Date getLastModifiedDate() {
      return this.lastModifiedDate;
   }

   public void setLastModifiedDate(Date lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }
}
