package com.amazonaws.internal;

import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.util.AWSRequestMetrics;
import java.io.IOException;
import java.io.InputStream;
import javax.net.ssl.SSLSocket;

public class SdkSSLMetricsSocket extends DelegateSSLSocket {
   private MetricsInputStream metricsIS;

   public SdkSSLMetricsSocket(SSLSocket sock) {
      super(sock);
   }

   public void setMetrics(AWSRequestMetrics metrics) throws IOException {
      this.getInputStream();
      this.metricsIS.setMetrics(metrics);
   }

   @Override
   public InputStream getInputStream() throws IOException {
      if (this.metricsIS == null) {
         this.metricsIS = new MetricsInputStream(this.sock.getInputStream());
      }

      return this.metricsIS;
   }

   @SdkTestInternalApi
   MetricsInputStream getMetricsInputStream() {
      return this.metricsIS;
   }
}
