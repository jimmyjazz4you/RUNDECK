package com.amazonaws.internal;

import com.amazonaws.AbortedException;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.SdkRuntime;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SdkFilterInputStream extends FilterInputStream implements MetricAware, Releasable {
   private volatile boolean aborted = false;

   protected SdkFilterInputStream(InputStream in) {
      super(in);
   }

   @SdkProtectedApi
   public InputStream getDelegateStream() {
      return this.in;
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

   public void abort() {
      if (this.in instanceof SdkFilterInputStream) {
         ((SdkFilterInputStream)this.in).abort();
      }

      this.aborted = true;
   }

   protected boolean isAborted() {
      return this.aborted;
   }

   @Override
   public int read() throws IOException {
      this.abortIfNeeded();
      return this.in.read();
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      this.abortIfNeeded();
      return this.in.read(b, off, len);
   }

   @Override
   public long skip(long n) throws IOException {
      this.abortIfNeeded();
      return this.in.skip(n);
   }

   @Override
   public int available() throws IOException {
      this.abortIfNeeded();
      return this.in.available();
   }

   @Override
   public void close() throws IOException {
      this.in.close();
      this.abortIfNeeded();
   }

   @Override
   public synchronized void mark(int readlimit) {
      this.abortIfNeeded();
      this.in.mark(readlimit);
   }

   @Override
   public synchronized void reset() throws IOException {
      this.abortIfNeeded();
      this.in.reset();
   }

   @Override
   public boolean markSupported() {
      this.abortIfNeeded();
      return this.in.markSupported();
   }

   @Override
   public void release() {
      SdkIOUtils.closeQuietly(this);
      if (this.in instanceof Releasable) {
         Releasable r = (Releasable)this.in;
         r.release();
      }
   }
}
