package com.amazonaws.util;

public class Platform {
   private Platform() {
   }

   public static boolean isWindows() {
      String osName = System.getProperty("os.name");
      return osName != null && osName.startsWith("Windows");
   }
}
