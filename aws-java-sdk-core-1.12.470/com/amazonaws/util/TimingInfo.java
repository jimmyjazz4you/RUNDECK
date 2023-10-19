package com.amazonaws.util;

import com.amazonaws.annotation.NotThreadSafe;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@NotThreadSafe
public class TimingInfo {
   static final int UNKNOWN = -1;
   private final Long startEpochTimeMilli;
   private final long startTimeNano;
   private Long endTimeNano;

   public static TimingInfo startTiming() {
      return new TimingInfo(System.currentTimeMillis(), System.nanoTime(), null);
   }

   public static TimingInfo startTimingFullSupport() {
      return new TimingInfoFullSupport(System.currentTimeMillis(), System.nanoTime(), null);
   }

   public static TimingInfo startTimingFullSupport(long startTimeNano) {
      return new TimingInfoFullSupport(null, startTimeNano, null);
   }

   public static TimingInfo startTimingFullSupport(long startTimeMillis, long startTimeNano) {
      return new TimingInfoFullSupport(startTimeMillis, startTimeNano, null);
   }

   public static TimingInfo newTimingInfoFullSupport(long startTimeNano, long endTimeNano) {
      return new TimingInfoFullSupport(null, startTimeNano, endTimeNano);
   }

   public static TimingInfo newTimingInfoFullSupport(long startEpochTimeMilli, long startTimeNano, long endTimeNano) {
      return new TimingInfoFullSupport(startEpochTimeMilli, startTimeNano, endTimeNano);
   }

   public static TimingInfo unmodifiableTimingInfo(long startTimeNano, Long endTimeNano) {
      return new TimingInfoUnmodifiable(null, startTimeNano, endTimeNano);
   }

   public static TimingInfo unmodifiableTimingInfo(long startEpochTimeMilli, long startTimeNano, Long endTimeNano) {
      return new TimingInfoUnmodifiable(startEpochTimeMilli, startTimeNano, endTimeNano);
   }

   protected TimingInfo(Long startEpochTimeMilli, long startTimeNano, Long endTimeNano) {
      this.startEpochTimeMilli = startEpochTimeMilli;
      this.startTimeNano = startTimeNano;
      this.endTimeNano = endTimeNano;
   }

   @Deprecated
   public final long getStartTime() {
      return this.isStartEpochTimeMilliKnown() ? this.startEpochTimeMilli : TimeUnit.NANOSECONDS.toMillis(this.startTimeNano);
   }

   @Deprecated
   public final long getStartEpochTimeMilli() {
      Long v = this.getStartEpochTimeMilliIfKnown();
      return v == null ? -1L : v;
   }

   public final Long getStartEpochTimeMilliIfKnown() {
      return this.startEpochTimeMilli;
   }

   public final long getStartTimeNano() {
      return this.startTimeNano;
   }

   @Deprecated
   public final long getEndTime() {
      return this.getEndEpochTimeMilli();
   }

   @Deprecated
   public final long getEndEpochTimeMilli() {
      Long v = this.getEndEpochTimeMilliIfKnown();
      return v == null ? -1L : v;
   }

   public final Long getEndEpochTimeMilliIfKnown() {
      return this.isStartEpochTimeMilliKnown() && this.isEndTimeKnown()
         ? this.startEpochTimeMilli + TimeUnit.NANOSECONDS.toMillis(this.endTimeNano - this.startTimeNano)
         : null;
   }

   public final long getEndTimeNano() {
      return this.endTimeNano == null ? -1L : this.endTimeNano;
   }

   public final Long getEndTimeNanoIfKnown() {
      return this.endTimeNano;
   }

   @Deprecated
   public final double getTimeTakenMillis() {
      Double v = this.getTimeTakenMillisIfKnown();
      return v == null ? -1.0 : v;
   }

   public final Double getTimeTakenMillisIfKnown() {
      return this.isEndTimeKnown() ? durationMilliOf(this.startTimeNano, this.endTimeNano) : null;
   }

   public static double durationMilliOf(long startTimeNano, long endTimeNano) {
      double micros = (double)TimeUnit.NANOSECONDS.toMicros(endTimeNano - startTimeNano);
      return micros / 1000.0;
   }

   @Deprecated
   public final long getElapsedTimeMillis() {
      Double v = this.getTimeTakenMillisIfKnown();
      return v == null ? -1L : v.longValue();
   }

   public final boolean isEndTimeKnown() {
      return this.endTimeNano != null;
   }

   public final boolean isStartEpochTimeMilliKnown() {
      return this.startEpochTimeMilli != null;
   }

   @Override
   public final String toString() {
      return String.valueOf(this.getTimeTakenMillis());
   }

   @Deprecated
   public void setEndTime(long endTimeMilli) {
      this.endTimeNano = TimeUnit.MILLISECONDS.toNanos(endTimeMilli);
   }

   public void setEndTimeNano(long endTimeNano) {
      this.endTimeNano = endTimeNano;
   }

   public TimingInfo endTiming() {
      this.endTimeNano = System.nanoTime();
      return this;
   }

   public void addSubMeasurement(String subMeasurementName, TimingInfo timingInfo) {
   }

   public TimingInfo getSubMeasurement(String subMeasurementName) {
      return null;
   }

   public TimingInfo getSubMeasurement(String subMesurementName, int index) {
      return null;
   }

   public TimingInfo getLastSubMeasurement(String subMeasurementName) {
      return null;
   }

   public List<TimingInfo> getAllSubMeasurements(String subMeasurementName) {
      return null;
   }

   public Map<String, List<TimingInfo>> getSubMeasurementsByName() {
      return Collections.emptyMap();
   }

   public Number getCounter(String key) {
      return null;
   }

   public Map<String, Number> getAllCounters() {
      return Collections.emptyMap();
   }

   public void setCounter(String key, long count) {
   }

   public void incrementCounter(String key) {
   }
}
