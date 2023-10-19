package com.amazonaws.util;

import com.amazonaws.util.json.Jackson;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UnreliableFilterInputStream extends FilterInputStream {
   private int maxNumErrors = 1;
   private int currNumErrors;
   private int bytesReadBeforeException = 100;
   private int marked;
   private int position;
   private final boolean isFakeIOException;
   private int resetCount;
   private int resetIntervalBeforeException;

   public UnreliableFilterInputStream(InputStream in, boolean isFakeIOException) {
      super(in);
      this.isFakeIOException = isFakeIOException;
   }

   @Override
   public int read() throws IOException {
      int read = super.read();
      if (read != -1) {
         ++this.position;
      }

      this.triggerError();
      return read;
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      this.triggerError();
      int read = super.read(b, off, len);
      this.position += read;
      this.triggerError();
      return read;
   }

   @Override
   public void mark(int readlimit) {
      super.mark(readlimit);
      this.marked = this.position;
   }

   @Override
   public void reset() throws IOException {
      ++this.resetCount;
      super.reset();
      this.position = this.marked;
   }

   private void triggerError() throws FakeIOException {
      if (this.currNumErrors < this.maxNumErrors) {
         if (this.position >= this.bytesReadBeforeException) {
            if (this.resetIntervalBeforeException <= 0 || this.resetCount % this.resetIntervalBeforeException == this.resetIntervalBeforeException - 1) {
               ++this.currNumErrors;
               if (this.isFakeIOException) {
                  throw new FakeIOException("Fake IO error " + this.currNumErrors + " on UnreliableFileInputStream: " + this);
               } else {
                  throw new RuntimeException("Injected runtime error " + this.currNumErrors + " on UnreliableFileInputStream: " + this);
               }
            }
         }
      }
   }

   public int getCurrNumErrors() {
      return this.currNumErrors;
   }

   public int getMaxNumErrors() {
      return this.maxNumErrors;
   }

   public UnreliableFilterInputStream withMaxNumErrors(int maxNumErrors) {
      this.maxNumErrors = maxNumErrors;
      return this;
   }

   public UnreliableFilterInputStream withBytesReadBeforeException(int bytesReadBeforeException) {
      this.bytesReadBeforeException = bytesReadBeforeException;
      return this;
   }

   public int getBytesReadBeforeException() {
      return this.bytesReadBeforeException;
   }

   public UnreliableFilterInputStream withResetIntervalBeforeException(int resetIntervalBeforeException) {
      this.resetIntervalBeforeException = resetIntervalBeforeException;
      return this;
   }

   public int getResetIntervalBeforeException() {
      return this.resetIntervalBeforeException;
   }

   public int getMarked() {
      return this.marked;
   }

   public int getPosition() {
      return this.position;
   }

   public boolean isFakeIOException() {
      return this.isFakeIOException;
   }

   public int getResetCount() {
      return this.resetCount;
   }

   @Override
   public String toString() {
      return Jackson.toJsonString(this);
   }
}
