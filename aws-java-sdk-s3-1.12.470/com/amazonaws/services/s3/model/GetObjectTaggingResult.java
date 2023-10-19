package com.amazonaws.services.s3.model;

import java.util.List;

public class GetObjectTaggingResult {
   private String versionId;
   private List<Tag> tagSet;

   public GetObjectTaggingResult(List<Tag> tagSet) {
      this.tagSet = tagSet;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public GetObjectTaggingResult withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }

   public List<Tag> getTagSet() {
      return this.tagSet;
   }

   public void setTagSet(List<Tag> tagSet) {
      this.tagSet = tagSet;
   }

   public GetObjectTaggingResult withTagSet(List<Tag> tagSet) {
      this.setTagSet(tagSet);
      return this;
   }
}
