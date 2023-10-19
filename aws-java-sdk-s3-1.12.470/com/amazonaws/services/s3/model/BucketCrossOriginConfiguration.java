package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class BucketCrossOriginConfiguration implements Serializable {
   private List<CORSRule> rules;

   public List<CORSRule> getRules() {
      return this.rules;
   }

   public void setRules(List<CORSRule> rules) {
      this.rules = rules;
   }

   public BucketCrossOriginConfiguration withRules(List<CORSRule> rules) {
      this.setRules(rules);
      return this;
   }

   public BucketCrossOriginConfiguration withRules(CORSRule... rules) {
      this.setRules(Arrays.asList(rules));
      return this;
   }

   public BucketCrossOriginConfiguration(List<CORSRule> rules) {
      this.rules = rules;
   }

   public BucketCrossOriginConfiguration() {
   }
}
