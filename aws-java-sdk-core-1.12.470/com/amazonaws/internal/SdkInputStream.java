package com.amazonaws.internal;

import com.amazonaws.AbortedException;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.SdkRuntime;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.LogFactory;

public abstract class SdkInputStream extends InputStream implements MetricAware, Releasable {
   protected abstract InputStream getWrappedInputStream();

   @Override
   public final boolean isMetricActivated() {
      InputStream in = this.getWrappedInputStream();
      if (in instanceof MetricAware) {
         MetricAware metricAware = (MetricAware)in;
         return metricAware.isMetricActivated();
      } else {
         return false;
      }
   }

   protected final void abortIfNeeded() {
      if (SdkRuntime.shouldAbort()) {
         try {
            this.abort();
         } catch (IOException var2) {
            LogFactory.getLog(this.getClass()).debug("FYI", var2);
         }

         throw new AbortedException();
      }
   }

   protected void abort() throws IOException {
   }

   @Override
   public void release() {
      IOUtils.closeQuietly(this, null);
      InputStream in = this.getWrappedInputStream();
      if (in instanceof Releasable) {
         Releasable r = (Releasable)in;
         r.release();
      }
   }
}
