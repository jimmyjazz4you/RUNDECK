package com.amazonaws.services.s3.model.analytics;

import java.io.Serializable;

public class StorageClassAnalysis implements Serializable {
   private StorageClassAnalysisDataExport dataExport;

   public StorageClassAnalysisDataExport getDataExport() {
      return this.dataExport;
   }

   public void setDataExport(StorageClassAnalysisDataExport dataExport) {
      this.dataExport = dataExport;
   }

   public StorageClassAnalysis withDataExport(StorageClassAnalysisDataExport dataExport) {
      this.setDataExport(dataExport);
      return this;
   }
}
