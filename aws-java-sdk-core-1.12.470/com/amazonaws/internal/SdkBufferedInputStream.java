package com.amazonaws.internal;

import com.amazonaws.AbortedException;
import com.amazonaws.util.SdkRuntime;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SdkBufferedInputStream extends BufferedInputStream implements MetricAware {
   public SdkBufferedInputStream(InputStream in) {
      super(in);
   }

   public SdkBufferedInputStream(InputStream in, int size) {
      super(in, size);
   }

   @Override
   public boolean isMetricActivated() {
      if (this.in instanceof MetricAware) {
         MetricAware metricAware = (MetricAware)this.in;
         return metricAware.isMetricActivated();
      } else {
         return false;
      }
   }

   protected final void abortIfNeeded() {
      if (SdkRuntime.shouldAbort()) {
         this.abort();
         throw new AbortedException();
      }
   }

   protected void abort() {
   }

   @Override
   public int read() throws IOException {
      this.abortIfNeeded();
      return super.read();
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      this.abortIfNeeded();
      return super.read(b, off, len);
   }

   @Override
   public long skip(long n) throws IOException {
      this.abortIfNeeded();
      return super.skip(n);
   }

   @Override
   public int available() throws IOException {
      this.abortIfNeeded();
      return super.available();
   }

   @Override
   public void close() throws IOException {
      super.close();
      this.abortIfNeeded();
   }

   @Override
   public void mark(int readlimit) {
      this.abortIfNeeded();
      super.mark(readlimit);
   }

   @Override
   public void reset() throws IOException {
      this.abortIfNeeded();
      super.reset();
   }

   @Override
   public boolean markSupported() {
      this.abortIfNeeded();
      return super.markSupported();
   }
}
