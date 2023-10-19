package com.amazonaws.services.s3.model.analytics;

import java.io.Serializable;

public class AnalyticsConfiguration implements Serializable {
   private String id;
   private AnalyticsFilter filter;
   private StorageClassAnalysis storageClassAnalysis;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public AnalyticsConfiguration withId(String id) {
      this.setId(id);
      return this;
   }

   public AnalyticsFilter getFilter() {
      return this.filter;
   }

   public void setFilter(AnalyticsFilter filter) {
      this.filter = filter;
   }

   public AnalyticsConfiguration withFilter(AnalyticsFilter filter) {
      this.setFilter(filter);
      return this;
   }

   public StorageClassAnalysis getStorageClassAnalysis() {
      return this.storageClassAnalysis;
   }

   public void setStorageClassAnalysis(StorageClassAnalysis storageClassAnalysis) {
      this.storageClassAnalysis = storageClassAnalysis;
   }

   public AnalyticsConfiguration withStorageClassAnalysis(StorageClassAnalysis storageClassAnalysis) {
      this.setStorageClassAnalysis(storageClassAnalysis);
      return this;
   }
}
