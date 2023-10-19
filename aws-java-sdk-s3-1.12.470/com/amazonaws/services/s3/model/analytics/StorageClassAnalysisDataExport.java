package com.amazonaws.services.s3.model.analytics;

import java.io.Serializable;

public class StorageClassAnalysisDataExport implements Serializable {
   private String outputSchemaVersion;
   private AnalyticsExportDestination destination;

   public void setOutputSchemaVersion(StorageClassAnalysisSchemaVersion outputSchemaVersion) {
      if (outputSchemaVersion == null) {
         this.setOutputSchemaVersion((String)null);
      } else {
         this.setOutputSchemaVersion(outputSchemaVersion.toString());
      }
   }

   public StorageClassAnalysisDataExport withOutputSchemaVersion(StorageClassAnalysisSchemaVersion outputSchemaVersion) {
      this.setOutputSchemaVersion(outputSchemaVersion);
      return this;
   }

   public String getOutputSchemaVersion() {
      return this.outputSchemaVersion;
   }

   public void setOutputSchemaVersion(String outputSchemaVersion) {
      this.outputSchemaVersion = outputSchemaVersion;
   }

   public StorageClassAnalysisDataExport withOutputSchemaVersion(String outputSchemaVersion) {
      this.setOutputSchemaVersion(outputSchemaVersion);
      return this;
   }

   public AnalyticsExportDestination getDestination() {
      return this.destination;
   }

   public void setDestination(AnalyticsExportDestination destination) {
      this.destination = destination;
   }

   public StorageClassAnalysisDataExport withDestination(AnalyticsExportDestination destination) {
      this.setDestination(destination);
      return this;
   }
}
