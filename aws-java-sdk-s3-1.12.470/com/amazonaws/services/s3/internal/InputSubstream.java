package com.amazonaws.services.s3.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.internal.SdkFilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputSubstream extends SdkFilterInputStream {
   private static final int MAX_SKIPS = 100;
   private long currentPosition;
   private final long requestedOffset;
   private final long requestedLength;
   private final boolean closeSourceStream;
   private long markedPosition = 0L;

   public InputSubstream(InputStream in, long offset, long length, boolean closeSourceStream) {
      super(in);
      this.currentPosition = 0L;
      this.requestedLength = length;
      this.requestedOffset = offset;
      this.closeSourceStream = closeSourceStream;
   }

   public int read() throws IOException {
      byte[] b = new byte[1];
      int bytesRead = this.read(b, 0, 1);
      return bytesRead == -1 ? bytesRead : b[0];
   }

   public int read(byte[] b, int off, int len) throws IOException {
      long skippedBytes;
      for(int count = 0; this.currentPosition < this.requestedOffset; this.currentPosition += skippedBytes) {
         skippedBytes = super.skip(this.requestedOffset - this.currentPosition);
         if (skippedBytes == 0L) {
            if (++count > 100) {
               throw new SdkClientException("Unable to position the currentPosition from " + this.currentPosition + " to " + this.requestedOffset);
            }
         }
      }

      skippedBytes = this.requestedLength + this.requestedOffset - this.currentPosition;
      if (skippedBytes <= 0L) {
         return -1;
      } else {
         len = (int)Math.min((long)len, skippedBytes);
         int bytesRead = super.read(b, off, len);
         this.currentPosition += (long)bytesRead;
         return bytesRead;
      }
   }

   public synchronized void mark(int readlimit) {
      this.markedPosition = this.currentPosition;
      super.mark(readlimit);
   }

   public synchronized void reset() throws IOException {
      this.currentPosition = this.markedPosition;
      super.reset();
   }

   public void close() throws IOException {
      if (this.closeSourceStream) {
         super.close();
      }
   }

   public int available() throws IOException {
      long bytesRemaining;
      if (this.currentPosition < this.requestedOffset) {
         bytesRemaining = this.requestedLength;
      } else {
         bytesRemaining = this.requestedLength + this.requestedOffset - this.currentPosition;
      }

      return (int)Math.min(bytesRemaining, (long)super.available());
   }

   InputStream getWrappedInputStream() {
      return this.in;
   }
}
