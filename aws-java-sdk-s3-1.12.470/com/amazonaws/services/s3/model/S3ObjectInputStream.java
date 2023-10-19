package com.amazonaws.services.s3.model;

import com.amazonaws.internal.MetricAware;
import com.amazonaws.internal.SdkFilterInputStream;
import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.metrics.MetricFilterInputStream;
import com.amazonaws.services.s3.metrics.S3ServiceMetric;
import com.amazonaws.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.client.methods.HttpRequestBase;

public class S3ObjectInputStream extends SdkFilterInputStream {
   private final HttpRequestBase httpRequest;

   public S3ObjectInputStream(InputStream in, HttpRequestBase httpRequest) {
      this(in, httpRequest, wrapWithByteCounting(in));
   }

   public S3ObjectInputStream(InputStream in, HttpRequestBase httpRequest, boolean collectMetrics) {
      super((InputStream)(collectMetrics ? new MetricFilterInputStream(S3ServiceMetric.S3DownloadThroughput, in) : in));
      this.httpRequest = httpRequest;
   }

   private static boolean wrapWithByteCounting(InputStream in) {
      if (!AwsSdkMetrics.isMetricsEnabled()) {
         return false;
      } else if (in instanceof MetricAware) {
         MetricAware aware = (MetricAware)in;
         return !aware.isMetricActivated();
      } else {
         return true;
      }
   }

   public void abort() {
      super.abort();
      if (this.httpRequest != null) {
         this.httpRequest.abort();
      }

      if (!(this.in instanceof SdkFilterInputStream)) {
         IOUtils.closeQuietly(this.in, null);
      }
   }

   public HttpRequestBase getHttpRequest() {
      return this.httpRequest;
   }

   public int available() throws IOException {
      int estimate = super.available();
      return estimate == 0 ? 1 : estimate;
   }

   public void close() throws IOException {
      super.close();
   }
}
