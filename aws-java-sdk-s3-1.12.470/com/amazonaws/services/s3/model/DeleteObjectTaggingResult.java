package com.amazonaws.services.s3.model;

public class DeleteObjectTaggingResult {
   private String versionId;

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public DeleteObjectTaggingResult withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }
}
