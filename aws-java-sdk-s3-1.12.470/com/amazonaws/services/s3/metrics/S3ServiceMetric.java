package com.amazonaws.services.s3.metrics;

import com.amazonaws.metrics.ServiceMetricType;
import com.amazonaws.metrics.SimpleMetricType;
import com.amazonaws.metrics.ThroughputMetricType;

public class S3ServiceMetric extends SimpleMetricType implements ServiceMetricType {
   static final String SERVICE_NAME_PREFIX = "S3";
   public static final S3ServiceMetric.S3ThroughputMetric S3DownloadThroughput = new S3ServiceMetric.S3ThroughputMetric(metricName("DownloadThroughput")) {
      public ServiceMetricType getByteCountMetricType() {
         return S3DownloadByteCount;
      }
   };
   public static final S3ServiceMetric S3DownloadByteCount = new S3ServiceMetric(metricName("DownloadByteCount"));
   public static final S3ServiceMetric.S3ThroughputMetric S3UploadThroughput = new S3ServiceMetric.S3ThroughputMetric(metricName("UploadThroughput")) {
      public ServiceMetricType getByteCountMetricType() {
         return S3UploadByteCount;
      }
   };
   public static final S3ServiceMetric S3UploadByteCount = new S3ServiceMetric(metricName("UploadByteCount"));
   private static final S3ServiceMetric[] values = new S3ServiceMetric[]{S3DownloadThroughput, S3DownloadByteCount, S3UploadThroughput, S3UploadByteCount};
   private final String name;

   private static final String metricName(String suffix) {
      return "S3" + suffix;
   }

   private S3ServiceMetric(String name) {
      this.name = name;
   }

   public String name() {
      return this.name;
   }

   public String getServiceName() {
      return "Amazon S3";
   }

   public static S3ServiceMetric[] values() {
      return (S3ServiceMetric[])values.clone();
   }

   public static S3ServiceMetric valueOf(String name) {
      for(S3ServiceMetric e : values()) {
         if (e.name().equals(name)) {
            return e;
         }
      }

      throw new IllegalArgumentException("No S3ServiceMetric defined for the name " + name);
   }

   private abstract static class S3ThroughputMetric extends S3ServiceMetric implements ThroughputMetricType {
      private S3ThroughputMetric(String name) {
         super(name);
      }
   }
}
