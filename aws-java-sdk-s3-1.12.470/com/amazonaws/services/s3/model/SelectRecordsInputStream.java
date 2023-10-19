package com.amazonaws.services.s3.model;

import com.amazonaws.internal.SdkFilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SelectRecordsInputStream extends SdkFilterInputStream {
   private final SdkFilterInputStream abortableHttpStream;

   SelectRecordsInputStream(InputStream selectResultStream, SdkFilterInputStream abortableHttpStream) {
      super(selectResultStream);
      this.abortableHttpStream = abortableHttpStream;
   }

   public void abort() {
      super.abort();
      this.abortableHttpStream.abort();
   }

   public void close() throws IOException {
      super.close();
      this.abortableHttpStream.close();
   }
}
