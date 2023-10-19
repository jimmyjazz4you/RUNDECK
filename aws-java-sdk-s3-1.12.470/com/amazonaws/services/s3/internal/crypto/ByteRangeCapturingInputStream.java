package com.amazonaws.services.s3.internal.crypto;

import com.amazonaws.internal.SdkFilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteRangeCapturingInputStream extends SdkFilterInputStream {
   private final long startingPosition;
   private final long endingPosition;
   private long streamPosition;
   private int blockPosition = 0;
   private final byte[] block;
   private long markedStreamPosition;
   private int markedBlockPosition;

   public ByteRangeCapturingInputStream(InputStream in, long startingPosition, long endingPosition) {
      super(in);
      if (startingPosition >= endingPosition) {
         throw new IllegalArgumentException("Invalid byte range specified: the starting position must be less than the ending position");
      } else {
         this.startingPosition = startingPosition;
         this.endingPosition = endingPosition;
         int blockSize = (int)(endingPosition - startingPosition);
         this.block = new byte[blockSize];
      }
   }

   public byte[] getBlock() {
      return this.block;
   }

   public int read() throws IOException {
      int data = super.read();
      if (data == -1) {
         return -1;
      } else {
         if (this.streamPosition >= this.startingPosition && this.streamPosition <= this.endingPosition) {
            this.block[this.blockPosition++] = (byte)data;
         }

         ++this.streamPosition;
         return data;
      }
   }

   public synchronized void mark(int readlimit) {
      super.mark(readlimit);
      if (this.markSupported()) {
         this.markedStreamPosition = this.streamPosition;
         this.markedBlockPosition = this.blockPosition;
      }
   }

   public synchronized void reset() throws IOException {
      super.reset();
      if (this.markSupported()) {
         this.streamPosition = this.markedStreamPosition;
         this.blockPosition = this.markedBlockPosition;
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      int bytesRead = super.read(b, off, len);
      if (bytesRead == -1) {
         return -1;
      } else {
         if (this.streamPosition + (long)bytesRead >= this.startingPosition && this.streamPosition <= this.endingPosition) {
            for(int i = 0; i < bytesRead; ++i) {
               if (this.streamPosition + (long)i >= this.startingPosition && this.streamPosition + (long)i < this.endingPosition) {
                  this.block[this.blockPosition++] = b[off + i];
               }
            }
         }

         this.streamPosition += (long)bytesRead;
         return bytesRead;
      }
   }
}
