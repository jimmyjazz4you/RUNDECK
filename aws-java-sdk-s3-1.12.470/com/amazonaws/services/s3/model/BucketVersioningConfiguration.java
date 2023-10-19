package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class BucketVersioningConfiguration implements Serializable {
   public static final String OFF = "Off";
   public static final String SUSPENDED = "Suspended";
   public static final String ENABLED = "Enabled";
   private String status;
   private Boolean isMfaDeleteEnabled = null;

   public BucketVersioningConfiguration() {
      this.setStatus("Off");
   }

   public BucketVersioningConfiguration(String status) {
      this.setStatus(status);
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public BucketVersioningConfiguration withStatus(String status) {
      this.setStatus(status);
      return this;
   }

   public Boolean isMfaDeleteEnabled() {
      return this.isMfaDeleteEnabled;
   }

   public void setMfaDeleteEnabled(Boolean mfaDeleteEnabled) {
      this.isMfaDeleteEnabled = mfaDeleteEnabled;
   }

   public BucketVersioningConfiguration withMfaDeleteEnabled(Boolean mfaDeleteEnabled) {
      this.setMfaDeleteEnabled(mfaDeleteEnabled);
      return this;
   }
}
