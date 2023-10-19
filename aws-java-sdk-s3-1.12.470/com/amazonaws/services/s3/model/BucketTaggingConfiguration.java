package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BucketTaggingConfiguration implements Serializable {
   private List<TagSet> tagSets = null;

   public BucketTaggingConfiguration() {
      this.tagSets = new ArrayList<>(1);
   }

   public BucketTaggingConfiguration(Collection<TagSet> tagSets) {
      this.tagSets = new ArrayList<>(1);
      this.tagSets.addAll(tagSets);
   }

   public BucketTaggingConfiguration withTagSets(TagSet... tagSets) {
      this.tagSets.clear();

      for(int index = 0; index < tagSets.length; ++index) {
         this.tagSets.add(tagSets[index]);
      }

      return this;
   }

   public void setTagSets(Collection<TagSet> tagSets) {
      this.tagSets.clear();
      this.tagSets.addAll(tagSets);
   }

   public List<TagSet> getAllTagSets() {
      return this.tagSets;
   }

   public TagSet getTagSet() {
      return this.tagSets.get(0);
   }

   public TagSet getTagSetAtIndex(int index) {
      return this.tagSets.get(index);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      sb.append("TagSets: " + this.getAllTagSets());
      sb.append("}");
      return sb.toString();
   }
}
