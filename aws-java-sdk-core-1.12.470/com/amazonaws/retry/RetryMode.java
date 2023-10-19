package com.amazonaws.retry;

public enum RetryMode {
   LEGACY("legacy"),
   STANDARD("standard"),
   ADAPTIVE("adaptive");

   private final String name;

   private RetryMode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static RetryMode fromName(String value) {
      if (value == null) {
         return null;
      } else {
         for(RetryMode retryMode : values()) {
            if (retryMode.getName().equalsIgnoreCase(value)) {
               return retryMode;
            }
         }

         return null;
      }
   }
}
