package com.amazonaws.http.conn;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

public class SdkConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
   private final long maxIdleTime;

   public SdkConnectionKeepAliveStrategy(long maxIdleTime) {
      this.maxIdleTime = maxIdleTime;
   }

   public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
      long duration = DefaultConnectionKeepAliveStrategy.INSTANCE.getKeepAliveDuration(response, context);
      return 0L < duration && duration < this.maxIdleTime ? duration : this.maxIdleTime;
   }
}
