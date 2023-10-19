package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.SyncProgressListener;

public abstract class S3SyncProgressListener extends SyncProgressListener implements S3ProgressListener {
   public void progressChanged(ProgressEvent progressEvent) {
   }
}
