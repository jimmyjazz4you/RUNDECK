package com.amazonaws.services.s3.model.analytics;

import java.io.Serializable;

public enum AnalyticsS3ExportFileFormat implements Serializable {
   CSV("CSV");

   private final String format;

   private AnalyticsS3ExportFileFormat(String format) {
      this.format = format;
   }

   @Override
   public String toString() {
      return this.format;
   }
}
