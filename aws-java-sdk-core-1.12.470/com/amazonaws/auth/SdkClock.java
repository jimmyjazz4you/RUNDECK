package com.amazonaws.auth;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import java.util.Date;

@SdkProtectedApi
public interface SdkClock {
   SdkClock STANDARD = new SdkClock() {
      @Override
      public long currentTimeMillis() {
         return System.currentTimeMillis();
      }
   };

   long currentTimeMillis();

   public static final class Instance {
      private static SdkClock clock = SdkClock.STANDARD;

      public static SdkClock get() {
         return clock;
      }

      @SdkTestInternalApi
      public static void set(SdkClock newClock) {
         clock = newClock;
      }

      @SdkTestInternalApi
      public static void reset() {
         clock = SdkClock.STANDARD;
      }
   }

   public static final class MockClock implements SdkClock {
      private final long mockedTime;

      public MockClock(Date mockedTime) {
         this(mockedTime.getTime());
      }

      public MockClock(long mockedTime) {
         this.mockedTime = mockedTime;
      }

      @Override
      public long currentTimeMillis() {
         return this.mockedTime;
      }
   }
}
