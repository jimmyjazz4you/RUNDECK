package com.amazonaws.util;

import com.amazonaws.annotation.NotThreadSafe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.LogFactory;

@NotThreadSafe
class TimingInfoFullSupport extends TimingInfo {
   private final Map<String, List<TimingInfo>> subMeasurementsByName = new HashMap<>();
   private final Map<String, Number> countersByName = new HashMap<>();

   TimingInfoFullSupport(Long startEpochTimeMilli, long startTimeNano, Long endTimeNano) {
      super(startEpochTimeMilli, startTimeNano, endTimeNano);
   }

   @Override
   public void addSubMeasurement(String subMeasurementName, TimingInfo ti) {
      List<TimingInfo> timings = this.subMeasurementsByName.get(subMeasurementName);
      if (timings == null) {
         timings = new ArrayList<>();
         this.subMeasurementsByName.put(subMeasurementName, timings);
      }

      if (ti.isEndTimeKnown()) {
         timings.add(ti);
      } else {
         LogFactory.getLog(this.getClass()).debug("Skip submeasurement timing info with no end time for " + subMeasurementName);
      }
   }

   @Override
   public TimingInfo getSubMeasurement(String subMeasurementName) {
      return this.getSubMeasurement(subMeasurementName, 0);
   }

   @Override
   public TimingInfo getSubMeasurement(String subMesurementName, int index) {
      List<TimingInfo> timings = this.subMeasurementsByName.get(subMesurementName);
      return index >= 0 && timings != null && timings.size() != 0 && index < timings.size() ? timings.get(index) : null;
   }

   @Override
   public TimingInfo getLastSubMeasurement(String subMeasurementName) {
      if (this.subMeasurementsByName != null && this.subMeasurementsByName.size() != 0) {
         List<TimingInfo> timings = this.subMeasurementsByName.get(subMeasurementName);
         return timings != null && timings.size() != 0 ? timings.get(timings.size() - 1) : null;
      } else {
         return null;
      }
   }

   @Override
   public List<TimingInfo> getAllSubMeasurements(String subMeasurementName) {
      return this.subMeasurementsByName.get(subMeasurementName);
   }

   @Override
   public Map<String, List<TimingInfo>> getSubMeasurementsByName() {
      return this.subMeasurementsByName;
   }

   @Override
   public Number getCounter(String key) {
      return this.countersByName.get(key);
   }

   @Override
   public Map<String, Number> getAllCounters() {
      return this.countersByName;
   }

   @Override
   public void setCounter(String key, long count) {
      this.countersByName.put(key, count);
   }

   @Override
   public void incrementCounter(String key) {
      int count = 0;
      Number counter = this.getCounter(key);
      if (counter != null) {
         count = counter.intValue();
      }

      this.setCounter(key, (long)(++count));
   }
}
