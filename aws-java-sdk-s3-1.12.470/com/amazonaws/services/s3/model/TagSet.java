package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class TagSet implements Serializable {
   private Map<String, String> tags = new LinkedHashMap<>(1);

   public TagSet() {
   }

   public TagSet(Map<String, String> tags) {
      this.tags.putAll(tags);
   }

   public String getTag(String key) {
      return this.tags.get(key);
   }

   public void setTag(String key, String value) {
      this.tags.put(key, value);
   }

   public Map<String, String> getAllTags() {
      return this.tags;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      sb.append("Tags: " + this.getAllTags());
      sb.append("}");
      return sb.toString();
   }
}
