package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class DecodedStreamBuffer {
   private static final Log log = LogFactory.getLog(DecodedStreamBuffer.class);
   private byte[] bufferArray;
   private int maxBufferSize;
   private int byteBuffered;
   private int pos = -1;
   private boolean bufferSizeOverflow;

   public DecodedStreamBuffer(int maxBufferSize) {
      this.bufferArray = new byte[maxBufferSize];
      this.maxBufferSize = maxBufferSize;
   }

   public void buffer(byte read) {
      this.pos = -1;
      if (this.byteBuffered >= this.maxBufferSize) {
         if (log.isDebugEnabled()) {
            log.debug("Buffer size " + this.maxBufferSize + " has been exceeded and the input stream will not be repeatable. Freeing buffer memory");
         }

         this.bufferSizeOverflow = true;
      } else {
         this.bufferArray[this.byteBuffered++] = read;
      }
   }

   public void buffer(byte[] src, int srcPos, int length) {
      this.pos = -1;
      if (this.byteBuffered + length > this.maxBufferSize) {
         if (log.isDebugEnabled()) {
            log.debug("Buffer size " + this.maxBufferSize + " has been exceeded and the input stream will not be repeatable. Freeing buffer memory");
         }

         this.bufferSizeOverflow = true;
      } else {
         System.arraycopy(src, srcPos, this.bufferArray, this.byteBuffered, length);
         this.byteBuffered += length;
      }
   }

   public boolean hasNext() {
      return this.pos != -1 && this.pos < this.byteBuffered;
   }

   public byte next() {
      return this.bufferArray[this.pos++];
   }

   public void startReadBuffer() {
      if (this.bufferSizeOverflow) {
         throw new SdkClientException("The input stream is not repeatable since the buffer size " + this.maxBufferSize + " has been exceeded.");
      } else {
         this.pos = 0;
      }
   }
}
