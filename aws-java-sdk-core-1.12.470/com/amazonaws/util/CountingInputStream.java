package com.amazonaws.util;

import com.amazonaws.internal.SdkFilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends SdkFilterInputStream {
   private long byteCount = 0L;

   public CountingInputStream(InputStream in) {
      super(in);
   }

   public long getByteCount() {
      return this.byteCount;
   }

   @Override
   public int read() throws IOException {
      int tmp = super.read();
      this.byteCount += tmp >= 0 ? 1L : 0L;
      return tmp;
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      int tmp = super.read(b, off, len);
      this.byteCount += tmp >= 0 ? (long)tmp : 0L;
      return tmp;
   }
}
