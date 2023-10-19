package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.transfer.PersistableTransfer;

public interface S3ProgressListener extends ProgressListener {
   void onPersistableTransfer(PersistableTransfer var1);
}
