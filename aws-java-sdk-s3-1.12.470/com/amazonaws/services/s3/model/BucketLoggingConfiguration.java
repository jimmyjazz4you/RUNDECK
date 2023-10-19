package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class BucketLoggingConfiguration implements Serializable {
   private String destinationBucketName = null;
   private String logFilePrefix = null;

   public BucketLoggingConfiguration() {
   }

   public BucketLoggingConfiguration(String destinationBucketName, String logFilePrefix) {
      this.setLogFilePrefix(logFilePrefix);
      this.setDestinationBucketName(destinationBucketName);
   }

   public boolean isLoggingEnabled() {
      return this.destinationBucketName != null && this.logFilePrefix != null;
   }

   public String getLogFilePrefix() {
      return this.logFilePrefix;
   }

   public void setLogFilePrefix(String logFilePrefix) {
      if (logFilePrefix == null) {
         logFilePrefix = "";
      }

      this.logFilePrefix = logFilePrefix;
   }

   public String getDestinationBucketName() {
      return this.destinationBucketName;
   }

   public void setDestinationBucketName(String destinationBucketName) {
      this.destinationBucketName = destinationBucketName;
   }

   @Override
   public String toString() {
      String result = "LoggingConfiguration enabled=" + this.isLoggingEnabled();
      if (this.isLoggingEnabled()) {
         result = result + ", destinationBucketName=" + this.getDestinationBucketName() + ", logFilePrefix=" + this.getLogFilePrefix();
      }

      return result;
   }
}
