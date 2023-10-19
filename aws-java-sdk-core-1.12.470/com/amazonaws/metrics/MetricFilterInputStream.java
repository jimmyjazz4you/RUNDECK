package com.amazonaws.metrics;

import com.amazonaws.internal.SdkFilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MetricFilterInputStream extends SdkFilterInputStream {
   private final ByteThroughputHelper helper;

   public MetricFilterInputStream(ThroughputMetricType type, InputStream in) {
      super(in);
      this.helper = new ByteThroughputHelper(type);
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      this.abortIfNeeded();
      long startNano = this.helper.startTiming();
      int bytesRead = this.in.read(b, off, len);
      if (bytesRead > 0) {
         this.helper.increment(bytesRead, startNano);
      }

      return bytesRead;
   }

   @Override
   public void close() throws IOException {
      this.helper.reportMetrics();
      this.in.close();
      this.abortIfNeeded();
   }

   @Override
   public final boolean isMetricActivated() {
      return true;
   }
}
