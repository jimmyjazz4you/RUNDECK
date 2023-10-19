package com.amazonaws.internal;

import com.amazonaws.util.AWSRequestMetrics;
import java.io.IOException;
import java.io.InputStream;

public class MetricsInputStream extends DelegateInputStream {
   private AWSRequestMetrics metrics;

   public MetricsInputStream(InputStream in) {
      super(in);
   }

   public void setMetrics(AWSRequestMetrics metrics) {
      this.metrics = metrics;
   }

   @Override
   public int read() throws IOException {
      if (this.metrics != null) {
         this.metrics.startEvent(AWSRequestMetrics.Field.HttpSocketReadTime);
      }

      int var1;
      try {
         var1 = this.in.read();
      } finally {
         if (this.metrics != null) {
            this.metrics.endEvent(AWSRequestMetrics.Field.HttpSocketReadTime);
         }
      }

      return var1;
   }

   @Override
   public int read(byte[] b) throws IOException {
      if (this.metrics != null) {
         this.metrics.startEvent(AWSRequestMetrics.Field.HttpSocketReadTime);
      }

      int var2;
      try {
         var2 = this.in.read(b);
      } finally {
         if (this.metrics != null) {
            this.metrics.endEvent(AWSRequestMetrics.Field.HttpSocketReadTime);
         }
      }

      return var2;
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      if (this.metrics != null) {
         this.metrics.startEvent(AWSRequestMetrics.Field.HttpSocketReadTime);
      }

      int var4;
      try {
         var4 = this.in.read(b, off, len);
      } finally {
         if (this.metrics != null) {
            this.metrics.endEvent(AWSRequestMetrics.Field.HttpSocketReadTime);
         }
      }

      return var4;
   }
}
