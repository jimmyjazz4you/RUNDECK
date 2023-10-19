package com.amazonaws.internal;

import com.amazonaws.util.IOUtils;
import java.io.FilterOutputStream;
import java.io.OutputStream;

public class SdkFilterOutputStream extends FilterOutputStream implements MetricAware, Releasable {
   public SdkFilterOutputStream(OutputStream out) {
      super(out);
   }

   @Override
   public boolean isMetricActivated() {
      if (this.out instanceof MetricAware) {
         MetricAware metricAware = (MetricAware)this.out;
         return metricAware.isMetricActivated();
      } else {
         return false;
      }
   }

   @Override
   public final void release() {
      IOUtils.closeQuietly(this, null);
      if (this.out instanceof Releasable) {
         Releasable r = (Releasable)this.out;
         r.release();
      }
   }
}
