package com.amazonaws.services.s3.transfer;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.exception.PauseException;

public interface Download extends AbortableTransfer {
   ObjectMetadata getObjectMetadata();

   String getBucketName();

   String getKey();

   PersistableDownload pause() throws PauseException;
}
