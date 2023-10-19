package com.amazonaws;

public enum Protocol {
   HTTP("http"),
   HTTPS("https");

   private final String protocol;

   private Protocol(String protocol) {
      this.protocol = protocol;
   }

   @Override
   public String toString() {
      return this.protocol;
   }
}
