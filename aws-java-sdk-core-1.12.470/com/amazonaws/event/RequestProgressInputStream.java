package com.amazonaws.event;

import java.io.InputStream;

class RequestProgressInputStream extends ProgressInputStream {
   RequestProgressInputStream(InputStream is, ProgressListener listener) {
      super(is, listener);
   }

   @Override
   protected void onReset() {
      SDKProgressPublisher.publishRequestReset(this.getListener(), this.getNotifiedByteCount());
   }

   @Override
   protected void onEOF() {
      this.onNotifyBytesRead();
   }

   @Override
   protected void onNotifyBytesRead() {
      SDKProgressPublisher.publishRequestBytesTransferred(this.getListener(), (long)this.getUnnotifiedByteCount());
   }
}
