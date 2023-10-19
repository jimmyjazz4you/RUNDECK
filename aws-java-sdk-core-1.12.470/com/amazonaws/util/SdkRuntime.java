package com.amazonaws.util;

public enum SdkRuntime {
   public static boolean shouldAbort() {
      return Thread.interrupted();
   }
}
