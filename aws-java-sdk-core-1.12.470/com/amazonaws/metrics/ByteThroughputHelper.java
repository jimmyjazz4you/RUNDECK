package com.amazonaws.metrics;

import java.util.concurrent.TimeUnit;

class ByteThroughputHelper extends ByteThroughputProvider {
   private static final int REPORT_INTERVAL_SECS = 10;

   ByteThroughputHelper(ThroughputMetricType type) {
      super(type);
   }

   long startTiming() {
      if (TimeUnit.NANOSECONDS.toSeconds(this.getDurationNano()) > 10L) {
         this.reportMetrics();
      }

      return System.nanoTime();
   }

   void reportMetrics() {
      if (this.getByteCount() > 0) {
         ServiceMetricCollector col = AwsSdkMetrics.getServiceMetricCollector();
         col.collectByteThroughput(this);
         this.reset();
      }
   }

   @Override
   public void increment(int bytesDelta, long startTimeNano) {
      super.increment(bytesDelta, startTimeNano);
   }
}
