package com.amazonaws;

import com.amazonaws.annotation.NotThreadSafe;
import org.apache.http.conn.socket.ConnectionSocketFactory;

@NotThreadSafe
public final class ApacheHttpClientConfig {
   private ConnectionSocketFactory sslSocketFactory;

   ApacheHttpClientConfig() {
   }

   ApacheHttpClientConfig(ApacheHttpClientConfig that) {
      this.sslSocketFactory = that.sslSocketFactory;
   }

   public ConnectionSocketFactory getSslSocketFactory() {
      return this.sslSocketFactory;
   }

   public void setSslSocketFactory(ConnectionSocketFactory sslSocketFactory) {
      this.sslSocketFactory = sslSocketFactory;
   }

   public ApacheHttpClientConfig withSslSocketFactory(ConnectionSocketFactory sslSocketFactory) {
      this.sslSocketFactory = sslSocketFactory;
      return this;
   }
}
