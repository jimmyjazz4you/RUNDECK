package com.amazonaws.services.s3.model;

import com.amazonaws.event.ProgressEventType;

@Deprecated
public class ProgressEvent extends com.amazonaws.event.ProgressEvent {
   public ProgressEvent(int bytesTransferred) {
      super((long)bytesTransferred);
   }

   public ProgressEvent(long bytesTransferred) {
      super(ProgressEventType.BYTE_TRANSFER_EVENT, bytesTransferred);
   }

   public ProgressEvent(ProgressEventType eventType) {
      super(eventType);
   }

   @Deprecated
   public int getBytesTransfered() {
      return (int)this.getBytesTransferred();
   }
}
