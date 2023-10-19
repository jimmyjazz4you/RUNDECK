package com.amazonaws.services.s3.model;

public class SetObjectTaggingResult {
   private String versionId;

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public SetObjectTaggingResult withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }
}
