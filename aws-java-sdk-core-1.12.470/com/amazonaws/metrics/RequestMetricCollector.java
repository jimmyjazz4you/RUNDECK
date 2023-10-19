package com.amazonaws.metrics;

import com.amazonaws.Request;
import com.amazonaws.Response;

public abstract class RequestMetricCollector {
   public static final RequestMetricCollector NONE = new RequestMetricCollector() {
      @Override
      public void collectMetrics(Request<?> request, Response<?> response) {
      }

      @Override
      public boolean isEnabled() {
         return false;
      }
   };

   public abstract void collectMetrics(Request<?> var1, Response<?> var2);

   public boolean isEnabled() {
      return true;
   }

   public interface Factory {
      RequestMetricCollector getRequestMetricCollector();
   }
}
