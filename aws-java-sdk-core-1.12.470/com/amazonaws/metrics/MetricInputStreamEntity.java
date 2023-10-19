package com.amazonaws.metrics;

import com.amazonaws.internal.MetricAware;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

public class MetricInputStreamEntity extends InputStreamEntity {
   private static final int BUFFER_SIZE = 2048;
   private final ByteThroughputHelper helper;

   public MetricInputStreamEntity(ThroughputMetricType metricType, InputStream instream, long length) {
      super(instream, length);
      this.helper = new ByteThroughputHelper(metricType);
   }

   public MetricInputStreamEntity(ThroughputMetricType metricType, InputStream instream, long length, ContentType contentType) {
      super(instream, length, contentType);
      this.helper = new ByteThroughputHelper(metricType);
   }

   public void writeTo(OutputStream outstream) throws IOException {
      if (outstream instanceof MetricAware) {
         MetricAware aware = (MetricAware)outstream;
         if (aware.isMetricActivated()) {
            super.writeTo(outstream);
            return;
         }
      }

      this.writeToWithMetrics(outstream);
   }

   private void writeToWithMetrics(OutputStream outstream) throws IOException {
      if (outstream == null) {
         throw new IllegalArgumentException("Output stream may not be null");
      } else {
         InputStream content = this.getContent();
         long length = this.getContentLength();
         InputStream instream = content;

         try {
            byte[] buffer = new byte[2048];
            int l;
            if (length < 0L) {
               while((l = instream.read(buffer)) != -1) {
                  long startNano = this.helper.startTiming();
                  outstream.write(buffer, 0, l);
                  this.helper.increment(l, startNano);
               }
            } else {
               for(long remaining = length; remaining > 0L; remaining -= (long)l) {
                  l = instream.read(buffer, 0, (int)Math.min(2048L, remaining));
                  if (l == -1) {
                     break;
                  }

                  long startNano = this.helper.startTiming();
                  outstream.write(buffer, 0, l);
                  this.helper.increment(l, startNano);
               }
            }
         } finally {
            this.helper.reportMetrics();
            instream.close();
         }
      }
   }
}
