package com.amazonaws.services.s3.internal;

import com.amazonaws.internal.SdkFilterInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public abstract class AbstractRepeatableCipherInputStream<T> extends SdkFilterInputStream {
   private final T cipherFactory;
   private final InputStream unencryptedDataStream;
   private boolean hasBeenAccessed;

   protected AbstractRepeatableCipherInputStream(InputStream input, FilterInputStream cipherInputStream, T cipherFactory) {
      super(cipherInputStream);
      this.unencryptedDataStream = input;
      this.cipherFactory = cipherFactory;
   }

   public boolean markSupported() {
      this.abortIfNeeded();
      return this.unencryptedDataStream.markSupported();
   }

   public void mark(int readlimit) {
      this.abortIfNeeded();
      if (this.hasBeenAccessed) {
         throw new UnsupportedOperationException("Marking is only supported before your first call to read or skip.");
      } else {
         this.unencryptedDataStream.mark(readlimit);
      }
   }

   public void reset() throws IOException {
      this.abortIfNeeded();
      this.unencryptedDataStream.reset();
      this.in = this.createCipherInputStream(this.unencryptedDataStream, this.cipherFactory);
      this.hasBeenAccessed = false;
   }

   public int read() throws IOException {
      this.hasBeenAccessed = true;
      return super.read();
   }

   public int read(byte[] b) throws IOException {
      this.hasBeenAccessed = true;
      return super.read(b);
   }

   public int read(byte[] b, int off, int len) throws IOException {
      this.hasBeenAccessed = true;
      return super.read(b, off, len);
   }

   public long skip(long n) throws IOException {
      this.hasBeenAccessed = true;
      return super.skip(n);
   }

   protected abstract FilterInputStream createCipherInputStream(InputStream var1, T var2);
}
