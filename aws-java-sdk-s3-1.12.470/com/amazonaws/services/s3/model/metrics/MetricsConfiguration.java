package com.amazonaws.services.s3.model.metrics;

import java.io.Serializable;

public class MetricsConfiguration implements Serializable {
   private String id;
   private MetricsFilter filter;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public MetricsConfiguration withId(String id) {
      this.setId(id);
      return this;
   }

   public MetricsFilter getFilter() {
      return this.filter;
   }

   public void setFilter(MetricsFilter filter) {
      this.filter = filter;
   }

   public MetricsConfiguration withFilter(MetricsFilter filter) {
      this.setFilter(filter);
      return this;
   }
}
