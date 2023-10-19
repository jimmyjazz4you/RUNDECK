package com.amazonaws.util;

import com.amazonaws.annotation.ThreadSafe;

@ThreadSafe
final class TimingInfoUnmodifiable extends TimingInfo {
   TimingInfoUnmodifiable(Long startEpochTimeMilli, long startTimeNano, Long endTimeNano) {
      super(startEpochTimeMilli, startTimeNano, endTimeNano);
   }

   @Override
   public void setEndTime(long _) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setEndTimeNano(long _) {
      throw new UnsupportedOperationException();
   }

   @Override
   public TimingInfo endTiming() {
      throw new UnsupportedOperationException();
   }
}
