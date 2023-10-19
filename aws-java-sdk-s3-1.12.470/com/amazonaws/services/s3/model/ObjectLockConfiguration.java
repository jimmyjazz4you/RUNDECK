package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ObjectLockConfiguration implements Serializable {
   private String objectLockEnabled;
   private ObjectLockRule rule;

   public String getObjectLockEnabled() {
      return this.objectLockEnabled;
   }

   public ObjectLockConfiguration withObjectLockEnabled(String objectLockEnabled) {
      this.objectLockEnabled = objectLockEnabled;
      return this;
   }

   public ObjectLockConfiguration withObjectLockEnabled(ObjectLockEnabled objectLockEnabled) {
      return this.withObjectLockEnabled(objectLockEnabled.toString());
   }

   public void setObjectLockEnabled(String objectLockEnabled) {
      this.withObjectLockEnabled(objectLockEnabled);
   }

   public void setObjectLockEnabled(ObjectLockEnabled objectLockEnabled) {
      this.setObjectLockEnabled(objectLockEnabled.toString());
   }

   public ObjectLockRule getRule() {
      return this.rule;
   }

   public ObjectLockConfiguration withRule(ObjectLockRule rule) {
      this.rule = rule;
      return this;
   }

   public void setRule(ObjectLockRule rule) {
      this.withRule(rule);
   }
}
