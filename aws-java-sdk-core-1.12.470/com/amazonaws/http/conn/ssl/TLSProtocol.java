package com.amazonaws.http.conn.ssl;

enum TLSProtocol {
   TLSv1_2("TLSv1.2"),
   TLSv1_1("TLSv1.1"),
   TLSv1("TLSv1"),
   TLS("TLS");

   private final String protocolName;

   private TLSProtocol(String protocolName) {
      this.protocolName = protocolName;
   }

   String getProtocolName() {
      return this.protocolName;
   }
}
