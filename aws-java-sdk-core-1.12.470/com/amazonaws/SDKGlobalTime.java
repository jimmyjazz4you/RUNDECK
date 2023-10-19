package com.amazonaws;

public class SDKGlobalTime {
   private static volatile int globalTimeOffset;

   public static void setGlobalTimeOffset(int timeOffset) {
      globalTimeOffset = timeOffset;
   }

   public static int getGlobalTimeOffset() {
      return globalTimeOffset;
   }
}
