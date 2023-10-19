package com.amazonaws.internal;

import com.amazonaws.util.AWSRequestMetrics;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SdkMetricsSocket extends DelegateSocket {
   private MetricsInputStream metricsIS;

   public SdkMetricsSocket(Socket sock) {
      super(sock);
   }

   public void setMetrics(AWSRequestMetrics metrics) {
      if (this.metricsIS == null) {
         throw new IllegalStateException("The underlying input stream must be initialized!");
      } else {
         this.metricsIS.setMetrics(metrics);
      }
   }

   @Override
   public InputStream getInputStream() throws IOException {
      this.metricsIS = new MetricsInputStream(this.sock.getInputStream());
      return this.metricsIS;
   }
}
