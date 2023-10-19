package com.amazonaws.metrics;

public abstract class ByteThroughputProvider {
   private long duration;
   private int byteCount;
   private final ThroughputMetricType throughputType;

   protected ByteThroughputProvider(ThroughputMetricType type) {
      this.throughputType = type;
   }

   public ThroughputMetricType getThroughputMetricType() {
      return this.throughputType;
   }

   public int getByteCount() {
      return this.byteCount;
   }

   public long getDurationNano() {
      return this.duration;
   }

   public String getProviderId() {
      return super.toString();
   }

   protected void increment(int bytesDelta, long startTimeNano) {
      this.byteCount += bytesDelta;
      this.duration += System.nanoTime() - startTimeNano;
   }

   protected void reset() {
      this.byteCount = 0;
      this.duration = 0L;
   }

   @Override
   public String toString() {
      return String.format(
         "providerId=%s, throughputType=%s, byteCount=%d, duration=%d", this.getProviderId(), this.throughputType, this.byteCount, this.duration
      );
   }
}
