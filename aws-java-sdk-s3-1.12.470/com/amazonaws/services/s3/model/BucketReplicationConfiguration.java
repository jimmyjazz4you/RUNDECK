package com.amazonaws.services.s3.model;

import com.amazonaws.util.json.Jackson;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BucketReplicationConfiguration implements Serializable {
   private String roleARN;
   private Map<String, ReplicationRule> rules = new HashMap<>();

   public String getRoleARN() {
      return this.roleARN;
   }

   public void setRoleARN(String roleARN) {
      this.roleARN = roleARN;
   }

   public BucketReplicationConfiguration withRoleARN(String roleARN) {
      this.setRoleARN(roleARN);
      return this;
   }

   public Map<String, ReplicationRule> getRules() {
      return this.rules;
   }

   public ReplicationRule getRule(String id) {
      return this.rules.get(id);
   }

   public void setRules(Map<String, ReplicationRule> rules) {
      if (rules == null) {
         throw new IllegalArgumentException("Replication rules cannot be null");
      } else {
         this.rules = new HashMap<>(rules);
      }
   }

   public BucketReplicationConfiguration withRules(Map<String, ReplicationRule> rules) {
      this.setRules(rules);
      return this;
   }

   public BucketReplicationConfiguration addRule(String id, ReplicationRule rule) {
      if (id == null || id.trim().isEmpty()) {
         throw new IllegalArgumentException("Rule id cannot be null or empty.");
      } else if (rule == null) {
         throw new IllegalArgumentException("Replication rule cannot be null");
      } else {
         this.rules.put(id, rule);
         return this;
      }
   }

   public BucketReplicationConfiguration removeRule(String id) {
      this.rules.remove(id);
      return this;
   }

   @Override
   public String toString() {
      return Jackson.toJsonString(this);
   }
}
