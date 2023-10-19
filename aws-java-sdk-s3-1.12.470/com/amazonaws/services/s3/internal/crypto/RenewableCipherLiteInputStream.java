package com.amazonaws.services.s3.internal.crypto;

import java.io.IOException;
import java.io.InputStream;

public final class RenewableCipherLiteInputStream extends CipherLiteInputStream {
   private boolean hasBeenAccessed;

   public RenewableCipherLiteInputStream(InputStream is, CipherLite cipherLite) {
      super(is, cipherLite);
   }

   public RenewableCipherLiteInputStream(InputStream is, CipherLite c, int buffsize) {
      super(is, c, buffsize);
   }

   public RenewableCipherLiteInputStream(InputStream is, CipherLite c, int buffsize, boolean multipart, boolean lastMultiPart) {
      super(is, c, buffsize, multipart, lastMultiPart);
   }

   protected RenewableCipherLiteInputStream(InputStream is) {
      super(is);
   }

   @Override
   public boolean markSupported() {
      this.abortIfNeeded();
      return this.in.markSupported();
   }

   @Override
   public void mark(int readlimit) {
      this.abortIfNeeded();
      if (this.hasBeenAccessed) {
         throw new UnsupportedOperationException("Marking is only supported before your first call to read or skip.");
      } else {
         this.in.mark(readlimit);
      }
   }

   @Override
   public void reset() throws IOException {
      this.abortIfNeeded();
      this.in.reset();
      this.renewCipherLite();
      this.resetInternal();
      this.hasBeenAccessed = false;
   }

   @Override
   public int read() throws IOException {
      this.hasBeenAccessed = true;
      return super.read();
   }

   @Override
   public int read(byte[] b) throws IOException {
      this.hasBeenAccessed = true;
      return super.read(b);
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      this.hasBeenAccessed = true;
      return super.read(b, off, len);
   }

   @Override
   public long skip(long n) throws IOException {
      this.hasBeenAccessed = true;
      return super.skip(n);
   }
}
