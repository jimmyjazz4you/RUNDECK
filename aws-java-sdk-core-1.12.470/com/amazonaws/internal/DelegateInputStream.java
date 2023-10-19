package com.amazonaws.internal;

import java.io.IOException;
import java.io.InputStream;

public class DelegateInputStream extends InputStream {
   protected final InputStream in;

   public DelegateInputStream(InputStream in) {
      this.in = in;
   }

   @Override
   public int read() throws IOException {
      return this.in.read();
   }

   @Override
   public int read(byte[] b) throws IOException {
      return this.in.read(b);
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      return this.in.read(b, off, len);
   }

   @Override
   public long skip(long n) throws IOException {
      return this.in.skip(n);
   }

   @Override
   public int available() throws IOException {
      return this.in.available();
   }

   @Override
   public void close() throws IOException {
      this.in.close();
   }

   @Override
   public void mark(int readlimit) {
      this.in.mark(readlimit);
   }

   @Override
   public void reset() throws IOException {
      this.in.reset();
   }

   @Override
   public boolean markSupported() {
      return this.in.markSupported();
   }
}
