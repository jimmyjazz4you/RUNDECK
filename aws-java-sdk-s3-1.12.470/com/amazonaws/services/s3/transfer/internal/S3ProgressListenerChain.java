package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.ProgressListener;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.transfer.PersistableTransfer;

public class S3ProgressListenerChain extends ProgressListenerChain implements S3ProgressListener {
   public S3ProgressListenerChain(ProgressListener... listeners) {
      super(listeners);
   }

   @Override
   public void onPersistableTransfer(PersistableTransfer persistableTransfer) {
      for(ProgressListener listener : this.getListeners()) {
         if (listener instanceof S3ProgressListener) {
            S3ProgressListener s3listener = (S3ProgressListener)listener;
            s3listener.onPersistableTransfer(persistableTransfer);
         }
      }
   }
}
