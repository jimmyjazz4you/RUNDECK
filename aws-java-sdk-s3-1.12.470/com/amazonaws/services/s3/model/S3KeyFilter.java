package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class S3KeyFilter implements Serializable {
   private List<FilterRule> filterRules = new ArrayList<>();

   public List<FilterRule> getFilterRules() {
      return Collections.unmodifiableList(this.filterRules);
   }

   public void setFilterRules(List<FilterRule> filterRules) {
      this.filterRules = new ArrayList<>(filterRules);
   }

   public S3KeyFilter withFilterRules(List<FilterRule> filterRules) {
      this.setFilterRules(filterRules);
      return this;
   }

   public S3KeyFilter withFilterRules(FilterRule... filterRules) {
      this.setFilterRules(Arrays.asList(filterRules));
      return this;
   }

   public void addFilterRule(FilterRule filterRule) {
      this.filterRules.add(filterRule);
   }

   public static enum FilterRuleName {
      Prefix,
      Suffix;

      public FilterRule newRule() {
         return new FilterRule().withName(this.toString());
      }

      public FilterRule newRule(String value) {
         return this.newRule().withValue(value);
      }
   }
}
