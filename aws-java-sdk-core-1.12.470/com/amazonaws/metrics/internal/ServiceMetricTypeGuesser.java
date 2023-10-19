package com.amazonaws.metrics.internal;

import com.amazonaws.Request;
import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.metrics.SimpleThroughputMetricType;
import com.amazonaws.metrics.ThroughputMetricType;

public enum ServiceMetricTypeGuesser {
   public static ThroughputMetricType guessThroughputMetricType(Request<?> req, String metricNameSuffix, String byteCountMetricNameSuffix) {
      if (!AwsSdkMetrics.isMetricsEnabled()) {
         return null;
      } else {
         Object orig = req.getOriginalRequestObject();
         return orig.getClass().getName().startsWith("com.amazonaws.services.s3")
            ? new SimpleThroughputMetricType("S3" + metricNameSuffix, req.getServiceName(), "S3" + byteCountMetricNameSuffix)
            : null;
      }
   }
}
