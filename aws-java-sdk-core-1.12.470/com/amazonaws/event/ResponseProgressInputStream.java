package com.amazonaws.event;

import java.io.InputStream;

class ResponseProgressInputStream extends ProgressInputStream {
   ResponseProgressInputStream(InputStream is, ProgressListener listener) {
      super(is, listener);
   }

   @Override
   protected void onReset() {
      SDKProgressPublisher.publishResponseReset(this.getListener(), this.getNotifiedByteCount());
   }

   @Override
   protected void onEOF() {
      this.onNotifyBytesRead();
   }

   @Override
   protected void onNotifyBytesRead() {
      SDKProgressPublisher.publishResponseBytesTransferred(this.getListener(), (long)this.getUnnotifiedByteCount());
   }
}
